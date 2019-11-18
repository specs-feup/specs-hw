package org.specs.MicroBlaze.test;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeTraceStreamTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("./resources/org/specs/MicroBlaze/asm/test/test.elf");
        fd.deleteOnExit();

        try (MicroBlazeTraceStream el = new MicroBlazeTraceStream(fd)) {

        }
    }

}
