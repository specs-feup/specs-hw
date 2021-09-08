package org.specs.Riscv.provider;

import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.stream.RiscvElfStream;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public interface RiscvELFProvider extends ELFProvider {

    final static String PREFIX = "org/specs/Riscv/asm/";

    @Override
    default RiscvApplication toApplication() {
        return new RiscvApplication(this);
    }

    @Override
    default StaticInstructionStream toStaticStream() {
        return new RiscvElfStream(this);
    }

    @Override
    default TraceInstructionStream toTraceStream() {
        return new RiscvTraceStream(this);
    }

    @Override
    default public String getResource() {
        return PREFIX + this.getClass().getSimpleName() + ".7z";
    }
}
