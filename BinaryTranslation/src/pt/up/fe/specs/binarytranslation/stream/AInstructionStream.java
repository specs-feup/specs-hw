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

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;
import pt.up.fe.specs.util.threadstream.AObjectStream;

public abstract class AInstructionStream extends AObjectStream<Instruction> implements InstructionStream {

    @Expose
    protected long numinsts;

    @Expose
    protected long numcycles;

    // TODO: add support for non continguous bounds (i.e., list)
    @Expose
    protected long boundStartAddr = -1;

    @Expose
    protected long boundStopAddr = -1;

    @Expose
    protected long numBoundCycles = 0;

    @Expose
    protected long numBoundInsts = 0;

    /*
     * This type of stream contains its own producer internally
     */
    private InstructionProducer producer;

    /*
     * 
     */
    private boolean silent = true;

    public AInstructionStream(InstructionProducer producer) {
        super(NullInstruction.NullInstance);
        this.producer = producer;
        this.numinsts = 0;
        this.numcycles = 0;
    }

    protected InstructionProducer getProducer() {
        return producer;
    }

    @Override
    public void silent(boolean isSilent) {
        this.silent = isSilent;
    }

    protected boolean isSilent() {
        return silent;
    }

    @Override
    public void rawDump() {
        this.producer.rawDump();
    }

    @Override
    public long getNumInstructions() {
        return this.numinsts;
    }

    @Override
    public long getCycles() {
        return this.numcycles;
    }

    @Override
    protected Instruction consumeFromProvider() {
        var next = this.producer.nextInstruction();
        if (next == null)
            return NullInstruction.NullInstance;
        else
            return next;
    }

    protected void counterIncreases(Instruction inst) {

        this.numcycles += inst.getLatency();
        this.numinsts++;

        var instaddr = inst.getAddress().longValue();
        if (this.boundStartAddr != -1 && this.boundStopAddr != -1) {
            if (instaddr >= this.boundStartAddr && instaddr <= this.boundStopAddr) {
                this.numBoundCycles += inst.getLatency();
                this.numBoundInsts++;
            }
        }
    }

    @Override
    public Instruction nextInstruction() {
        var inst = this.next();
        if (inst == null)
            return null;

        this.counterIncreases(inst);
        return inst;
    }

    @Override
    public final Application getApp() {
        return this.producer.getApp();
    }

    @Override
    public final Integer getInstructionWidth() {
        return this.getApp().getInstructionWidth();
    }

    @Override
    public void close() {
        try {
            this.producer.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
