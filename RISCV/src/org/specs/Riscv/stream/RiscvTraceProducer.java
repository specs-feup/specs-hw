package org.specs.Riscv.stream;

import java.io.File;

import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.RiscvResource;
import org.specs.Riscv.instruction.RiscvInstruction;

import pt.up.fe.specs.binarytranslation.producer.TraceInstructionProducer;

public class RiscvTraceProvider extends TraceInstructionProducer {

    public RiscvTraceProvider(File elfname) {
        super(new RiscvApplication(elfname),
                RiscvResource.RISC_TRACE_REGEX,
                RiscvInstruction::newInstance);
    }
}
