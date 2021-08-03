package org.specs.Riscv.stream;

import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.RiscvELFProvider;
import org.specs.Riscv.RiscvResource;
import org.specs.Riscv.instruction.RiscvInstruction;

import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;

public class RiscvDetailedTraceProducer extends DetailedRegisterInstructionProducer {

    public RiscvDetailedTraceProducer(RiscvELFProvider elfprovider) {
        super(new RiscvApplication(elfprovider),
                RiscvResource.RISC_TRACE_REGEX,
                RiscvInstruction::newInstance);
    }

}
