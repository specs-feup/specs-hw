# F4PGA API

## What is F4PGA

## F4PGA download and installation

### Requirements

Update and install xz-utils depending on your Linux distribution:
			
	Debian:
	
		apt update -y
		apt install -y git wget xz-utils
		
	CentOS:
	
		yum update -y
		yum install -y git wget which xz
		
	Fedora:
	
		dnf install -y findutils git wget which xz

	
		Clone a repository (example):
		
			git clone https://github.com/chipsalliance/f4pga-examples
			cd f4pga-examples 
	
	
### Installing F4PGA toolchain:
		
#### Installing Conda package manager:
			
	wget https://repo.continuum.io/miniconda/Miniconda3-latest-Linux-x86_64.sh -O conda_installer.sh
			
			
#### Choosing installation directory
			
Directory can be either home directory (~/opt/f4pga) or system directory (/opt/f4pga)
				
				If you choose system directory, you'll need root permition to installation.
				Add sudo commands to instructions:
				
					export export F4PGA_INSTALL_DIR=~/opt/f4pga
					
				
				Select one target FPGA family:
				
					Artix 7:
			
						export FPGA_FAM=xc7
						
					
					EOS S3:
			
						export FPGA_FAM=eos-s3
						
				
				Setup Conda and System's environment:
				
					bash conda_installer.sh -u -b -p $F4PGA_INSTALL_DIR/$FPGA_FAM/conda;
					source "$F4PGA_INSTALL_DIR/$FPGA_FAM/conda/etc/profile.d/conda.sh";
					conda env create -f $FPGA_FAM/environment.yml
			
			
			
			- Downloading architecture definitions / installing toolchain
			
				Artix-7
				
					mkdir -p $F4PGA_INSTALL_DIR/xc7/install

					F4PGA_TIMESTAMP='20220714-173445'
					F4PGA_HASH='f7afc12'

					for PKG in install xc7a50t_test xc7a100t_test xc7a200t_test xc7z010_test; do
						wget -qO- https://storage.googleapis.com/symbiflow-arch-defs/artifacts/prod/foss-fpga-tools/symbiflow-arch-defs/continuous/install/${F4PGA_TIMESTAMP}/symbiflow-arch-defs-${PKG}-${F4PGA_HASH}.tar.xz | tar -xJC $F4PGA_INSTALL_DIR/${FPGA_FAM}/install
					done
					
					
				EOS-S3
				
					wget -qO- https://storage.googleapis.com/symbiflow-arch-defs-install/quicklogic-arch-defs-qlf-fc5d8da.tar.gz | tar -xzC $F4PGA_INSTALL_DIR/$FPGA_FAM/

## F4PGA workflow

1. run the config file
the configuration depends on the FPGA family: (artix 7 or eos s3)
(the current configuration file is for the artix 7 family)

2. Building
 the building option are depending on the hardware you running: 
 arty 35t
 arty 100t
 nexys 4 ddr
 basys 3
 Nexys Video
 Zybo Z7


	
	
	
	
	
