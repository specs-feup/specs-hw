# specs-hw
Hardware-related libraries and applications

# Description

Coming soon!

# Installing Dependencies

The framework uses a combination of QEMU and GBD to retrieve instruction traces. As of commit *05c821f2*, the QEMU and GDB processes are launched via Java, and communicate via TPC on localhost:1234. In toder to support Microblaze, ARM, and RISC-V, the respective binutils and QEMU builds are required.

## QEMU For MicroBlaze and ARM (aarch64)

We target implementations on Xilinx development boards (mostly for R&D and testing purposes), so we must rely on Xilinx's [fork of QEMU](https://github.com/Xilinx/qemu.git), since the QEMU builds for MicroBlaze and ARM builds in this fork support additional machine types (e.g. *microblaze-fdt*, to support a baremetal device tree). To build the most recent version of QEMU for both targets, follow the next steps. 

```
git clone https://github.com/Xilinx/qemu.git
git checkout xilinx-v2021.1
```

For Microblaze, configure with:

```
./configure --target-list="microblazeel-softmmu" --prefix=<install dir>
make
make install
```

For ARM (aarch64), configure with:

```
./configure --target-list="aarch64-softmmu" --prefix=<install dir>
make
make install
```

Add the install paths to your PATH. Both targets can be compiled at once and installed in the same install path.

## QEMU For RISCV-V

For RISC-V we employ the latest [official](https://github.com/qemu/qemu) QEMU release. Since this is a different repo entirely from the Xilinx fork, use a different install path for this target.

```
./configure --target-list="riscv32-softmmu" --prefix=<install dir>
make 
make install
```

## Compiling binutils and the cross-compilers for MicroBlaze, aarch64, and RISC-V

We currently rely on generating ELF files by cross-compilation for the architecture's we wish to target/explore. Generating the required binutils (especially objdump and gdb) for each target is lenghty. Some auxiliary resources about cross-compilation in general can be found [here](https://www6.software.ibm.com/developerworks/education/l-cross/l-cross-ltr.pdf), [here](https://wiki.osdev.org/GCC_Cross-Compiler), and [here](http://www.ifp.illinois.edu/~nakazato/tips/xgcc.html).

For the build flow we follow, see [this](doc/README.md) README for more details.

### Generating the DTBs for MicroBlaze Baremetal Runs

The repository already has the DTBs that QEMU (qemu-system-microblazeel) requires to execute, but here is how we generated them: we created an example Vivado 2021.1 project for the ZCU102 board, instantiated only a MicroBlaze with block rams and necessary components. Then we exported the XSA (hardware description file) via File->Export->Export Hardware. (The XSA file for the MicroBlaze is also included in the repo.) Then, we ran *xsct* and performed the following commands:

```
hsi open_hw_design mb_baremetal.xsa
hsi set_repo_path <path to device-tree-xnlx>
hsi create_sw_design device-tree -os device_tree -proc microblaze_0
hsi generate_target -dir .
```

This generates the a pl.dtsi file and a system-top.dts file. The system-top.dts file has a **SYNTAX ERROR**. The "#include" line should be "/include/". Change that. Then run:

```
dtc -I dts -O dtb -o system.dtb system-top.dts
```

The resulting DTB file is passed to *qemu-system-microblazeel* along with the kernel to execute and other options.

All the ELF files for the MicroBlaze that are bundled with the repo were compiled for this hardware description.

# Publications

- N. Paulino, J. C. Ferreira, "A Binary Translation Framework for Automated Hardware Generation", Demo at the 2020 International Design Automation and Test in Europe Conference (DATE), 2020

- N. Paulino, J. C. Ferreira, J. Bispo and J. M. P. Cardoso, ["Executing ARMv8 Loop Traces on Reconfigurable Accelerator via Binary Translation Framework"](https://ieeexplore.ieee.org/document/9221508), 2020 30th International Conference on Field-Programmable Logic and Applications (FPL), Gothenburg, Sweden, 2020, pp. 367-367, doi: 10.1109/FPL50879.2020.00072.

- N. Paulino, J. Bispo, J. C. Ferreira and J. M. P. Cardoso, "A Binary Translation Framework for Automated Hardware Generation," in IEEE Micro, vol. 41, no. 4, pp. 15-23, 1 July-Aug. 2021, doi: 10.1109/MM.2021.3088670.

# How to Cite

```
@misc{specshw,
  author        = {Paulino, Nuno and Bispo, João},
  year          = "2020",
  title         = "Hardware-Related Libraries and Applications",
  url           = "https://github.com/specs-feup/specs-hw"
}
```

## Authors

* **Nuno Paulino** - *Creator and developer* - [ResearchGate](https://www.researchgate.net/profile/Nuno_Paulino2)

* **João Bispo** - *Developer* - [ResearchGate](https://www.researchgate.net/profile/Joao-Bispo)

