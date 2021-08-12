# Compiling binutils and the cross-compilers for MicroBlaze, aarch64, and RISC-V

We provide a [script](./build.sh) script in this directory which can (should!) handle the build and installation of required tools. The script is meant to be run in a folder containing the required source tarballs. For MicroBlaze and aarch64, its possible to use the builds packaged with Xilinx's tools (e.g., Vitis 2021.1), if you have them installed (also applies to their builds of QEMU for these two architectures). However, you can also fetch [this zip](https://www.xilinx.com/bin/public/openDownload?filename=mb-gnu-2021-0623.tar.gz), from [this page](https://www.xilinx.com/products/design-tools/guest-resources.html#2021). (Note: if anyone can point to a direction where Xilinx's stores these forks of binutils, we would be grateful. We can only find stale and deprecated repos...).

Do not try to use the mainline binutils, gcc, and newlib repos (mirror [here](https://github.com/bminor) if you need them), since Xilinx's changes are not reflected in the mainline. 

If you do not wish to use the script, or require the individual steps per architecture, untar the *binutils-2_35.tar.gz*, *gcc-10_2.tar.gz*, and *newlib-3_3_0.tar.gz*, and follow the next sections. As with typical gcc builds, the process involves building binutils, a first stage bootstrap gcc, newlib (alternatively glibc), and the final gcc suite.

Our most tested build enviroment was Ubuntu 20.4.

## Build Flow for MicroBlaze and aarch64

### Setup for MicroBlaze

If your target is "microblaze" set the following variables. Change the program-prefix and prefix to your liking, then follow the flow in 

```
export TARGET=microblaze-xilinx-elf
export PREFIX=<installation dir>
export PROGRAM_PREFIX="mb-"
export CONFIGUREFLAGS=" --disable-initfini_array --disable-__cxa_atexit --enable-target-optspace --without-long-double-128 "
```

### Setup for aarch64

If your target is "aarch64" (ARMv8) set the following variables. Change the program-prefix and prefix to your liking, then follow the flow in 

```
export TARGET=aarch64-xilinx-elf
export PREFIX=<installation dir>
export PROGRAM_PREFIX="aarch64-xilinx-elf-"
export CONFIGUREFLAGS=" --disable-multiarch --with-arch=armv8-a "
```

### Extra: Setup for builds under mingw64

(This is relatively untested!). For building under mingw64 add the flags: 

```
-host=x86_64-w64-mingw32 --enable-mingw-wildcard --enable-leading-mingw64-underscores --enable-large-address-aware
```

to all *configure* calls. This was tested with a MSYS2 installation found [here](https://www.msys2.org/). Follow the instruction to install the native build tools on the same page. In summary, in the mingw64 shell:

```
$ pacman -Syu
$ pacman -Su
$ pacman -S --needed base-devel mingw-w64-x86_64-toolchain
```

### Common Build Flow

Add the following common flags to the architecture specific flags of the build:

```
export CONFIGUREFLAGS=$(CONFIGUREFLAGS) --disable-silent-rules --disable-dependency-tracking --with-gnu-ld --enable-shared --enable-languages=c,c++ --enable-threads=posix --enable-multilib --enable-c99 --enable-long-long --enable-libstdcxx-pch --without-local-prefix --disable-install-libiberty --enable-lto --disable-libssp --enable-libitm --disable-bootstrap --with-system-zlib --enable-linker-build-id --with-ppl=no --with-cloog=no --enable-checking=release --enable-cheaders=c_global --without-isl --with-gxx-include-dir=/not/exist/usr/include/c++/10.2.0 --enable-poison-system-directories --enable-nls --with-glibc-version=2.28 --enable-initfini-array --without-headers --with-newlib --disable-libstdcxx-pch --with-newlib --disable-threads --enable-plugins --with-gnu-as --disable-libitm --enable-multilib --with-sysroot=/not/exist
```

Binutils builds are out-of-tree, so create a build dir:

```
mkdir build-binutils
cd build-binutils
../binutils-2_35/configure --target=$TARGET --prefix=$PREFIX --program-prefix=$PROGRAM_PREFIX $CONFIGUREFLAGS
make all
make install
```

For the first stage gcc:

```
mkdir build-gcc1
cd build-gcc1
../gcc-10_2/configure --target=$TARGET --prefix=$PREFIX --program-prefix=$PROGRAM_PREFIX $CONFIGUREFLAGS
make all-gcc
make all-target-libgcc
make install-gcc
make install-target-libgcc
```

For newlib, since a different prefixing scheme is used, set the following variables:

```
export CC_FOR_TARGET="$PROGRAM_PREFIX"gcc
export CXX_FOR_TARGET="$PROGRAM_PREFIX"g++
export GCC_FOR_TARGET="$PROGRAM_PREFIX"gcc
export AR_FOR_TARGET="$PROGRAM_PREFIX"ar
export AS_FOR_TARGET="$PROGRAM_PREFIX"as
export LD_FOR_TARGET="$PROGRAM_PREFIX"ld
export NM_FOR_TARGET="$PROGRAM_PREFIX"nm
export RANLIB_FOR_TARGET="$PROGRAM_PREFIX"ranlib
export STRIP_FOR_TARGET="$PROGRAM_PREFIX"strip
```

Then build:

```
mkdir build-binutils
cd build-binutils
../newlib-3_3_0/configure --target=$TARGET --prefix=$PREFIX --program-prefix=$PROGRAM_PREFIX $CONFIGUREFLAGS
make all
make install
```

Finally, stage 2 gcc:

```
mkdir build-gcc1
cd build-gcc1
../gcc-10_2/configure --target=$TARGET --prefix=$PREFIX --program-prefix=$PROGRAM_PREFIX $CONFIGUREFLAGS
make all
make install
```

Add the install paths to your PATH.

## Build Flow for RISC-V (iam and iamf)

*TODO* (check back later)

