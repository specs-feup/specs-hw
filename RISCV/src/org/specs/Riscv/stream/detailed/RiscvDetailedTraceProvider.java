package org.specs.Riscv.stream.detailed;

import java.io.File;
import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.RiscvResource;
import org.specs.Riscv.instruction.RiscvInstruction;

import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;

public class RiscvDetailedTraceProvider extends DetailedRegisterInstructionProducer {

    public RiscvDetailedTraceProvider(File elfname) {
        super(new RiscvApplication(elfname),
                RiscvResource.RISC_TRACE_REGEX,
                RiscvInstruction::newInstance);
    }

}
