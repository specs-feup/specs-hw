/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.specs.MicroBlaze.test.analysis;

import java.util.HashMap;
import java.util.Map;

import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniFloat;

import pt.up.fe.specs.binarytranslation.ELFProvider;

public class MicroBlazeBasicBlockInfo {
    public static Map<ELFProvider, Integer[]> getLivermoreKernels() {
        Map<ELFProvider, Integer[]> elfs = Map.of(
                MicroBlazeLivermoreELFN10.innerprod, new Integer[] { 10 });
        return elfs;
    }

    public static Map<ELFProvider, Integer[]> getPolybenchSmallFloatKernels() {
        Map<ELFProvider, Integer[]> elfs = new HashMap<>();

        elfs.put(MicroBlazePolyBenchMiniFloat._2mm, new Integer[] { 12, 13 });
        elfs.put(MicroBlazePolyBenchMiniFloat._3mm, new Integer[] { 11, 12, 13 });
        elfs.put(MicroBlazePolyBenchMiniFloat.adi, new Integer[] { 8, 13, 25, 27 });
        elfs.put(MicroBlazePolyBenchMiniFloat.atax, new Integer[] { 5, 9, 11 });
        elfs.put(MicroBlazePolyBenchMiniFloat.bicg, new Integer[] { 5, 12, 17 });
        elfs.put(MicroBlazePolyBenchMiniFloat.covariance, new Integer[] { 8, 9 });
        elfs.put(MicroBlazePolyBenchMiniFloat.trisolv, new Integer[] { 10, 11 });
        elfs.put(MicroBlazePolyBenchMiniFloat.mvt, new Integer[] { 9, 11, 12 });
        elfs.put(MicroBlazePolyBenchMiniFloat.trmm, new Integer[] { 12 });
        elfs.put(MicroBlazePolyBenchMiniFloat.doitgen, new Integer[] { 12 });
        elfs.put(MicroBlazePolyBenchMiniFloat.fdtd2d, new Integer[] { 19 });
        elfs.put(MicroBlazePolyBenchMiniFloat.gemm, new Integer[] { 8, 13 });
        elfs.put(MicroBlazePolyBenchMiniFloat.symm, new Integer[] { 18 });
        elfs.put(MicroBlazePolyBenchMiniFloat.syrk, new Integer[] { 7, 14 });
        elfs.put(MicroBlazePolyBenchMiniFloat.gesummv, new Integer[] { 16 });
        elfs.put(MicroBlazePolyBenchMiniFloat.symm, new Integer[] { 18 });
        elfs.put(MicroBlazePolyBenchMiniFloat.nussinov, new Integer[] { 6, 8 });
        elfs.put(MicroBlazePolyBenchMiniFloat.syr2k, new Integer[] { 7, 20 });
        elfs.put(MicroBlazePolyBenchMiniFloat.gemver, new Integer[] { 8, 10, 12, 15 });


//      elfs.put(MicroBlazePolyBenchSmallFloat.durbin, new Integer[] { 6, 10, 13, 14 }); // Needs bug fix enabled
//        elfs.put(MicroBlazePolyBenchSmallFloat.floydwarshall, new Integer[] {});
//        elfs.put(MicroBlazePolyBenchSmallFloat.jacobi2d, new Integer[] { 17 }); // HEAP ERROR
//        elfs.put(MicroBlazePolyBenchSmallFloat.heat3d, new Integer[] { 13 }); // HEAP ERROR
//        elfs.put(MicroBlazePolyBenchSmallFloat.lu, new Integer[] { 5 });
//        elfs.put(MicroBlazePolyBenchSmallFloat.seidel2d, new Integer[] { 10 });

//        // ERROR
//        elfs.put(MicroBlazePolyBenchSmallFloat.cholesky, new Integer[] {});
//        elfs.put(MicroBlazePolyBenchSmallFloat.correlation, new Integer[] {});
//        elfs.put(MicroBlazePolyBenchSmallFloat.deriche, new Integer[] {});
//        elfs.put(MicroBlazePolyBenchSmallFloat.ludcmp, new Integer[] {});
//        elfs.put(MicroBlazePolyBenchSmallFloat.gramschmidt, new Integer[] {});
//        elfs.put(MicroBlazePolyBenchSmallFloat.jacobi1d, new Integer[] {});

        return elfs;
    }
}
