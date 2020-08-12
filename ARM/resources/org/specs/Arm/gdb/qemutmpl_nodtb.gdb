set confirm off
undisplay
set print address off
set height 0
file <ELFNAME>
target remote | <QEMUBIN> -M virt -cpu cortex-a53 -nographic -kernel <ELFNAME> -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
stepi 1
set $v = 0
while $pc != $v
set $v = $pc
stepi 1
x/x $pc
end
kill
quit

