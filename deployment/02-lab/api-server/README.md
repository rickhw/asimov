

sudo vi /etc/systemd/system/asimov.api-server.service

```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable asimov.api-server.service
sudo systemctl restart asimov.api-server.service
sudo systemctl stop asimov.api-server.service
sudo systemctl start asimov.api-server.service
sudo systemctl status asimov.api-server.service
sudo journalctl -u asimov.api-server.service -f
```

/home/ubuntu/.sdkman/candidates/java/current/bin/java -Dlogging.config=/home/ubuntu/asimov/api-server/conf/logback-spring.xml -jar /home/ubuntu/asimov/api-server/api-server.jar 