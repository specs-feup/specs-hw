set confirm off
undisplay
set print address off
set height 0
file <ELFNAME>
target remote localhost:1234
while $pc != 0x80
stepi 1
x/x $pc
end
<KILL>
quit
