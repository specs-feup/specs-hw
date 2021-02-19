package org.specs.Riscv.test.analysis;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Test;
import org.specs.Riscv.stream.RiscvTraceStream;
import org.specs.Riscv.stream.detailed.RiscvDetailedTraceProvider;
import org.specs.Riscv.stream.detailed.RiscvDetailedTraceStream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;
import pt.up.fe.specs.binarytranslation.test.stream.InstructionStreamTestUtils;
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
    
    @Test
    public void testRegisters() throws IOException {
        // extensionless name of the elf to run
        String elfname = "example";
        // true to print trace to std; false to print to a txt file instead
        boolean std = true;
        
        String path = "org/specs/Riscv/asm/" + elfname + ".elf";
        File fd = SpecsIo.resourceCopy(path);
        fd.deleteOnExit();
        
        RiscvDetailedTraceProvider test = new RiscvDetailedTraceProvider(fd);

        for (int i = 0; i < 100; i++) {
            RegisterDump rd = test.nextRegister();
            Instruction inst = test.nextInstruction();
            rd.prettyPrint();
            System.out.println("\n");
            inst.printInstruction();
            System.out.println("\n");
        }
    }
}
