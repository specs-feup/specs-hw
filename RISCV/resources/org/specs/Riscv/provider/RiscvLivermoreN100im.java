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
 
package org.specs.Riscv.provider;

public enum RiscvLivermoreN100im implements RiscvZippedELFProvider {

    /**
     * This set uses floating point numbers, but is compiled without FPU
     */

    cholesky(),
    diffpredict("difference_predictors"),
    // glinearrec("glinearrec.elf", 0x2dac, 0x5698), // linrecurrence? (there are
    // 2 linear
    // recurrence kernels)
    hydro("hydro_fragment"),
    hydro2d("hydro_2d"),
    hydro2dimpl("hydro_2d_implicit"),
    innerprod("inner_product"),
    intpredict("integrate_predictors"),
    linrec("lin_recurrence"),
    matmul(),
    pic1d("pic_1d"),
    pic2d("pic_2d"),
    statefrag("state_fragment"),
    tridiag("tri_diagonal");

    private String functionName;
    private String elfName;

    private RiscvLivermoreN100im(String functionName) {
        this.functionName = functionName;
        this.elfName = name() + ".elf";
    }

    private RiscvLivermoreN100im() {
        this.functionName = name();
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
