set confirm off
undisplay
set print address off
set height 0
file C:/Users/Nuno/git/specs-hw/ARM/./org/specs/Arm/asm/N10/innerprod_N10.elf
target remote | qemu-system-aarch64.exe -nographic -M arm-generic-fdt -dtb C:/Users/Nuno/git/specs-hw/ARM/./org/specs/Arm/qemu/zcu102-arm.dtb -device loader,file=C:/Users/Nuno/git/specs-hw/ARM/./org/specs/Arm/asm/N10/innerprod_N10.elf,cpu-num=0 -device loader,addr=0xfd1a0104,data=0x8000000e,data-len=4 -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
stepi 1
set $v = 0
while $pc != $v
set $v = $pc
stepi 1
x/x $pc
end
monitor quit
disconnect
quit
