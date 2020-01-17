package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.specs.MicroBlaze.MicroBlazeResource;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.Replacer;

public class MicroBlazeTraceStream extends ATraceInstructionStream {

    private static final String GDB_EXE = "mb-gdb";

    // private static final String QEMU_EXE = "qemu-system-microblazeel";
    private static final String QEMU_EXE = "/media/nuno/HDD/work/projects/myqemus/qemu-system-microblazeel";

    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
    private static final MicroBlazeResource QEMU_DTB = MicroBlazeResource.QEMU_MICROBLAZE_BAREMETAL_DTB;
    private static final MicroBlazeResource QEMU_GDB_TMPL = MicroBlazeResource.QEMU_MICROBLAZE_GDB_TEMPLATE;

    public MicroBlazeTraceStream(File elfname) {
        super(MicroBlazeTraceStream.newSimulatorBuilder(elfname));
    }

    /*
     * Need this method instead of the method in ATraceInstructionStream due to the need to specify DTB
     */
    private static ProcessBuilder newSimulatorBuilder(File elfname) {

        // copy dtb to local folder
        File fd = SpecsIo.resourceCopy(QEMU_DTB.getResource());
        fd.deleteOnExit();

        String elfpath = elfname.getAbsolutePath();
        var gdbScript = new Replacer(QEMU_GDB_TMPL);
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<QEMUBIN>", QEMU_EXE);
        gdbScript.replace("<DTBFILE>", fd.getAbsolutePath());
        SpecsIo.write(new File("tmpscript.gdb"), gdbScript.toString());

        return new ProcessBuilder(Arrays.asList(GDB_EXE, "-x", "tmpscript.gdb"));
    }

    @Override
    public Instruction nextInstruction() {

        var newinst = MicroBlazeInstructionStreamMethods.nextInstruction(this.insts, REGEX);
        if (newinst == null) {
            return null;
        }
        this.numcycles += newinst.getLatency();
        this.numinsts++;
        return newinst;
    }

    @Override
    public int getInstructionWidth() {
        return MicroBlazeInstructionStreamMethods.getInstructionWidth();
    }
}
