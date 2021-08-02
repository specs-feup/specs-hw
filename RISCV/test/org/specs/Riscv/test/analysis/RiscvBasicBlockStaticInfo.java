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

import org.specs.Riscv.RiscvPolyBenchSmallFloat;

import pt.up.fe.specs.binarytranslation.ELFProvider;

public class RiscvBasicBlockStaticInfo {
    public static Map<ELFProvider, Long[][]> getPolybenchSmallFloatKernels() {
        Map<ELFProvider, Long[][]> elfs = new HashMap<>();

        elfs.put(RiscvPolyBenchSmallFloat._2mm, new Long[][] { {0x80000028L, 0x80000030L } });
        elfs.put(RiscvPolyBenchSmallFloat._3mm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.adi, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.atax, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.bicg, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.covariance, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.trisolv, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.mvt, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.trmm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.doitgen, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.fdtd2d, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.gemm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.symm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.syrk, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.gesummv, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.symm, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.nussinov, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.syr2k, new Long[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.gemver, new Long[][] { { } });
        
        return elfs;
    }

}
