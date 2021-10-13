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
 
package org.specs.Arm.provider;

public enum ArmPolyBenchMiniInt implements ArmZippedELFProvider {

    floydwarshall("floyd-warshall"),
    gemm,
    gemver,
    jacobi1d("jacobi-1d"),
    lu,
    ludcmp,
    mvt,
    nussinov,
    symm,
    syr2k,
    syrk,
    trisolv,
    trmm;

    private String functionName;
    private String elfName;

    private ArmPolyBenchMiniInt(String elfname) {
        this.functionName = "kernel_" + elfname.replace("-", "_");
        this.elfName = elfname + ".elf";
    }

    private ArmPolyBenchMiniInt() {
        this.functionName = "kernel_" + name();
        this.elfName = name() + ".elf";
    }

    @Override
    public String getELFName() {
        return this.elfName;
    }

    @Override
    public String getFunctionName() {
        return this.functionName;
    }
}
