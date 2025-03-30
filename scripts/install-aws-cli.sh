
# EC2 執行個體是 ARM 架構（如 Graviton2/Graviton3)
curl "https://awscli.amazonaws.com/awscli-exe-linux-aarch64.zip" -o "awscliv2.zip"

sudo apt install unzip -y

unzip awscliv2.zip

sudo ./aws/install

aws --version
