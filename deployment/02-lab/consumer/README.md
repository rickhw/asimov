

sudo vi /etc/systemd/system/asimov.consumer-server.service

```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable asimov.consumer-server.service
sudo systemctl restart asimov.consumer-server.service
sudo systemctl stop asimov.consumer-server.service
sudo systemctl start asimov.consumer-server.service
sudo systemctl status asimov.consumer-server.service
sudo journalctl -u asimov.consumer-server.service -f
```

/home/ubuntu/.sdkman/candidates/java/current/bin/java -Dlogging.config=/home/ubuntu/asimov/consumer-server/conf/logback-spring.xml -jar /home/ubuntu/asimov/consumer-server/consumer-server.jar 