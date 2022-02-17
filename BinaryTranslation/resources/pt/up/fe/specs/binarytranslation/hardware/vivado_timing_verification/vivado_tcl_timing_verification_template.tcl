set outputDir ../reports
set moduleName <MODULE_NAME>

read_verilog [glob ../hdl/*.sv]

set device2test xcau25p-ffvb676-1-e
synth_design -top $moduleName -name $moduleName -part $device2test
report_timing -file $outputDir/timing_$device2test.rpt
report_power -file $outputDir/power_$device2test.rpt
report_utilization -file $outputDir/utilization_$device2test.rpt

set device2test xc7s6cpga196-1
synth_design -top $moduleName -name $moduleName -part $device2test
report_timing -file $outputDir/timing_$device2test.rpt
report_power -file $outputDir/power_$device2test.rpt
report_utilization -file $outputDir/utilization_$device2test.rpt

exit
