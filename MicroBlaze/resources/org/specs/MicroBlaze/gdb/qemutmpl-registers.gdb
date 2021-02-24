set confirm off
undisplay
set print address off
set height 0
file <ELFNAME>
target remote | <QEMUBIN> -nographic -M microblaze-fdt-plnx -m 64 -display none -kernel <ELFNAME> -dtb <DTBFILE> -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
while $pc != 0x80
info registers
stepi 1
x/x $pc
end
kill
quit
