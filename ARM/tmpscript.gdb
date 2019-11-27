set confirm off
undisplay
set print address off
set height 0
file /media/nuno/HDD/work/projects/specs-hw/ARM/./org/specs/Arm/asm/aarch64_bare_metal_qemu/test64.elf
target remote | /usr/bin/qemu-system-aarch64 -M virt -cpu cortex-a53 -nographic -kernel /media/nuno/HDD/work/projects/specs-hw/ARM/./org/specs/Arm/asm/aarch64_bare_metal_qemu/test64.elf -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
set $v = 0
while $pc != $v
set $v = $pc
stepi 1
x/x $pc
end
kill
quit
