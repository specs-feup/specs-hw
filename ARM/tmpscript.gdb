set confirm off
undisplay
set print address off
set height 0
file /media/nuno/HDD/work/projects/specs-hw/ARM/./org/specs/Arm/asm/cholesky.elf
target remote | /media/nuno/HDD/work/projects/xilinx-repos/qemu/aarch64-softmmu/qemu-system-aarch64 -nographic -M arm-generic-fdt -dtb /media/nuno/HDD/work/projects/specs-hw/ARM/./org/specs/Arm/qemu/zcu102-arm.dtb -device loader,file=/media/nuno/HDD/work/projects/specs-hw/ARM/./org/specs/Arm/asm/cholesky.elf,cpu-num=0 -device loader,addr=0xfd1a0104,data=0x8000000e,data-len=4 -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
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
