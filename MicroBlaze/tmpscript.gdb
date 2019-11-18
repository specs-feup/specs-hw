set confirm off
target remote | /media/nuno/HDD/work/projects/qemu/microblazeel-softmmu/qemu-system-microblazeel -M microblaze-fdt-plnx -m 64 -device loader,file=./resources/org/specs/MicroBlaze/asm/test/helloworld.elf -gdb stdio -hw-dtb ./resources/org/specs/MicroBlaze/qemu/system.dtb -display none -S
file ./resources/org/specs/MicroBlaze/asm/test/helloworld.elf
break _exit
set height 0
set logging file test.log
set logging on
while $pc != 0x80
stepi
disas/r $pc,$pc+1
end
quit
