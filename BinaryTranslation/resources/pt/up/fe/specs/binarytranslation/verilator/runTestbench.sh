verilator -cc $1_tb 
verilator -cc $1_tb --top-module $1_tb -exe custominstructionunit_tb.cc
make -C obj_dir/  -f V$1_tb.mk
./obj_dir/V$1_tb
