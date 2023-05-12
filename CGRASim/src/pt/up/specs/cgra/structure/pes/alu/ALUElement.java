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
import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.dataypes.PEData;
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
        amap.put(ALUControlSetting.PASSNULL, (a, b) -> a.passNull(b));
        amap.put(ALUControlSetting.ADD, (a, b) -> a.add(b));
        amap.put(ALUControlSetting.SUB, (a, b) -> a.sub(b));
        amap.put(ALUControlSetting.MUL, (a, b) -> a.mul(b));
        amap.put(ALUControlSetting.DIV, (a, b) -> a.div(b));
        amap.put(ALUControlSetting.LSHIFT, (a, b) -> a.lshift(b));
        amap.put(ALUControlSetting.RSHIFT, (a, b) -> a.rshift(b));
        amap.put(ALUControlSetting.AND, (a, b) -> a.and(b));
        amap.put(ALUControlSetting.OR, (a, b) -> a.or(b));
        amap.put(ALUControlSetting.XOR, (a, b) -> a.xor(b));
        amap.put(ALUControlSetting.PASSL, (a, b) -> a.passl(b));
        amap.put(ALUControlSetting.PASSR, (a, b) -> a.passr(b));
        amap.put(ALUControlSetting.SLT, (a, b) -> a.slt(b));
        amap.put(ALUControlSetting.SEQ, (a, b) -> a.seq(b));

        // amap.put(ALUControlSetting.NORM1, (a, b) -> a.mul(a) + b.mul(b));
        // amap.put(ALUControl.MOD, (a, b) -> a.mod(b));
        // amap.put(ALUControl.XOR, (a, b) -> a.xor(b));

        ALUOperations = Collections.unmodifiableMap(amap);
    }

    private static void debug(String str) {
        SpecsLogs.debug(ALUElement.class.getSimpleName() + ": " + str);
    }

    /*
     * At the next "_execute" step, this operation will be executed
     */
    // private ALUControlSetting ctrl;

    public ALUElement(int latency, int memorySize) {
        super(latency, memorySize);
    }

    public ALUElement(int latency) {
        this(latency, 0);
    }

    public ALUElement() {
        this(1, 1);
    }

    public boolean setControl(ALUControlSetting ctrl) {

        if (!ALUOperations.containsKey(ctrl)) {
            System.out.println("set control failed: unknown control setting");
            return false;
        }

        this.ctrl = ctrl;

        System.out.printf("set control on PE %d %d for instruction: %s \n", this.getX(), this.getY(),
                this.getControl().getName());

        return true;
    }

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
    }

    @Override
    public boolean setOperation(ALUControlSetting ctrl) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ALUControlSetting getControl() {
        if (this.ctrl != null)
            return (ALUControlSetting) this.ctrl;
        else
            return null;
    }

    @Override
    protected PEData _execute() {
        var x = ALUOperations.get(this.ctrl).apply(this.getOperand(0), this.getOperand(1));

        System.out.printf("ALU %d %d executed successfuly operation %d %s %d yielding result %d\n",
                this.getX(), this.getY(), this.getOperand(0).getValue().intValue(), this.getControl().name(),
                this.getOperand(1).getValue().intValue(), x.getValue().intValue());

        return x;

    }

    @Override
    public ProcessingElement copy() {
        return new ALUElement(this.getLatency(), this.getMemorySize());
    }

    @Override
    public String toString() {
        return "ALU";
    }
}
