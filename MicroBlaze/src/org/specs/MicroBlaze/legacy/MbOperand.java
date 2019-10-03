/*
 * Copyright 2010 SPeCS Research Group.
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

package org.specs.MicroBlaze.legacy;

import java.util.Optional;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Represents a MicroBlaze operand.
 *
 * <p>
 * Field "value" should represent the contents of the register.
 *
 * @author Joao Bispo
 */
public class MbOperand {
    /**
     * INSTANCE VARIABLES
     */
    private final Flow flow;
    private final Type type;
    private final int intValue;
    private final String id;
    private final MbRegister regId;
    private final short shortValue;

    public enum Flow {
        READ,
        WRITE;
    }

    public enum Type {
        REGISTER,
        IMMEDIATE;
    }

    /**
     * Constructor for registers.
     * 
     * @param register
     * @param flow
     * @param asmValue
     * @param registerName
     */
    private MbOperand(MbRegister register, Flow flow, short asmValue, String registerName) {
        this.flow = flow;
        this.type = Type.REGISTER;
        this.intValue = asmValue;
        this.id = registerName;
        this.regId = register;
        this.shortValue = asmValue;
    }

    /**
     * Constructor for immediates.
     * 
     * @param flow
     * @param type
     * @param value
     * @param id
     * @param regId
     */
    private MbOperand(short value, Flow flow) {
        this.flow = flow;
        this.type = Type.IMMEDIATE;
        this.intValue = value;
        this.id = MbRegisterUtils.getConstantName();
        this.regId = null;
        this.shortValue = value;
    }

    /**
     * Creates a new register operand.
     * 
     * @param flow
     * @param value
     * @param id
     * @param register
     * @return
     */
    public static MbOperand newRegister(MbRegister register, Flow flow) {

        // Get value
        short value = register.getAsmValue().shortValue();
        // Get id
        String id = register.getName();

        return new MbOperand(register, flow, value, id);
    }

    /**
     * Creates a new immediate operand.
     * 
     * @param value
     * @param flow
     * @return
     */
    public static MbOperand newImmediate(String value, Flow flow) {
        return new MbOperand(Short.parseShort(value), flow);
    }

    /**
     * 
     * @return the MbRegister if of type register, or null otherwise
     */
    public MbRegister getRegId() {
        return regId;
    }

    public Flow getFlow() {
        return flow;
    }

    public Type getType() {
        return type;
    }

    /**
     * If is of type immediate, returns the value of the constant. Otherwise, returns the asm value of the register.
     * 
     * @return
     */
    public int getIntValue() {
        return intValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public String getId() {
        return id;
    }

    public String getAsm() {
        if (type == Type.REGISTER) {
            return regId.getAsmRepresentation();
        }

        return Integer.toString(intValue);

    }

    @Override
    public String toString() {
        String prefix = "";

        if (type == Type.REGISTER) {
            return id;
        }

        return prefix + intValue;
    }

    public static Boolean isInput(Flow flow) {
        if (flow == Flow.READ) {
            return true;
        }

        if (flow == Flow.WRITE) {
            return false;
        }

        SpecsLogs.getLogger().warning("Case '" + flow + "' not defined");
        return null;
    }

    public static Boolean isConstant(Type type) {
        if (type == Type.IMMEDIATE) {
            return true;
        }

        if (type == Type.REGISTER) {
            return false;
        }

        SpecsLogs.getLogger().warning("Case '" + type + "' not defined");
        return null;
    }

    public boolean isR0() {
        if (getType() != MbOperand.Type.REGISTER) {
            return false;
        }

        if (!MbRegisterUtils.isGpr(regId)) {
            return false;
        }

        Integer gprNumber = MbRegisterUtils.getGprNumber(regId);
        if (!gprNumber.equals(0)) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param mbOperand
     * @return If given MbOperand is R0, returns an immediate operand with value 0 and same flow as given operand.
     *         Otherwise returns null
     */
    public static Optional<MbOperand> transformR0InImm0(MbOperand mbOperand) {
        // Check if is R0
        if (!mbOperand.isR0()) {
            return Optional.empty();
        }

        return Optional.of(new MbOperand((short) 0, mbOperand.flow));
    }

    public static MbOperand newCarryIn() {
        return new MbOperand(MbRegister.RMSR, Flow.READ, (short) -1, MbRegisterUtils.getCarryFlagName());
    }

    public static MbOperand newCarryOut() {
        return new MbOperand(MbRegister.RMSR, Flow.WRITE, (short) -1, MbRegisterUtils.getCarryFlagName());
    }

    /**
     * Warns if the check if not correct.
     *
     * @param type
     * @param flow
     * @return true if operand has the type and the flow given. False otherwise.
     */
    public boolean check(Type type, Flow flow) {
        if (getType() != type) {
            SpecsLogs.getLogger().warning("Operand '" + this + "' should have type '" + type + "'.");
            return false;
        }

        if (getFlow() != flow) {
            SpecsLogs.getLogger().warning("Operand '" + this + "' should have flow '" + flow + "'.");
            return false;
        }

        return true;
    }

}
