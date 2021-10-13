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
 
package org.specs.MicroBlaze.provider;

public enum MicroBlazeGccOptimizationLevels implements MicroBlazeZippedELFProvider {

    autocor0("autocor-O0"),
    autocor1("autocor-O1"),
    autocor2("autocor-O2"),
    autocor2s("autocor-O2-small"),
    autocor3("autocor-O3"),
    autocorf("autocor-Ofast"),
    computeGradient0("computeGradient-O0"),
    dotprod0("dotprod-O0"),
    dotprod1("dotprod-O1"),
    dotprod2("dotprod-O2"),
    dotprod2s("dotprod-O2-small"),
    dotprod3("dotprod-O3"),
    dotprodf("dotprod-Ofast"),
    matmul0("matmul-O0"),
    test2("test-O2"),
    fir1("fir-O1"),
    fir2("fir-O2"),
    fir3("fir-O3"),
    firparam1("fir-param-O1"),
    firparam2("fir-param-O2");

    private String functionName;
    private String elfName;

    private MicroBlazeGccOptimizationLevels(String elfname) {
        this.functionName = "main"; // defaults to main since theres no explicit kernels (compiled with static??)
        this.elfName = elfname + ".elf";
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
