package org.specs.Riscv.provider;

import org.specs.Riscv.RiscvApplication;
import org.specs.Riscv.stream.RiscvElfStream;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructioStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;

public interface RiscvELFProvider extends ZippedELFProvider {

    final static String PREFIX = "org/specs/Riscv/asm/";

    @Override
    default RiscvELFProvider asTraceTxtDump() {
        return new RiscvTraceDumpProvider(this);
    }

    @Override
    default RiscvELFProvider asTxtDump() {
        return new RiscvObjDumpProvider(this);
    }

    @Override
    default Application toApplication() {
        return new RiscvApplication(this);
    }

    @Override
    default StaticInstructioStream toStaticStream() {
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
