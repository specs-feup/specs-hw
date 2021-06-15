set confirm off
undisplay
set print address off
set height 0
file <ELFNAME>
target remote | <QEMUBIN> -nographic -M arm-generic-fdt -dtb <DTBFILE> -device loader,file=<ELFNAME>,cpu-num=0 -device loader,addr=0xfd1a0104,data=0x8000000e,data-len=4 -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
