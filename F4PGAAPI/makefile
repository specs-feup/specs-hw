current_dir := ${CURDIR}
TARGET := basys3
TOP := <put the name of your top module here>
SOURCES := ${current_dir}/<HDL file 1> \
       ${current_dir}/<HDL file 2> \
       ${current_dir}/<HDL file 3> \
       ...
       ${current_dir}/<HDL file n> \

# Include your constraint file path(s) below. Use either an XDC file
# or a PCF+SDC pair. Don't use all three file types.
XDC := ${current_dir}/<name of your pcf file if applicable>
PCF := ${current_dir}/<name of your xdc file if applicable>
SDC := ${current_dir}/<name of your sdc file if applicable>

ifeq ($(TARGET),arty_35)
ifdef F4PGA_USE_DEPRECATED
  XDC := ${current_dir}/arty.xdc
else
build:
	f4pga build --flow ./flow.json
endif
else ifeq ($(TARGET),arty_100)
  XDC := ${current_dir}/arty.xdc
else ifeq ($(TARGET),nexys4ddr)
  XDC := ${current_dir}/nexys4ddr.xdc
else ifeq ($(TARGET),zybo)
  XDC := ${current_dir}/zybo.xdc
  SOURCES:=${current_dir}/counter_zynq.v
else ifeq ($(TARGET),nexys_video)
  XDC := ${current_dir}/nexys_video.xdc
else ifeq ($(TARGET),basys3)
  XDC := ${current_dir}/basys3.xdc
endif

include <path to f4pga-examples root directory>/common/common.mk





