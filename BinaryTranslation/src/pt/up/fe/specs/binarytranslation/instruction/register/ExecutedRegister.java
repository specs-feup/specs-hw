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
 
package pt.up.fe.specs.binarytranslation.instruction.register;

import pt.up.fe.specs.binarytranslation.asm.parsing.AsmField;

public class ExecutedRegister {

    private final AsmField asmField;
    private final Register registerDefinition;
    private final Number dataValue;

    public ExecutedRegister(AsmField asmField, Register regdef, Number dataValue) {
        this.asmField = asmField;
        this.registerDefinition = regdef;
        this.dataValue = dataValue;
    }

    public AsmField getAsmField() {
        return asmField;
    }

    public String getName() {
        return this.registerDefinition.getName();
    }

    public Number getDataValue() {
        return dataValue;
    }

    public Register getRegisterDefinition() {
        return registerDefinition;
    }
}
