package org.specs.Riscv.stream;

import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.RiscvResource;
import org.specs.Riscv.instruction.RiscvInstruction;
import org.specs.Riscv.provider.RiscvELFProvider;

import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;

public class RiscvTraceProducer extends TraceInstructionProducer {

    public RiscvTraceProducer(RiscvELFProvider elfprovider) {
        super(new RiscvApplication(elfprovider),
                RiscvResource.RISC_TRACE_REGEX,
                RiscvInstruction::newInstance);
    }
}
