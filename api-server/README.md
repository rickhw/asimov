

vi /etc/systemd/system/asimov.api-server.service

```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable asimov.api-server.service
sudo systemctl restart asimov.api-server.service
sudo systemctl stop asimov.api-server.service
sudo systemctl status asimov.api-server.service
sudo journalctl -u asimov.api-server.service -f
```