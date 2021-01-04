set confirm off
undisplay
set print address off
set height 0
file /media/nuno/HDD/work/projects/specs-hw/RISCV/./org/specs/Riscv/asm/N100iamf/innerprod_N100iamf.elf
target remote | /media/nuno/HDD/work/projects/myqemus/qemu-system-riscv32 -machine virt -nographic  -m 256M -kernel /media/nuno/HDD/work/projects/specs-hw/RISCV/./org/specs/Riscv/asm/N100iamf/innerprod_N100iamf.elf -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
while $pc != 0x00
stepi 1
x/x $pc
end
kill
quit
