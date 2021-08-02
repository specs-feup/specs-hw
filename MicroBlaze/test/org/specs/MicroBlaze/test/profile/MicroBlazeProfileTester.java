package org.specs.MicroBlaze.test.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProducer;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;
import pt.up.fe.specs.util.utilities.heapwindow.HeapWindow;

public class MicroBlazeProfileTester {

    /*
     * map providers to streams
     */
    private static final Map<Class<?>, Class<?>> AUXMAP = new HashMap<>();
    static {
        AUXMAP.put(MicroBlazeTraceProducer.class, MicroBlazeTraceStream.class);
    }

    @Test
    public void MicroBlazeProfile() {
        (new HeapWindow()).run();

        Class<?> producers[] = { /*MicroBlazeStaticProvider.class ,*/ MicroBlazeTraceProducer.class };

        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(InstructionTypeHistogram.class);
        profilerList.add(InstructionHistogram.class);

        for (var file : MicroBlazeLivermoreELFN100.values()) {
            // for (var file : Arrays.asList(MicroBlazeLivermoreELFN100.linrec100)) {

            for (var producer : producers) {

                var result = InstructionStreamProfilingUtils.profile(
                        MicroBlazeLivermoreELFN10.cholesky.getResource(), producer, AUXMAP.get(producer),
                        profilerList, file.getKernelStart(), file.getKernelStop());

                for (var r : result) {
                    r.toJSON();
                }
            }
            break;
        }
    }

    /*
     * just to count total cycles (Cause i didnt before)
     */
    @Test
    public void MicroBlazeSimulate() {

        for (var elf : MicroBlazeLivermoreELFN100.values()) {

            try (var istream = new MicroBlazeTraceStream(elf)) {

                // sim
                istream.silent(true);
                while ((istream.nextInstruction()) != null)
                    ;

                System.out.println(elf.getFilename() + ": " + istream.getNumInstructions());
            }
        }
    }
}
