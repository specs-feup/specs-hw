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
 
package org.specs.Arm.instruction;

import java.util.Arrays;

import org.specs.Arm.asm.ArmRegister;

import pt.up.fe.specs.binarytranslation.instruction.register.ARegisterDump;
import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;

public class ArmRegisterDump extends ARegisterDump {

    public ArmRegisterDump(String rawRegisterDump) {
        super(ARegisterDump.parseRegisters(Arrays.asList(ArmRegister.values()), rawRegisterDump));
    }

    /*
     * Private copy constructor
     */
    private ArmRegisterDump(ArmRegisterDump other) {
        super(other);
    }

    @Override
    public RegisterDump copy() {
        return new ArmRegisterDump(this);
    }
}