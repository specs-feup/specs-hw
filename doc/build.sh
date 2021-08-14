#!/bin/bash

# extract tar
function extract {
	echo "Extracting $1..." >&3
	tar -xf $1 && wait
	echo "Extracted $1!" >&3
}

# configure and build
function build {
	echo "Creating build folder $2..." >&3
	mkdir -p $2
	cd $2
	echo "Configuring to build $1..." >&3
	../$1/configure --target=$TARGET --prefix=$PREFIX --program-prefix=$PROGRAM_PREFIX $CONFIGUREFLAGS
	echo "Building $1..." >&3
	make -s $3 && wait
	cd ..
}

# install toolchain
function installToolchain {

	if [ "$1" == "microblaze" ]; then
		ARCH=$1
		TARGET=microblaze-xilinx-elf
		PREFIX=/opt/$1/
		PROGRAM_PREFIX="mb-"
		CONFIGUREFLAGS="--disable-silent-rules --disable-dependency-tracking --with-gnu-ld --enable-shared --enable-languages=c,c++ --enable-threads=posix --enable-multilib --enable-c99 --enable-long-long --enable-libstdcxx-pch --without-local-prefix --enable-lto --disable-libssp --enable-libitm --disable-bootstrap --disable-libmudflap --with-system-zlib --enable-linker-build-id --with-ppl=no --with-cloog=no --enable-checking=release --enable-cheaders=c_global --without-isl --enable-poison-system-directories --enable-nls --with-glibc-version=2.28 --enable-initfini-array --without-headers --with-newlib --disable-initfini_array --disable-__cxa_atexit --disable-libstdcxx-pch --disable-threads --enable-plugins --with-gnu-as --disable-libitm --enable-target-optspace --without-long-double-128 --with-sysroot=/not/exist --disable-werror"

	elif [ "$1" == "aarch64" ]; then
		ARCH=$1
		TARGET=aarch64-xilinx-elf
		PREFIX=/media/nuno/HDD/gnu/install/xilinx/$1/
		PROGRAM_PREFIX="aarch64-xilinx-elf-"
		CONFIGUREFLAGS="--disable-silent-rules --disable-dependency-tracking --with-gnu-ld --enable-shared --enable-languages=c,c++ --enable-threads=posix --enable-multilib --enable-c99 --enable-long-long --enable-libstdcxx-pch --without-local-prefix --disable-install-libiberty --enable-lto --disable-libssp --enable-libitm --disable-bootstrap --with-system-zlib --enable-linker-build-id --with-ppl=no --with-cloog=no --enable-checking=release --enable-cheaders=c_global --without-isl --enable-poison-system-directories --enable-nls --with-glibc-version=2.28 --enable-initfini-array --without-headers --with-newlib --disable-libstdcxx-pch --disable-threads --enable-plugins --with-gnu-as --disable-libitm --disable-multiarch --with-arch=armv8-a --enable-multilib --with-sysroot=/not/exist"
	else
		echo "Invalid arch" >&3
		exit 1;
	fi
	
	mkdir -p $1
	cd $1	
	START_TIME=$SECONDS
	logfile=build.log
	exec 3>&1 >$logfile 2>&1
	
	echo $ARCH >&3
	echo $TARGET >&3
	echo $PREFIX >&3
	echo $PROGRAM_PREFIX >&3			
	echo $CONFIGUREFLAGS >&3	
	export PATH=$PATH:$PREFIX/bin

	## script
	echo "----------" >&3
	echo "Binutils..." >&3
	extract "../binutils-2_35.tar.gz" && wait
	build "binutils-2_35" "build-binutils" "all install" && wait

	echo "----------" >&3
	echo "GCC Stage 1..." >&3
	extract "../gcc-10_2.tar.gz" && wait
	build "gcc-10_2" "build-gcc1" "all-gcc all-target-libgcc install-gcc install-target-libgcc" && wait

	echo "----------" >&3
	echo "newlib..." >&3
	export CC_FOR_TARGET="$PROGRAM_PREFIX"gcc
	export CXX_FOR_TARGET="$PROGRAM_PREFIX"g++
	export GCC_FOR_TARGET="$PROGRAM_PREFIX"gcc
	export AR_FOR_TARGET="$PROGRAM_PREFIX"ar
	export AS_FOR_TARGET="$PROGRAM_PREFIX"as
	export LD_FOR_TARGET="$PROGRAM_PREFIX"ld
	export NM_FOR_TARGET="$PROGRAM_PREFIX"nm
	export RANLIB_FOR_TARGET="$PROGRAM_PREFIX"ranlib
	export STRIP_FOR_TARGET="$PROGRAM_PREFIX"strip
	extract "../newlib-3_3_0.tar.gz" && wait
	build "newlib-3_3_0" "build-newlib" "all install" && wait

	echo "----------" >&3
	echo "GCC Stage 2..." >&3
	build "gcc-10_2" "build-gcc2" "all install" && wait

	echo "----------" >&3
	echo "Removing build dirs..." >&3
	rm -rf ./binutils-2_35 && wait
	rm -rf ./gcc-10_2 && wait
	rm -rf ./newlib-3_3_0 && wait
	cd ..
}

###################################

if [[ $# -eq 0 ]] ; then
    echo 'Specify architecture (microblaze, aarch64)'
    exit 0
fi

echo "----------"
echo "Attempting to build for $1"
installToolchain $1
echo "----------" >&3
echo "Done!" >&3
ELAPSED_TIME=$(($SECONDS - $START_TIME))
echo "$(($ELAPSED_TIME/60)) min $(($ELAPSED_TIME%60)) sec"  >&3

