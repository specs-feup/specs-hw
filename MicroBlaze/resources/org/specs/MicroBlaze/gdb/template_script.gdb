set confirm off
target remote | ../../../qemu/microblazeel-softmmu/qemu-system-microblazeel -M microblaze-fdt-plnx -m 64 -device loader,file=./helloworld/Debug/helloworld.elf -gdb stdio -hw-dtb ./device_tree_bsp_0/system.dtb -display none -S
file ./helloword/Debug/helloworld.elf
break _exit
set height 0
set logging file test.log
set logging on
while $pc != 0x80
stepi 
disas/r $pc,$pc+1
end 
quit

