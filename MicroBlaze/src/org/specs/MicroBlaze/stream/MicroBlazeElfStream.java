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
 
package org.specs.MicroBlaze.stream;

import org.specs.MicroBlaze.asm.MicroBlazeApplication;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.ChanneledInstructionProducer;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class MicroBlazeElfStream extends AStaticInstructionStream {

    /*
     * Auxiliary constructor so that streams can be built from the threading engine
     * with the channels created by the parent InstructionProducer
     */
    public MicroBlazeElfStream(MicroBlazeApplication app, ChannelConsumer<Instruction> channel) {
        super(new ChanneledInstructionProducer(app, channel));
    }

    public MicroBlazeElfStream(MicroBlazeApplication app) {
        super(new MicroBlazeStaticProducer(app));
    }

    public MicroBlazeElfStream(MicroBlazeELFProvider elfprovider) {
        super(new MicroBlazeStaticProducer(elfprovider.toApplication()));
    }
}
