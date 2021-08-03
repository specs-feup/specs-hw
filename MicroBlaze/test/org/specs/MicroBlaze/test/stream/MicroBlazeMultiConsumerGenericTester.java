package org.specs.MicroBlaze.test.stream;

import java.util.ArrayList;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;
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
        var iproducer = new MicroBlazeStaticProducer(elfprovider);

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
                cc -> new MicroBlazeElfStream(elfprovider, cc));

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
        this.testParallel((MicroBlazeLivermoreELFN10.cholesky));
    }
}
