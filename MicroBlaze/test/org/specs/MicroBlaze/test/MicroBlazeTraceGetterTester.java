package org.specs.MicroBlaze.test;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeTraceGetter;

public class MicroBlazeTraceGetterTester {

    @Test
    public void test() {
        MicroBlazeTraceGetter.getTrace("./resources/org/specs/MicroBlaze/asm/test/helloworld.elf");
    }

}
