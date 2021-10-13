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
 
package pt.up.fe.specs.binarytranslation.stream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;

public abstract class ATraceInstructionStream extends AInstructionStream implements TraceInstructionStream {

    /*
     * Output from GDB + QEMU Execution
     */
    protected ATraceInstructionStream(InstructionProducer traceProducer) {
        super(traceProducer);
    }

    @Override
    public boolean runUntil(long addr) {
        return this.getProducer().runUntil(addr);
    }

    @Override
    public boolean runUntil(String namedTarget) {
        return this.getProducer().runUntil(namedTarget);
    }

    @Override
    public void setCycleCounterBounds(Number startAddr, Number stopAddr) {
        this.boundStartAddr = startAddr.longValue();
        this.boundStopAddr = stopAddr.longValue();
    }

    @Override
    public Instruction nextInstruction() {

        var newinst = super.nextInstruction();
        if (this.numBoundCycles % 10000 == 0 && !this.isSilent()) {
            System.out.println(this.numBoundCycles + " bound cycles simulated... at addr 0x"
                    + Long.toHexString(newinst.getAddress()));
        }
        return newinst;
    }
}
