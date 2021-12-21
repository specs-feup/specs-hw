set confirm off
undisplay
set print address off
set height 0
maintenance set internal-error backtrace off
maintenance set internal-warning backtrace off
file <ELFNAME>
target remote localhost:<PORT>
