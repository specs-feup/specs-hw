package org.specs.Riscv.test.instruction;

import org.junit.Test;
import org.specs.Riscv.instruction.RiscvInstruction;

public class RiscvInstructionParseTester {

    @Test
    public void testRiscvParse() {

        var i = RiscvInstruction.newInstance("000", "00670733");
        i.printInstruction();
    }

}
