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

import org.specs.Arm.parsing.ArmAsmField;

import pt.up.fe.specs.binarytranslation.instruction.operand.AOperand;
import pt.up.fe.specs.binarytranslation.instruction.operand.AOperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandAccessType;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandDataSize;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandDataType;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandProperties;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandType;

public class ArmOperand extends AOperand {

    static int SPvalue = 31;

    /*
     * Integer value constructor
     */
    private ArmOperand(OperandProperties props, Number value) {
        super(props, value);
    }

    /*
     * String value constructor
     */
    private ArmOperand(OperandProperties props, String value) {
        super(props, value);
    }

    /*
     * 
     */
    private static ArmOperand newInstance(
            ArmAsmField field, String prefix, String suffix, Number value,
            OperandType opType, OperandAccessType opRnw,
            OperandDataType opDataType, OperandDataSize opDataSize) {

        var checkType = (value.intValue() == SPvalue) ? OperandType.SPECIAL : opType;
        var props = new AOperandProperties(field, prefix, suffix, checkType, opRnw, opDataType, opDataSize);
        if (checkType == OperandType.SPECIAL)
            return new ArmOperand(props, "sp");
        else
            return new ArmOperand(props, value);
    }

    private static String getPrefix(int width) {
        switch (width) {
        case 32:
            return "w";
        case 64:
            return "x";
        default:
            return "x";
        }
    }

    private static String getSIMDPrefix(int width) {
        switch (width) {
        case 8:
            return "b";
        case 16:
            return "h";
        case 32:
            return "s";
        case 64:
            return "d";
        case 128:
            return "q";
        default:
            return "s";
        }
    }

    /*
     * Read register
     */
    public static ArmOperand newReadRegister(ArmAsmField field, Number value, int width) {
        String prefix = (value.intValue() == SPvalue) ? "" : getPrefix(width);
        return newInstance(field, prefix, "", value,
                OperandType.REGISTER, OperandAccessType.READ,
                OperandDataType.SCALAR_INTEGER, AOperandProperties.resolveWidth(width));
    }

    /*
     * Write register
     */
    public static ArmOperand newWriteRegister(ArmAsmField field, Number value, int width) {
        String prefix = (value.intValue() == SPvalue) ? "" : getPrefix(width);
        return newInstance(field, prefix, "", value,
                OperandType.REGISTER, OperandAccessType.WRITE,
                OperandDataType.SCALAR_INTEGER, AOperandProperties.resolveWidth(width));
    }

    /*
     * Read simd register
     */
    public static ArmOperand newSIMDReadRegister(ArmAsmField field, Number value, int width) {
        String prefix = getSIMDPrefix(width);
        return newInstance(field, prefix, "", value,
                OperandType.REGISTER, OperandAccessType.READ,
                OperandDataType.SIMD_INTEGER, AOperandProperties.resolveWidth(width));
    }

    /*
     * Write simd register
     */
    public static ArmOperand newSIMDWriteRegister(ArmAsmField field, Number value, int width) {
        String prefix = getSIMDPrefix(width);
        return newInstance(field, prefix, "", value,
                OperandType.REGISTER, OperandAccessType.WRITE,
                OperandDataType.SIMD_INTEGER, AOperandProperties.resolveWidth(width));
    }

    /*
     * Immediate
     */
    public static ArmOperand newImmediate(ArmAsmField field, Number value, int width) {
        String prefix = (value instanceof Float) ? "" : "#0x";
        var datatype = (value instanceof Float) ? OperandDataType.SCALAR_FLOAT : OperandDataType.SCALAR_INTEGER;
        return newInstance(field, prefix, "", value,
                OperandType.IMMEDIATE, OperandAccessType.READ,
                datatype, AOperandProperties.resolveWidth(width));
    }

    /*
     * Immediate without prefix (used for labels?)
     */
    public static ArmOperand newImmediateLabel(ArmAsmField field, Number value, int width) {
        return newInstance(field, "", "", value,
                OperandType.IMMEDIATE, OperandAccessType.READ,
                OperandDataType.SCALAR_INTEGER, AOperandProperties.resolveWidth(width));
    }

    /*
     * *************************** SPECIAL TYPES *************************** TODO: fix later??
     */

    /*
     * Representational sub-operation operand for shifted (and extended?) register operations
     */
    public static ArmOperand newSubOperation(ArmAsmField field, String value, int width) {
        var props = new AOperandProperties(field, "", "",
                OperandType.SUBOPERATION, OperandAccessType.READ, // TODO: READ??
                OperandDataType.SCALAR_INTEGER, AOperandProperties.resolveWidth(width)); // TODO:
                                                                                         // OperandDataType.SCALAR_INTEGER
                                                                                         // ??
        return new ArmOperand(props, value);
    }

    /*
     * Special PSTATE Register (can only be an output)
     * (see C6.1.4 to check which instructions write to the PSTATE register)
     */
    public static ArmOperand newPSTATERegister(OperandAccessType rnw) {
        var props = new AOperandProperties(ArmAsmField.IMPLICIT, "[", "]",
                OperandType.SPECIAL, rnw,
                OperandDataType.BITFIELD, OperandDataSize.NIBBLE);
        return new ArmOperand(props, "nzvc");
    }

    /*
     * Copy "constructor"
     */
    @Override
    public ArmOperand copy() {
        var props = this.getProperties().copy();
        if (this.isSubOperation() || this.isSpecial())
            return new ArmOperand(props, this.getStringValue());
        else
            return new ArmOperand(props, this.getNumberValue());
    }
}
