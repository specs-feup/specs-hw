/**
 * Copyright 2023 SPeCS.
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

package pt.up.specs.cgra.structure.pes.alu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.specs.cgra.control.PEControl;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEDataNull;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.binary.BinaryProcessingElement;

public class ALUElement extends BinaryProcessingElement implements PEControl<ALUControlSetting> {

    /*
     * helper mapping of constructors to reduce verbosity
     */
    interface ALUOperation {
        PEData apply(PEData a, PEData b);
    }

    private static final Map<ALUControlSetting, ALUOperation> ALUOperations;
    static {
        Map<ALUControlSetting, ALUOperation> amap = new HashMap<ALUControlSetting, ALUOperation>();
        amap.put(ALUControlSetting.PASSNULL, (a, b) -> new PEDataNull());
        amap.put(ALUControlSetting.ADD, (a, b) -> a.add(b));
        amap.put(ALUControlSetting.SUB, (a, b) -> a.sub(b));
        amap.put(ALUControlSetting.MUL, (a, b) -> a.mul(b));
        amap.put(ALUControlSetting.DIV, (a, b) -> a.div(b));
        amap.put(ALUControlSetting.LSHIFT, (a, b) -> a.lshift(b));
        amap.put(ALUControlSetting.RSHIFT, (a, b) -> a.rshift(b));
        amap.put(ALUControlSetting.AND, (a, b) -> a.and(b));
        amap.put(ALUControlSetting.OR, (a, b) -> a.or(b));
        amap.put(ALUControlSetting.XOR, (a, b) -> a.xor(b));
        amap.put(ALUControlSetting.PASSL, (a, b) -> a.copy());
        amap.put(ALUControlSetting.PASSR, (a, b) -> b.copy());
        amap.put(ALUControlSetting.SLT, (a, b) -> a.slt(b));
        amap.put(ALUControlSetting.SEQ, (a, b) -> a.seq(b));
        amap.put(ALUControlSetting.XOR, (a, b) -> a.xor(b));

        // amap.put(ALUControlSetting.NORM1, (a, b) -> a.mul(a) + b.mul(b));
        // amap.put(ALUControl.MOD, (a, b) -> a.mod(b));
        ALUOperations = Collections.unmodifiableMap(amap);
    }

    private void debug(String str) {
        SpecsLogs.debug(ALUElement.class.getSimpleName() + ": " + str);
    }

    /*
     * At the next "_execute" step, this operation will be executed
     */
    private ALUControlSetting ctrl;

    public ALUElement(int latency, int memorySize) {
        super(latency, memorySize);
    }

    public ALUElement(int latency) {
        this(latency, 0);
    }

    public ALUElement() {
        this(1, 1);
    }

    @Override
    public void reset() {
        this.ctrl = ALUControlSetting.PASSNULL;
        super.reset();
    }

    @Override
    public boolean setOperation(ALUControlSetting ctrl) {

        if (!ALUOperations.containsKey(ctrl)) {
            this.debug("Set control failed: unknown control setting");
            return false;
        }

        this.ctrl = ctrl;
        this.debug("set control on " + this.getAt()
                + " for instruction " + this.getOperation().getName());
        return true;
    }

    /*
    @Override
    public boolean setControl(int ctrl) {
    
        PEControlSetting ectrl = null;
    
        for (var e : ALUControlSetting.values()) {
            ectrl = e;
            if (ctrl == ectrl.getValue()) {
                return this.setControl((ALUControlSetting) ectrl);
            }
        }
        return false;
    }*/

    @Override
    public ALUControlSetting getOperation() {
        return this.ctrl;
    }

    @Override
    protected boolean setReady() {
        if (this.ctrl == ALUControlSetting.PASSNULL) {
            this.debug(this.getAt() + "NOT ready for execution due to NULL control");
            return false;
        }
        return super.setReady();
    }

    @Override
    protected PEData _execute() {
        var x = ALUOperations.get(this.ctrl).apply(this.getOperand(0), this.getOperand(1));
        var opA = this.getOperand(0);
        var opB = this.getOperand(1);
        var op = this.getOperation().name();
        this.debug(this.getAt() + "executed successfuly operation" + opA + op + opB + " = " + x);
        return x;
    }

    @Override
    public ProcessingElement copy() {
        return new ALUElement(this.getLatency(), this.getMemorySize());
    }

    @Override
    protected ALUElement getThis() {
        return this;
    }

    @Override
    public String toString() {
        return "ALU";
    }
}
