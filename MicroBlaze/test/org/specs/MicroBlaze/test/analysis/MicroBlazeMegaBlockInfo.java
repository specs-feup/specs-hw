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

package org.specs.MicroBlaze.test.analysis;

import java.util.HashMap;
import java.util.Map;

import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniFloat;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;

public class MicroBlazeMegaBlockInfo {
    public static Map<ZippedELFProvider, Integer[]> getLivermoreN100Kernels() {
        Map<ZippedELFProvider, Integer[]> elfs = new HashMap<>();
        
        elfs.put(MicroBlazeLivermoreN100.cholesky, new Integer[] { 18 });
        elfs.put(MicroBlazeLivermoreN100.diffpredict, new Integer[] { 44, 45 });
        elfs.put(MicroBlazeLivermoreN100.hydro, new Integer[] { 14 });
        elfs.put(MicroBlazeLivermoreN100.hydro2d, new Integer[] {});
        elfs.put(MicroBlazeLivermoreN100.hydro2dimpl, new Integer[] {});
        elfs.put(MicroBlazeLivermoreN100.innerprod, new Integer[] { 10 });
        elfs.put(MicroBlazeLivermoreN100.intpredict, new Integer[] {});
        elfs.put(MicroBlazeLivermoreN100.linrec, new Integer[] { 10 });
        elfs.put(MicroBlazeLivermoreN100.matmul, new Integer[] { 15, 39, 54 });
        elfs.put(MicroBlazeLivermoreN100.pic1d, new Integer[] {});
        elfs.put(MicroBlazeLivermoreN100.pic2d, new Integer[] {});
        elfs.put(MicroBlazeLivermoreN100.statefrag, new Integer[] {});
        elfs.put(MicroBlazeLivermoreN100.tridiag, new Integer[] { 11 });
        
        return elfs;
    }

    public static Map<ZippedELFProvider, Integer[]> getPolybenchMiniFloatKernels() {
        Map<ZippedELFProvider, Integer[]> elfs = new HashMap<>();

        elfs.put(MicroBlazePolyBenchMiniFloat._2mm, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat._3mm, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.adi, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.atax, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.bicg, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.cholesky, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.correlation, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.covariance, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.deriche, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.doitgen, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.durbin, new Integer[] { });
        elfs.put(MicroBlazePolyBenchMiniFloat.fdtd2d, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.floydwarshall, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.gemm, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.gemver, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.gesummv, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.gramschmidt, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.heat3d, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.jacobi1d, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.jacobi2d, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.lu, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.ludcmp, new Integer[] { });
        elfs.put(MicroBlazePolyBenchMiniFloat.mvt, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.nussinov, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.seidel2d, new Integer[] {});
        elfs.put(MicroBlazePolyBenchMiniFloat.symm, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.syr2k, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.syrk, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.trisolv, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.trmm, new Integer[] { });

        return elfs;
    }
}
