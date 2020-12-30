package org.specs.MicroBlaze.test.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeProfileTester {

    /*
     * map providers to streams
     */
    private static final Map<Class<?>, Class<?>> AUXMAP = new HashMap<>();
    static {
        AUXMAP.put(MicroBlazeTraceProvider.class, MicroBlazeTraceStream.class);
    }

    @Test
    public void MicroBlazeProfile() {

        Class<?> producers[] = { /*MicroBlazeStaticProvider.class ,*/ MicroBlazeTraceProvider.class };

        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(InstructionTypeHistogram.class);
        profilerList.add(InstructionHistogram.class);

        for (var file : MicroBlazeLivermoreELFN100.values()) {
            // for (var file : Arrays.asList(MicroBlazeLivermoreELFN100.linrec100)) {
            for (var producer : producers) {

                var result = InstructionStreamProfilingUtils.profile(
                        file.getResource(), producer, AUXMAP.get(producer),
                        profilerList, file.getKernelStart(), file.getKernelStop());

                for (var r : result) {
                    r.toJSON();
                }
            }
        }
    }

    /*
     * just to count total cycles (Cause i didnt before)
     */
    @Test
    public void MicroBlazeSimulate() {

        for (var file : MicroBlazeLivermoreELFN100.values()) {

            File fd = SpecsIo.resourceCopy(file.getResource());
            fd.deleteOnExit();
            try (var istream = new MicroBlazeTraceStream(fd)) {

                // sim
                istream.silent(true);
                while ((istream.nextInstruction()) != null)
                    ;

                System.out.println(file.getFilename() + ": " + istream.getNumInstructions());
            }
        }
    }
}
