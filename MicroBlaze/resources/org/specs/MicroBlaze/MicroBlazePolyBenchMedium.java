/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.specs.MicroBlaze;

import org.specs.BinaryTranslation.ELFProvider;

public enum MicroBlazePolyBenchMedium implements ELFProvider {
    
    //_2mm("org/specs/MicroBlaze/asm/PolyBenchMedium/2mm.elf", null, null),
    //_3mm("org/specs/MicroBlaze/asm/PolyBenchMedium/3mm.elf", null, null),
    adi("org/specs/MicroBlaze/asm/PolyBenchMedium/adi.elf", null, null),
    atax("org/specs/MicroBlaze/asm/PolyBenchMedium/atax.elf", null, null),
    bicg("org/specs/MicroBlaze/asm/PolyBenchMedium/bicg.elf", null, null),
    cholesky("org/specs/MicroBlaze/asm/PolyBenchMedium/cholesky.elf", null, null),
    correlation("org/specs/MicroBlaze/asm/PolyBenchMedium/correlation.elf", null, null),
    covariance("org/specs/MicroBlaze/asm/PolyBenchMedium/covariance.elf", null, null),
    deriche("org/specs/MicroBlaze/asm/PolyBenchMedium/deriche.elf", null, null),
    doitgen("org/specs/MicroBlaze/asm/PolyBenchMedium/doitgen.elf", null, null),
    durbin("org/specs/MicroBlaze/asm/PolyBenchMedium/durbin.elf", null, null),
    //fdtd2d("org/specs/MicroBlaze/asm/PolyBenchMedium/fdtd-2d.elf", null, null),
    floydwarshall("org/specs/MicroBlaze/asm/PolyBenchMedium/floyd-warshall.elf", null, null),
    gemm("org/specs/MicroBlaze/asm/PolyBenchMedium/gemm.elf", null, null),
    gemver("org/specs/MicroBlaze/asm/PolyBenchMedium/gemver.elf", null, null),
    gesummv("org/specs/MicroBlaze/asm/PolyBenchMedium/gesummv.elf", null, null),
    gramschmidt("org/specs/MicroBlaze/asm/PolyBenchMedium/gramschmidt.elf", null, null),
    heat3d("org/specs/MicroBlaze/asm/PolyBenchMedium/heat-3d.elf", null, null),
    jacobi1d("org/specs/MicroBlaze/asm/PolyBenchMedium/jacobi-1d.elf", null, null),
    jacobi2d("org/specs/MicroBlaze/asm/PolyBenchMedium/jacobi-2d.elf", null, null),
    lu("org/specs/MicroBlaze/asm/PolyBenchMedium/lu.elf", null, null),
    ludcmp("org/specs/MicroBlaze/asm/PolyBenchMedium/ludcmp.elf", null, null),
    mvt("org/specs/MicroBlaze/asm/PolyBenchMedium/mvt.elf", null, null),
    nussinov("org/specs/MicroBlaze/asm/PolyBenchMedium/nussitov.elf", null, null),
    seidel2d("org/specs/MicroBlaze/asm/PolyBenchMedium/seidel-2d.elf", null, null),
    symm("org/specs/MicroBlaze/asm/PolyBenchMedium/symm.elf", null, null),
    syr2k("org/specs/MicroBlaze/asm/PolyBenchMedium/syr2k.elf", null, null),
    syrk("org/specs/MicroBlaze/asm/PolyBenchMedium/syrk.elf", null, null),
    trisolv("org/specs/MicroBlaze/asm/PolyBenchMedium/trisolv.elf", null, null),
    trmm("org/specs/MicroBlaze/asm/PolyBenchMedium/trmm.elf", null, null);

    // private String elfname;
    private String fullPath;
    private Number kernelStart;
    private Number kernelStop;

    private MicroBlazePolyBenchMedium(String fullPath, Number kernelStart, Number kernelStop) {
        // this.elfname = name();
        this.fullPath = fullPath;
        this.kernelStart = kernelStart;
        this.kernelStop = kernelStop;
    }

    @Override
    public String getResource() {
        return this.fullPath;
    }

    @Override
    public String asTxtDump() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Number getKernelStart() {
        return this.kernelStart;
    }

    @Override
    public Number getKernelStop() {
        return this.kernelStop;
    }

    @Override
    public String asTraceTxtDump() {
        return this.fullPath.replace(".elf", "_trace.txt");
    }
}
