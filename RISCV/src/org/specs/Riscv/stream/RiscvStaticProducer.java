package org.specs.Riscv.stream;

import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.RiscvResource;
import org.specs.Riscv.instruction.RiscvInstruction;
import org.specs.Riscv.provider.RiscvELFProvider;

import pt.up.fe.specs.binarytranslation.producer.StaticInstructionProducer;

public class RiscvStaticProducer extends StaticInstructionProducer {

    public RiscvStaticProducer(RiscvELFProvider elfprovider) {
        super(new RiscvApplication(elfprovider),
                RiscvResource.RISC_DUMP_REGEX,
                RiscvInstruction::newInstance);
    }
}
