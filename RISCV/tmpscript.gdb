set confirm off
undisplay
set print address off
set height 0
file /home/ubuntu/Dev/specs-feup/specs-hw/RISCV/./org/specs/Riscv/asm/example.elf
target remote | /home/ubuntu/qemu/qemu-system-riscv32 -machine virt -nographic  -m 256M -kernel /home/ubuntu/Dev/specs-feup/specs-hw/RISCV/./org/specs/Riscv/asm/example.elf -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
while $pc != 0x00
info registers
stepi 1
x/x $pc
end
kill
quit
