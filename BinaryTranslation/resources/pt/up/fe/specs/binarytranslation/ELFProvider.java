package pt.up.fe.specs.binarytranslation;

import java.io.File;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.providers.ResourceProvider;

public interface ELFProvider extends ResourceProvider {

    default public Application toApplication() {
        throw new NotImplementedException("toApplication()");
    }

    default public String getFunctionName() {
        return SpecsIo.removeExtension(this.getELFName());
    }

    default public StaticInstructionStream toStaticStream() {
        throw new NotImplementedException("toStaticStream()");
    }

    default public TraceInstructionStream toTraceStream() {
        throw new NotImplementedException("toTraceStream()");
    }

    public String getELFName();

    default File getFile() {
        return this.write();
    }
}
