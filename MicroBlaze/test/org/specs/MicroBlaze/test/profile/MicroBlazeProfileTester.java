package org.specs.MicroBlaze.test.profile;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamProfilingUtils;

public class MicroBlazeProfileTester {

    private String[] FILES = {
            "org/specs/MicroBlaze/asm/cholesky.txt",
            "org/specs/MicroBlaze/asm/diffpredict.txt",
            "org/specs/MicroBlaze/asm/glinearrec.txt",
            "org/specs/MicroBlaze/asm/hydro.txt",
            "org/specs/MicroBlaze/asm/hydro2d.txt",
            "org/specs/MicroBlaze/asm/innerprod.txt",
            "org/specs/MicroBlaze/asm/matmul.txt"
    };

    @Test
    public void MicroBlazeProfile() {

        Class<?> streams[] = { MicroBlazeElfStream.class /*, MicroBlazeTraceStream.class*/ };
        Class<?> profilers[] = { InstructionTypeHistogram.class, InstructionHistogram.class };

        for (var file : FILES) {
            for (var stream : streams) {
                for (var profile : profilers) {
                    var result = InstructionStreamProfilingUtils.profile(file, stream, profile);
                    result.toJSON();
                }
            }
        }
    }
}
