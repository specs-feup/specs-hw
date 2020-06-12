package org.specs.Arm.instruction;

import java.util.*;

import org.specs.Arm.parsing.ArmAsmField;

import pt.up.fe.specs.binarytranslation.instruction.*;
import pt.up.fe.specs.binarytranslation.instruction.operand.*;

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
        return ArmOperand.newPSTATERegister(OperandType.WRITE);
    }

    public Operand newReadPSTATERegister() {
        return ArmOperand.newPSTATERegister(OperandType.READ);
    }
}
