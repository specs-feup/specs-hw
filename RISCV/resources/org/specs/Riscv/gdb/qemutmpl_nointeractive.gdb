set confirm off
undisplay
set print address off
set height 0
file <ELFNAME>
target remote | <QEMUBIN> -machine virt -nographic  -m 128M -kernel <ELFNAME> -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
while $pc != 0x00
stepi 1
x/x $pc
end
kill
quit
