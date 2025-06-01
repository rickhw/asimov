以下是在 **Ubuntu 24.04** 上安裝 **Filebeat** 並將日誌傳送到 **Elasticsearch** 的完整步驟，包括設定與驗證流程：

---

## 🧰 1. 安裝 Filebeat

### 步驟 1.1：導入 Elastic GPG 金鑰並加入套件來源

```bash
curl -fsSL https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo gpg --dearmor -o /usr/share/keyrings/elastic.gpg
```

```bash
echo "deb [signed-by=/usr/share/keyrings/elastic.gpg] https://artifacts.elastic.co/packages/8.x/apt stable main" | sudo tee /etc/apt/sources.list.d/elastic-8.x.list
```

### 步驟 1.2：更新 apt 並安裝 filebeat

```bash
sudo apt update
sudo apt install filebeat
```

---

## 🧾 2. 設定 Filebeat 輸出到 Elasticsearch

### 步驟 2.1：修改 `/etc/filebeat/filebeat.yml`

打開設定檔：

```bash
sudo nano /etc/filebeat/filebeat.yml
```

設定重點如下：

#### ✅ 啟用 log input：

```yaml
filebeat.inputs:
  - type: filestream
    enabled: true
    paths:
      - /var/log/*.log
```

#### ✅ 設定 Elasticsearch 輸出：

```yaml
output.elasticsearch:
  hosts: ["http://localhost:9200"]  # 或你的 Elastic URL
  username: "elastic"               # 根據實際帳密
  password: "your_password"
```

#### ✅ 註解掉 Logstash 輸出（避免衝突）：

```yaml
#output.logstash:
#  hosts: ["localhost:5044"]
```

> 若你的 Elasticsearch 開啟 TLS，請改用 `https://...` 並設定憑證相關資訊。

---

## 📦 3. 啟用並載入 Filebeat 模組（可選）

### 步驟 3.1：啟用內建模組，例如 nginx、system 等：

```bash
sudo filebeat modules enable system
sudo filebeat modules enable nginx
```

### 步驟 3.2：載入模組對應的 index template、pipeline、dashboard：

```bash
sudo filebeat setup
```

---

## ▶️ 4. 啟動與開機啟動 Filebeat

```bash
sudo systemctl enable filebeat
sudo systemctl start filebeat
```

---

## 🔎 5. 驗證 Filebeat 是否正常傳送 log 到 Elasticsearch

### 方法 1：查詢 log：

```bash
sudo journalctl -u filebeat -f
```

### 方法 2：用 curl 驗證是否有 index 建立

```bash
curl -X GET "localhost:9200/_cat/indices/filebeat-*?v"
```

### 方法 3：在 Kibana 的「Discover」中查看 filebeat-\* index pattern（如果你有 Kibana）

---

## 📁 6. 自訂收集的 Log 路徑（可選）

如果你有自訂應用程式 log，例如 `/opt/myapp/logs/app.log`：

```yaml
filebeat.inputs:
  - type: filestream
    enabled: true
    paths:
      - /opt/myapp/logs/*.log
```

---

## 📌 小提醒

* 如果是 Elastic Stack 8.x，預設開啟 TLS 與安全性機制，請確認 Filebeat 使用的帳密與憑證正確。
* 可使用 `filebeat test output` 驗證是否能連接 Elasticsearch：

  ```bash
  sudo filebeat test output
  ```

---

如果你需要搭配 **Elastic Cloud**、**Docker 上的 Elastic**，或將 log 傳送到 **Logstash** 而不是直接傳到 Elasticsearch，我可以提供對應範例。是否要補充？
