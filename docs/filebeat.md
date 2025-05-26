以下是在 Ubuntu 24.04 上安裝並設定 Filebeat 的完整步驟，包括指定 Elasticsearch 為輸出目的地，並監控應用程式 log 檔的範例（例如 Spring Boot 的 `application.log`）：

---

## ✅ 一、安裝 Filebeat（Elastic 官方套件）

```bash
# 匯入 Elastic 公鑰
curl -fsSL https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo gpg --dearmor -o /usr/share/keyrings/elastic-archive-keyring.gpg

# 新增套件來源
echo "deb [signed-by=/usr/share/keyrings/elastic-archive-keyring.gpg] https://artifacts.elastic.co/packages/8.x/apt stable main" | \
  sudo tee /etc/apt/sources.list.d/elastic-8.x.list

# 更新套件並安裝 filebeat
sudo apt update
sudo apt install filebeat
```

---

## ✅ 二、設定 Filebeat 輸出到 Elasticsearch

修改設定檔 `/etc/filebeat/filebeat.yml`：

```yaml
# ============================== Filebeat inputs ===============================

filebeat.inputs:
  - type: filestream
    id: my-app-logs
    enabled: true
    paths:
      - /var/log/myapp/application.log

# ============================== Elasticsearch output ===========================

output.elasticsearch:
  hosts: ["http://localhost:9200"]
  username: "elastic"
  password: "your_elastic_password"

# ============================ Setup Kibana (optional) ==========================

setup.kibana:
  host: "http://localhost:5601"

# ============================ Logging ==========================================

logging.level: info
```

> 📌 說明：

* `/var/log/myapp/application.log` 是你要收集的應用程式 log 路徑，請根據實際狀況修改。
* `hosts` 是 Elasticsearch 的位置，如果是遠端伺服器請改成 IP。
* 輸出也可以改為 `output.logstash`，如果你使用 Logstash 處理。
* 如果沒有設定密碼認證的 Elasticsearch，也可以先註解掉 `username/password`。

---

## ✅ 三、測試 Filebeat 設定檔

```bash
sudo filebeat test config
```

若無錯誤訊息，即表示設定檔合法。

---

## ✅ 四、啟用並啟動 Filebeat

```bash
sudo systemctl enable filebeat
sudo systemctl start filebeat
```

確認是否啟動成功：

```bash
sudo systemctl status filebeat
```

---

## ✅ 五、(可選) 啟用 Filebeat Elasticsearch dashboard 與 index template

```bash
sudo filebeat setup --index-management -E output.elasticsearch.hosts=["http://localhost:9200"]
sudo filebeat setup --dashboards
```

這會：

* 在 Elasticsearch 中建立 index template（如：`filebeat-*`）
* 安裝 Kibana 預設 dashboard（需設定 `setup.kibana`）

---

## ✅ 六、檢查 Filebeat 是否成功送出資料

你可以使用 Kibana 或直接查詢 Elasticsearch：

```bash
curl -X GET "localhost:9200/filebeat-*/_search?pretty"
```

---

## ✅ 七、範例：Spring Boot Log 配置

如果你是 Spring Boot 應用程式：

```yaml
# application.yml
logging:
  file:
    name: /var/log/myapp/application.log
```

然後確保目錄與檔案具有 Filebeat 可讀權限：

```bash
sudo mkdir -p /var/log/myapp
sudo chown -R filebeat:filebeat /var/log/myapp
```

---

若你有更複雜的 log 格式（如 JSON），我也可以協助你加上 parsing 的 processor。

是否要我接下來幫你設定 log 格式解析（例如 JSON）或整合 Logstash？
