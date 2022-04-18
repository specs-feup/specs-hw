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

package pt.up.fe.specs.binarytranslation.asm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Deprecated
public abstract class AIsaRegisterConventions implements RegisterProperties {
    protected List<String> registers;
    protected List<String> parameters;
    protected List<String> temporaries;
    protected List<String> returnVals;
    protected String stackPointer;
    protected String zero;

    protected void setAllRegisters(String... regs) {
        registers = Collections.unmodifiableList(Arrays.asList(regs));
    }

    protected void setStackPointer(String reg) {
        stackPointer = reg;
    }

    protected void setZero(String reg) {
        zero = reg;
    }

    protected void setParameters(String... regs) {
        parameters = Collections.unmodifiableList(Arrays.asList(regs));
    }

    protected void setTemporaries(String... regs) {
        temporaries = Collections.unmodifiableList(Arrays.asList(regs));
    }

    protected void setReturnVals(String... regs) {
        returnVals = Collections.unmodifiableList(Arrays.asList(regs));
    }

    @Override
    public List<String> getAllRegisters() {
        return registers;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<String> getTemporaries() {
        return temporaries;
    }

    public List<String> getReturnVals() {
        return returnVals;
    }

    public String getStackPointer() {
        return stackPointer;
    }

    public String getZero() {
        return zero;
    }

    @Override
    public boolean isStackPointer(String reg) {
        return stackPointer.equals(reg);
    }

    @Override
    public boolean isTemporary(String reg) {
        return temporaries.contains(reg);
    }

    @Override
    public boolean isParameter(String reg) {
        return parameters.contains(reg);
    }

    @Override
    public boolean isZero(String reg) {
        return zero.equals(reg);
    }

    @Override
    public boolean isReturn(String reg) {
        return returnVals.equals(reg);
    }
}
