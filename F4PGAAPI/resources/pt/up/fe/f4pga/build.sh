#!/bin/sh

export PATH= /<INSTALATION_DIR>/<FAM_NAME>/install/bin:$PATH
source <INSTALATION_DIR>/<FAM_NAME>/conda/etc/profile.d/conda.sh
conda activate <FAM_NAME>
f4pga build --flow ./flow.json
