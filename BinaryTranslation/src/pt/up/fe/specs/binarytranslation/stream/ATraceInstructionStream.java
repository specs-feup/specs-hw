package pt.up.fe.specs.binarytranslation.stream;

import java.io.File;
import java.util.Arrays;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

public abstract class ATraceInstructionStream extends AInstructionStream {

    public static ProcessBuilder newSimulatorBuilder(File elfname, ResourceProvider gdbtmpl,
            String gdbexe, String qemuexe) {

        String elfpath = elfname.getAbsolutePath();
        var gdbScript = new Replacer(gdbtmpl);
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<QEMUBIN>", qemuexe);
        SpecsIo.write(new File("tmpscript.gdb"), gdbScript.toString());

        return new ProcessBuilder(Arrays.asList(gdbexe, "-x", "tmpscript.gdb"));
    }

    /*
     * used by arm
     */
    protected ATraceInstructionStream(File elfname, ResourceProvider gdbtmpl,
            String gdbexe, String qemuexe) {
        super(ATraceInstructionStream.newSimulatorBuilder(elfname, gdbtmpl, gdbexe, qemuexe));
        this.appName = elfname.getName();
    }

    /*
     * Constructor is only used by microblaze for now
     */
    protected ATraceInstructionStream(ProcessBuilder pb) {
        super(pb);
    }

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.TRACE;
    }
}
