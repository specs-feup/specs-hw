package org.specs.Riscv.stream;

import java.io.File;

import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.RiscvResource;
import org.specs.Riscv.instruction.RiscvInstruction;

import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;

public class RiscvTrace extends TraceInstructionProducer {

    public RiscvTrace(File elfname) {
        super(new RiscvApplication(elfname),
                RiscvResource.RISC_REGEX,
                RiscvInstruction::newInstance);
    }
}
