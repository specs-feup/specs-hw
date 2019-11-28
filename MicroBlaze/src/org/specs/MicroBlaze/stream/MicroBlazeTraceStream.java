package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.Replacer;

public class MicroBlazeTraceStream extends ATraceInstructionStream {

    private static final String GDB_EXE = "mb-gdb";

    // private static final String QEMU_EXE = "qemu-system-microblazeel";
    private static final String QEMU_EXE = "/media/nuno/HDD/work/projects/qemu/microblazeel-softmmu/qemu-system-microblazeel";

    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
    private static final MicroBlazeResource QEMU_DTB = MicroBlazeResource.QEMU_MICROBLAZE_BAREMETAL_DTB;
    private static final MicroBlazeResource QEMU_GDB_TMPL = MicroBlazeResource.QEMU_MICROBLAZE_GDB_TEMPLATE;

    // two auxiliary vars to help with mb-gdb bug
    private final Map<Integer, Instruction> elfdump = new HashMap<Integer, Instruction>();
    private Instruction afterimm = null;
    private boolean haveStoredInst = false;

    public MicroBlazeTraceStream(File elfname) {
        super(MicroBlazeTraceStream.newSimulatorBuilder(elfname));

        // Workaround for mb-gdb bug of stepping over
        // two instructions at once when it hits an "imm"
        // 1. open the ELF
        // 2. dump all the ELF into a local list
        var elf = new MicroBlazeElfStream(elfname);
        while (elf.hasNext()) {
            var i = elf.nextInstruction();
            elfdump.put(i.getAddress().intValue(), i);
        }
        elf.close();
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

        Instruction i = null;

        if (haveStoredInst == true) {
            i = afterimm;
            haveStoredInst = false;
        }

        else {
            String line = null;
            while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, REGEX))
                ;

            if (line == null) {
                return null;
            }

            var addressAndInst = SpecsStrings.getRegex(line, REGEX);
            var addr = addressAndInst.get(0).trim();
            var inst = addressAndInst.get(1).trim();
            i = MicroBlazeInstruction.newInstance(addr, inst);

            // get another one if true
            if (i.isImmediateValue()) {
                afterimm = elfdump.get(i.getAddress().intValue() + this.getInstructionWidth());
                haveStoredInst = true;
            }
        }

        this.numcycles += i.getLatency();
        this.numinsts++;
        return i;
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }
}
