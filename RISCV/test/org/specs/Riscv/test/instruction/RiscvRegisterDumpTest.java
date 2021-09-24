package org.specs.Riscv.test.instruction;

import org.junit.Test;
import org.specs.Riscv.instruction.RiscvRegisterDump;
import org.specs.Riscv.provider.RiscvLivermoreN100imaf;

import pt.up.fe.specs.binarytranslation.test.instruction.RegisterDumpTest;

public class RiscvRegisterDumpTest extends RegisterDumpTest {

    private static String gdbResponse = "ra             0x80000068    <main+64>\n"
            + "        sp             0x87fff600   \n"
            + "        gp             0x80008150   <__malloc_av_+560>\n"
            + "        tp             0x0  \n"
            + "        t0             0x6000   24576\n"
            + "        t1             0x0  0\n"
            + "        t2             0x0  0\n"
            + "        fp             0x88000000   \n"
            + "        s1             0x87fff600   -2013268480\n"
            + "        a0             0x87fffcec   -2013266708\n"
            + "        a1             0x80007a40   -2147452352\n"
            + "        a2             0x180    384\n"
            + "        a3             0x0  0\n"
            + "        a4             0x87ffdff0   -2013274128\n"
            + "        a5             0x1  1\n"
            + "        a6             0x0  0\n"
            + "        a7             0x0  0\n"
            + "        s2             0x0  0\n"
            + "        s3             0x0  0\n"
            + "        s4             0x0  0\n"
            + "        s5             0x0  0\n"
            + "        s6             0x0  0\n"
            + "        s7             0x0  0\n"
            + "        s8             0x0  0\n"
            + "        s9             0x0  0\n"
            + "        s10            0x0  0\n"
            + "        s11            0x0  0\n"
            + "        t3             0x0  0\n"
            + "        t4             0x0  0\n"
            + "        t5             0x0  0\n"
            + "        t6             0x0  0\n"
            + "        pc             0x800028f8   <mmemcpy>\n"
            + "        mstatush       0x0  0\"";

    /**
     * Construction of a @RiscvRegisterDump from the raw gdbresponse string. This process is hidden within the
     * "nextInstruction()" flow of the @TraceInstructionStream (if this instance contains a @TraceInstructionProducer
     * which is a @GDBRun). The @ARegisterDump class initializes a map where all values of the registers for the given
     * ISA can be queried using the list of register definitions e.g., @RiscvRegister
     */
    @Test
    public void RiscvTestDump() {
        TestRegisterDump(new RiscvRegisterDump(gdbResponse));
    }

    @Test
    public void auxGetGDBResponse() {
        GetGDBResponse(RiscvLivermoreN100imaf.innerprod);
    }
}
