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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.specs.Arm.parsing.ArmAsmField;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandAccessType;
import pt.up.fe.specs.binarytranslation.instruction.operand.OperandDataSize;

/*
 * this class only exists to provide a shorthandle to the providers
 */
public class ArmOperandBuilder {

    private Map<ArmAsmField, Integer> map;

    /*
     * construct the builder
     */
    public ArmOperandBuilder(Map<ArmAsmField, Integer> map) {
        this.map = map;
    }

    /*
     * helper mapping of constructors to reduce verbosity
     */
    interface ArmOperandProvider {
        ArmOperand apply(ArmAsmField field, Number value, int width);
    }

    private static final Map<Integer, OperandDataSize> WIDTHS;
    static {
        // NIBBLE(4),
        // BYTE(8),
        // HALFWORD(16),
        // WORD(32),
        // DWORD(64),
        // QWORD(128);

        Map<Integer, OperandDataSize> widths = new HashMap<Integer, OperandDataSize>();
        widths.put(4, OperandDataSize.NIBBLE);
        widths.put(8, OperandDataSize.BYTE);
        widths.put(16, OperandDataSize.HALFWORD);
        widths.put(32, OperandDataSize.WORD);
        widths.put(64, OperandDataSize.DWORD);
        widths.put(128, OperandDataSize.QWORD);
        WIDTHS = Collections.unmodifiableMap(widths);
    }

    private static final Map<Integer, ArmOperandProvider> OPERANDPROVIDE;
    static {
        // Key:
        // 00 - ~SIMD, READ
        // 01 - ~SIMD, WRITE
        // 10 - SIMD, READ
        // 11 - SIMD, WRITE

        Map<Integer, ArmOperandProvider> amap = new HashMap<Integer, ArmOperandProvider>();
        amap.put(0b00, ArmOperand::newReadRegister);
        amap.put(0b01, ArmOperand::newWriteRegister);
        amap.put(0b10, ArmOperand::newSIMDReadRegister);
        amap.put(0b11, ArmOperand::newSIMDWriteRegister);
        OPERANDPROVIDE = Collections.unmodifiableMap(amap);
    }

    private static ArmOperandProvider getProvider(int key) {
        return OPERANDPROVIDE.get(key);
    }

    public ArmOperand newRegister(int key, ArmAsmField field, int wd) {
        return getProvider(key).apply(field, map.get(field), wd);
    }

    public ArmOperand newRegister(int key, ArmAsmField field, Number value, int wd) {
        return getProvider(key).apply(field, value, wd);
    }

    public ArmOperand newReadRegister(ArmAsmField field, int wd) {
        return getProvider(0b00).apply(field, map.get(field), wd);
    }

    public ArmOperand newReadRegister(ArmAsmField field, Number value, int wd) {
        return getProvider(0b00).apply(field, value, wd);
    }

    public ArmOperand newWriteRegister(ArmAsmField field, int wd) {
        return getProvider(0b01).apply(field, map.get(field), wd);
    }

    public ArmOperand newImmediate(ArmAsmField field, int wd) {
        return ArmOperand.newImmediate(field, map.get(field), wd);
    }

    public ArmOperand newImmediate(ArmAsmField field, Number value, int wd) {
        return ArmOperand.newImmediate(field, value, wd);
    }

    public ArmOperand newImmediateLabel(ArmAsmField field, int wd) {
        return ArmOperand.newImmediateLabel(field, map.get(field), wd);
    }

    public ArmOperand newImmediateLabel(ArmAsmField field, Number value, int wd) {
        return ArmOperand.newImmediateLabel(field, value, wd);
    }

    public Operand newSubOperation(ArmAsmField field, String value, int wd) {
        return ArmOperand.newSubOperation(field, value, wd);
    }

    public Operand newWritePSTATERegister() {
        return ArmOperand.newPSTATERegister(OperandAccessType.WRITE);
    }

    public Operand newReadPSTATERegister() {
        return ArmOperand.newPSTATERegister(OperandAccessType.READ);
    }
}
