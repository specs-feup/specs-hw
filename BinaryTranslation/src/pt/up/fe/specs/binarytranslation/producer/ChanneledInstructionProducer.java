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
 
package pt.up.fe.specs.binarytranslation.producer;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.threadstream.GenericObjectStream;

public class ChanneledInstructionProducer extends GenericObjectStream<Instruction> implements InstructionProducer {

    /*
     * Interesting as output, and should be queryable by over-arching BTF chain
     */
    @Expose
    private final Application app;

    public ChanneledInstructionProducer(Application app, ChannelConsumer<Instruction> consumer) {
        super(consumer, InstructionStream.NullInstruction.NullInstance);
        this.app = app;
    }

    @Override
    public Integer getInstructionWidth() {
        return this.app.getInstructionWidth();
    }

    @Override
    public Instruction nextInstruction() {
        return this.next();
    }

    @Override
    public void rawDump() {
        // TODO Auto-generated method stub
    }

    @Override
    public Application getApp() {
        return this.app;
    }
}
