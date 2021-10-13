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
 
package org.specs.MicroBlaze.instruction;

import pt.up.fe.specs.binarytranslation.instruction.operand.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.operand.AOperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandAccessType;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandDataSize;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandDataType;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandType;
import pt.up.fe.specs.binarytranslation.instruction.register.ExecutedImmediate;
import pt.up.fe.specs.binarytranslation.instruction.register.ExecutedRegister;

public class MicroBlazeOperand extends AOperand {

    private MicroBlazeOperand(OperandProperties props, ExecutedRegister readReg) {
        super(props, readReg);
    }

    /*
     * Copy constructor
     */
    private MicroBlazeOperand(MicroBlazeOperand other) {
        super(other);
    }

    /*
     * public copy method
     */
    @Override
    public MicroBlazeOperand copy() {
        return new MicroBlazeOperand(this);
    }

    public static MicroBlazeOperand newReadRegister(ExecutedRegister readReg) {
        var props = new AOperandProperties(readReg.getAsmField(), "r", "",
                OperandType.REGISTER, OperandAccessType.READ,
                OperandDataType.SCALAR_INTEGER, OperandDataSize.WORD);
        return new MicroBlazeOperand(props, readReg);
    }

    public static MicroBlazeOperand newWriteRegister(ExecutedRegister writeReg) {
        var props = new AOperandProperties(writeReg.getAsmField(), "r", "",
                OperandType.REGISTER, OperandAccessType.WRITE,
                OperandDataType.SCALAR_INTEGER, OperandDataSize.WORD);
        return new MicroBlazeOperand(props, writeReg);
    }

    public static MicroBlazeOperand newImmediate(ExecutedImmediate immVal) {
        var props = new AOperandProperties(immVal.getAsmField(), "0x", "",
                OperandType.IMMEDIATE, OperandAccessType.READ,
                OperandDataType.SCALAR_INTEGER, OperandDataSize.WORD);
        return new MicroBlazeOperand(props, immVal);
    }
}
