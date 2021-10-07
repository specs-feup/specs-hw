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

import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniFloat;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;

public class MicroBlazeBasicBlockInfo {
    public static Map<ZippedELFProvider, Integer[]> getLivermoreKernels() {
        Map<ZippedELFProvider, Integer[]> elfs = Map.of(
                MicroBlazeLivermoreN10.innerprod, new Integer[] { 10 });
        return elfs;
    }

    public static Map<ZippedELFProvider, Integer[]> getPolybenchMiniFloatKernels() {
        Map<ZippedELFProvider, Integer[]> elfs = new HashMap<>();

        elfs.put(MicroBlazePolyBenchMiniFloat._2mm, new Integer[] { 12 });
        elfs.put(MicroBlazePolyBenchMiniFloat._3mm, new Integer[] { 11 });
        elfs.put(MicroBlazePolyBenchMiniFloat.adi, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.atax, new Integer[] { 5, 8, 9, 10, 11 });
        elfs.put(MicroBlazePolyBenchMiniFloat.bicg, new Integer[] { 5, 8, 17 });
        elfs.put(MicroBlazePolyBenchMiniFloat.cholesky, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.correlation, new Integer[] { 8 });
        elfs.put(MicroBlazePolyBenchMiniFloat.covariance, new Integer[] { 8 });
        elfs.put(MicroBlazePolyBenchMiniFloat.deriche, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.doitgen, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.durbin, new Integer[] { 7, 13 });
        elfs.put(MicroBlazePolyBenchMiniFloat.fdtd2d, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.floydwarshall, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.gemm, new Integer[] { 8, 12, 13 });
        elfs.put(MicroBlazePolyBenchMiniFloat.gemver, new Integer[] { 15 });
        elfs.put(MicroBlazePolyBenchMiniFloat.gesummv, new Integer[] { 16 });
        elfs.put(MicroBlazePolyBenchMiniFloat.gramschmidt, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.heat3d, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.jacobi1d, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.jacobi2d, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.lu, new Integer[] { 11 });
        elfs.put(MicroBlazePolyBenchMiniFloat.ludcmp, new Integer[] { 10 });
        elfs.put(MicroBlazePolyBenchMiniFloat.mvt, new Integer[] { 9 });
        elfs.put(MicroBlazePolyBenchMiniFloat.nussinov, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.seidel2d, new Integer[] {  });
        elfs.put(MicroBlazePolyBenchMiniFloat.symm, new Integer[] { 17 });
        elfs.put(MicroBlazePolyBenchMiniFloat.syr2k, new Integer[] { 7, 17, 18 });
        elfs.put(MicroBlazePolyBenchMiniFloat.syrk, new Integer[] { 7, 13, 14 });
        elfs.put(MicroBlazePolyBenchMiniFloat.trisolv, new Integer[] { 10 });
        elfs.put(MicroBlazePolyBenchMiniFloat.trmm, new Integer[] { 12 });

        return elfs;
    }
}
