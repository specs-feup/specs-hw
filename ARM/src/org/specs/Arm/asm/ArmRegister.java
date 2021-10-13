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
 
package org.specs.Arm.asm;

import static pt.up.fe.specs.binarytranslation.instruction.register.RegisterType.HARDZERO;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.register.Register;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterType;

public enum ArmRegister implements Register {

    // GP bank
    R0("r0", HARDZERO);

    // TODO: all others!!

    private final String name;
    private final List<RegisterType> regTypes;

    private ArmRegister(String name, RegisterType... registerTypes) {
        this.name = name;
        this.regTypes = Arrays.asList(registerTypes);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<RegisterType> getRegTypes() {
        return regTypes;
    }
}
