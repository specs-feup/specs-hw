package org.specs.Riscv.test.analysis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import org.specs.Riscv.stream.detailed.RiscvDetailedTraceProvider;
import org.specs.traceanalysis.TraceAnalysis;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;
import pt.up.fe.specs.util.SpecsIo;

public class RiscvTraceAnalysisTest {
//    @Test
//    public void testTraceToFile() throws IOException {
//        // extensionless name of the elf to run
//        String elfname = "example";
//        // true to print trace to std; false to print to a txt file instead
//        boolean std = true;
//        
//        String path = "resources/org/specs/Riscv/traces/" + elfname + ".txt";
//        if (!std)
//            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(path)), true));
//        InstructionStreamTestUtils.printStream(
//                "org/specs/Riscv/asm/" + elfname + ".elf", RiscvTraceStream.class);
//        System.out.println("OK!");
//    }
    
    private DetailedRegisterInstructionProducer getProducer(String elfname) {
        String path = "org/specs/Riscv/asm/" + elfname + ".elf";
        File fd = SpecsIo.resourceCopy(path);
        fd.deleteOnExit();
        
        DetailedRegisterInstructionProducer tr = new RiscvDetailedTraceProvider(fd);
        return tr;
    }
    
    @Test
    public void testRegisters() throws IOException {
        DetailedRegisterInstructionProducer tr = getProducer("example");

        RegisterDump rd = tr.nextRegister();
        Instruction inst = tr.nextInstruction();
        rd.prettyPrint();
        System.out.println("\n");
        inst.printInstruction();
        System.out.println("\n");
        assertFalse(rd.isEmpty());
    }
    
    @Test
    public void testRegistersInInst() throws IOException {
        DetailedRegisterInstructionProducer tr = getProducer("example");

        Instruction inst = tr.nextInstruction();
        assertFalse(inst.getRegisters().isEmpty());
    }
    
    @Test
    public void testAnalyzer() {
        DetailedRegisterInstructionProducer tr = getProducer("example");
        
        TraceAnalysis ta = new TraceAnalysis(tr);
        ta.analyze();
    }
}
