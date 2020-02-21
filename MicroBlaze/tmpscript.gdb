set confirm off
undisplay
set print address off
set height 0
file /media/nuno/HDD/work/projects/specs-hw/MicroBlaze/./org/specs/MicroBlaze/asm/cholesky.elf
target remote | /media/nuno/HDD/work/projects/myqemus/qemu-system-microblazeel -M microblaze-fdt-plnx -m 64 -display none -kernel /media/nuno/HDD/work/projects/specs-hw/MicroBlaze/./org/specs/MicroBlaze/asm/cholesky.elf -dtb /media/nuno/HDD/work/projects/specs-hw/MicroBlaze/./org/specs/MicroBlaze/qemu/system.dtb -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
while $pc != 0x80
stepi 1
x/x $pc
end
kill
quit

