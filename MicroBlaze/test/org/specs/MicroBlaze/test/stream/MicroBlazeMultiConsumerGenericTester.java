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
 
package org.specs.MicroBlaze.test.stream;

import java.util.ArrayList;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeStaticProducer;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.profiling.data.InstructionProfileResult;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.threadstream.ConsumerThread;
import pt.up.fe.specs.util.threadstream.ProducerEngine;

public class MicroBlazeMultiConsumerGenericTester {

    private void testParallel(MicroBlazeELFProvider elfprovider) {

        // producer
        var app = elfprovider.toApplication();
        var iproducer = new MicroBlazeStaticProducer(app);

        /*
         * TODO: the return of processing an instruction stream fully (post close) should be all meta info
         * contained in date, application info, etc
         * 
         * That way, no downstream object should need to query the istream for that data
         * 
         * Since the istream and the profilers/detectors are same-level hierarchy, the current approach
         * (with istream inside detectors/profilers) doesn't make sense anyway
         */

        // host for threads
        var streamengine = new ProducerEngine<Instruction, InstructionProducer>(iproducer, op -> op.nextInstruction(),
                cc -> new MicroBlazeElfStream(app, cc));

        // new consumer thread via lambda
        var threadlist = new ArrayList<ConsumerThread<Instruction, ?>>();
        for (int i = 0; i < 1; i++)
            threadlist.add(
                    streamengine.subscribe(os -> (new InstructionTypeHistogram()).profile((InstructionStream) os)));

        // launch all threads (blocking)
        streamengine.launch();

        // consume
        for (int i = 0; i < 1; i++) {
            var res = (InstructionProfileResult) threadlist.get(i).getConsumeResult();
            System.out.println(res.getJSONBytes());
        }
    }

    @Test
    public void testParallel() {
        this.testParallel((MicroBlazeLivermoreN10.cholesky));
    }
}
