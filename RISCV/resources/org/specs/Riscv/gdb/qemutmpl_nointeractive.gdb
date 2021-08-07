set confirm off
undisplay
set print address off
set height 0
file <ELFNAME>
target remote localhost:<PORT>
while $pc != 0x00
stepi 1
x/x $pc
end
kill
quit
