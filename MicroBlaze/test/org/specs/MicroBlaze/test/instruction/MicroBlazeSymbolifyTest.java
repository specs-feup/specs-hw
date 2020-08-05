package org.specs.MicroBlaze.test.instruction;

import org.junit.Test;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.test.instruction.SymbolifyTestUtils;

public class MicroBlazeSymbolifyTest {

    @Test
    public void test() {
        MicroBlazeInstruction i = MicroBlazeInstruction.newInstance("0", "3021ffe0"); // addik r1, r1, 0xffffffe0
        i.printInstruction();
        SymbolifyTestUtils.symbolifyTest(i);
    }
}
