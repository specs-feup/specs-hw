set confirm off
undisplay
set print address off
set height 0
file /home/ubuntu/Dev/specs-feup/specs-hw/MicroBlaze/./org/specs/MicroBlaze/asm/autocor-O2-small.elf
target remote | /home/ubuntu/qemu/qemu-system-microblazeel -nographic -M microblaze-fdt-plnx -m 64 -display none -kernel /home/ubuntu/Dev/specs-feup/specs-hw/MicroBlaze/./org/specs/MicroBlaze/asm/autocor-O2-small.elf -dtb /home/ubuntu/Dev/specs-feup/specs-hw/MicroBlaze/./org/specs/MicroBlaze/qemu/system.dtb -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
while $pc != 0x80
stepi 1
x/x $pc
end
kill
quit
