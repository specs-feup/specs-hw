picocom -b <baudRate> --imap lfcrlf /dev/ttyUSB1

#Create directory for server
sudo mkdir -p /tftp
sudo chmod 777 -R /tftp
sudo chown tftp -R /tftp

#Set up TFTP configuration
cat << EOF | sudo tee /etc/default/tftpd-hpa
TFTP_USERNAME="tftp"
TFTP_DIRECTORY="/tftp"
TFTP_ADDRESS=":6069"
TFTP_OPTIONS="--secure"
EOF

#Restart TFTP server
sudo systemctl restart tftpd-hpa

#Add ip address for interface
#IP address example 192.168.100.100/24
ip addr add <ipAddress> dev eth0

mkdir uboot-linux-images
pushd uboot-linux-images
wget -qO- https://github.com/SymbiFlow/symbiflow-xc7z-automatic-tester/releases/download/v1.0.0/uboot-linux-images.zip | bsdtar -xf-
popd




