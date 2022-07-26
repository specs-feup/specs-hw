#!/bin/sh

# Seting the installation directory:
export F4PGA_INSTALL_DIR=~/opt/f4pga
# Select your FPGA family:
FPGA_FAM="xc7"
# Prepare the environment
export PATH="$F4PGA_INSTALL_DIR/$FPGA_FAM/install/bin:$PATH";
source "$F4PGA_INSTALL_DIR/$FPGA_FAM/conda/etc/profile.d/conda.sh"
# Entering the working Conda environment:
conda activate $FPGA_FAM
