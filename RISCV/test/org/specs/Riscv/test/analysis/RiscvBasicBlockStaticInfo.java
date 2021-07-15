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

import org.specs.BinaryTranslation.ELFProvider;
import org.specs.Riscv.RiscvPolyBenchSmallFloat;

public class RiscvBasicBlockStaticInfo {
    public static Map<ELFProvider, Integer[][]> getPolybenchSmallFloatKernels() {
        Map<ELFProvider, Integer[][]> elfs = new HashMap<>();

        elfs.put(RiscvPolyBenchSmallFloat._2mm, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat._3mm, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.adi, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.atax, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.bicg, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.covariance, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.trisolv, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.mvt, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.trmm, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.doitgen, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.fdtd2d, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.gemm, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.symm, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.syrk, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.gesummv, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.symm, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.nussinov, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.syr2k, new Integer[][] { { } });
        elfs.put(RiscvPolyBenchSmallFloat.gemver, new Integer[][] { { } });
        
        return elfs;
    }

}
