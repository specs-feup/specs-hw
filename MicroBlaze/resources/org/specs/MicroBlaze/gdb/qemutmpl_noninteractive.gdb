set confirm off
undisplay
set print address off
set height 0
maintenance set internal-error backtrace off
maintenance set internal-warning backtrace off
file <ELFNAME>
target remote localhost:<PORT>
while $pc != 0x80
stepi 1
x/x $pc
end
<KILL>
quit
