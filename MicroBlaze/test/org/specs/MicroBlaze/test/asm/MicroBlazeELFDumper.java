package org.specs.MicroBlaze.test.asm;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazePolyBenchSmallFloat;
import org.specs.MicroBlaze.MicroBlazePolyBenchSmallInt;
import org.specs.MicroBlaze.asm.MicroBlazeApplication;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;
import pt.up.fe.specs.binarytranslation.processes.ObjDump;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeELFDumper {

    private void dumpELFStatic(ELFProvider elf) {
        var fd = BinaryTranslationUtils.getFile(elf);
        var objdump = new ObjDump(new MicroBlazeApplication(fd));
        BinaryTranslationUtils.dumpProcessRunStdOut(objdump, elf.getResource().replace(".elf", "_objdump.txt"));
    }

    private void dumpELFTrace(ELFProvider elf) {
        var fd = BinaryTranslationUtils.getFile(elf);
        var app = new MicroBlazeApplication(fd);
        var gdb = GDBRun.newInstanceFreeRun(app);
        BinaryTranslationUtils.dumpProcessRunStdOut(gdb, elf.getResource().replace(".elf", "_trace.txt"));
    }

    @Test
    public void dumpELFstoFiles() {
        // for (var elf : Arrays.asList(MicroBlazeLivermoreELFN10.cholesky)) {
        for (var elf : MicroBlazePolyBenchSmallInt.values()) {
            dumpELFStatic(elf);
            dumpELFTrace(elf);
        }

        for (var elf : MicroBlazePolyBenchSmallFloat.values()) {
            dumpELFStatic(elf);
            dumpELFTrace(elf);
        }
    }

}
