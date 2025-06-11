在 Ubuntu 24.04 上啟用 Docker 的 **REST API (也稱為 Docker Remote API)**，你需要修改 Docker 的啟動參數來綁定 TCP socket。預設 Docker 只監聽 Unix socket (`/var/run/docker.sock`)，但你可以額外讓它監聽 `tcp://127.0.0.1:2375` 或其他介面。

---

## 🔧 步驟一：修改 Docker 服務設定

執行以下指令編輯 Docker 的 systemd 設定：

```bash
sudo mkdir -p /etc/systemd/system/docker.service.d
sudo nano /etc/systemd/system/docker.service.d/override.conf
```

加入以下內容，將 Docker 設定為同時監聽 Unix 與 TCP：

```ini
[Service]
ExecStart=
ExecStart=/usr/bin/dockerd -H unix:///var/run/docker.sock -H tcp://127.0.0.1:2375
```

這代表：

* `-H unix:///var/run/docker.sock`: 保持原本本地 Unix socket 功能
* `-H tcp://127.0.0.1:2375`: 新增本機 TCP REST API (不建議對外開放，除非有 TLS 保護)

---

## 🔄 步驟二：重新載入並啟動 Docker

```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl restart docker
```

確認 Docker 是否有開在 TCP port：

```bash
sudo ss -lntp | grep 2375
```

你應該會看到類似：

```
LISTEN 0      4096      127.0.0.1:2375      0.0.0.0:*    users:(("dockerd",pid=xxxx,fd=xx))
```

---

## 🧪 步驟三：用 `curl` 測試 Docker REST API 建立 container

### 建立一個 `hello-world` container（範例）

```bash
curl -X POST http://localhost:2375/containers/create \
  -H "Content-Type: application/json" \
  -d '{
        "Image": "hello-world",
        "HostConfig": {
          "AutoRemove": true
        }
      }'
```

結果：

```bash
HTTP/1.1 201 Created
Api-Version: 1.50
Content-Type: application/json
Docker-Experimental: false
Ostype: linux
Server: Docker/28.2.2 (linux)
Date: Mon, 09 Jun 2025 10:54:40 GMT
Content-Length: 88
Connection: close

{
  "Id": "8768827001b41d19dbdfb1869e3254266261322cbbb56e4ecbb2848a094045bf",
  "Warnings": []
}
```


如果你沒有先 `pull` 過 `hello-world`，你可以用：

```bash
curl -X POST http://localhost:2375/images/create?fromImage=hello-world
```

### 啟動剛剛建立的 container（假設你拿到 ID 是 `abc123`）：

```bash
curl -X POST http://localhost:2375/containers/abc123/start
```

或用這一串腳本來自動建立並啟動：

```bash
CONTAINER_ID=$(curl -s -X POST http://localhost:2375/containers/create \
  -H "Content-Type: application/json" \
  -d '{"Image": "hello-world", "HostConfig": {"AutoRemove": true}}' | jq -r .Id)

curl -X POST http://localhost:2375/containers/$CONTAINER_ID/start
```

> 📦 需要安裝 `jq`：

```bash
sudo apt install jq
```

---

## ⚠️ 安全建議

開放 Docker TCP API 是有風險的，**請勿直接綁定 `0.0.0.0:2375`** 在沒有 TLS 保護的情況下。建議：

* 限定 `127.0.0.1`（只限本機存取）
* 若需要遠端控制，請考慮設定 [Docker TLS](https://docs.docker.com/engine/security/https/) 或使用 SSH tunnel。

---

如你需要設定 TLS 或對外暴露 API，我也可以一步步教你配置。是否需要？
