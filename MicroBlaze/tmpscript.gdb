set confirm off
undisplay
set print address off
set height 0
file /media/nuno/HDD/work/projects/specs-hw/MicroBlaze/./org/specs/MicroBlaze/asm/test/helloworld.elf
target remote | /media/nuno/HDD/work/projects/qemu/microblazeel-softmmu/qemu-system-microblazeel -M microblaze-fdt-plnx -m 64 -device loader,file=/media/nuno/HDD/work/projects/specs-hw/MicroBlaze/./org/specs/MicroBlaze/asm/test/helloworld.elf -gdb stdio -hw-dtb ./resources/org/specs/MicroBlaze/qemu/system.dtb -display none -S
while $pc != 0x80
stepi 1
x/x $pc
end
kill
quit
