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

package org.specs.Riscv.test.analysis;

import java.util.HashMap;
import java.util.Map;

import org.specs.Riscv.provider.RiscvPolyBenchMiniFloat;

import pt.up.fe.specs.binarytranslation.elf.ELFProvider;

public class RiscvBasicBlockStaticInfo {
    public static Map<ELFProvider, Long[][]> getPolybenchSmallFloatKernels() {
        Map<ELFProvider, Long[][]> elfs = new HashMap<>();

        elfs.put(RiscvPolyBenchMiniFloat._2mm, new Long[][] { {0x80000028L, 0x80000030L } });
        elfs.put(RiscvPolyBenchMiniFloat._3mm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.adi, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.atax, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.bicg, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.covariance, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.trisolv, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.mvt, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.trmm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.doitgen, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.fdtd2d, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.gemm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.symm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.syrk, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.gesummv, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.symm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.nussinov, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.syr2k, new Long[][] { { } });
        elfs.put(RiscvPolyBenchMiniFloat.gemver, new Long[][] { { } });
        
        return elfs;
    }

}
