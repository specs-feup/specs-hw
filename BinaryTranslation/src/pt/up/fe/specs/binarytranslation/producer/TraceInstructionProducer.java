package pt.up.fe.specs.binarytranslation.producer;

import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionInstantiator;
import pt.up.fe.specs.binarytranslation.processes.GDBRun;
import pt.up.fe.specs.binarytranslation.processes.StringProcessRun;
import pt.up.fe.specs.binarytranslation.processes.TxtDump;

public class TraceInstructionProducer extends AInstructionProducer {

    private final static Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    /*
     * Output from QEMU Execution
     */
    protected TraceInstructionProducer(Application app, InstructionInstantiator produceMethod) {
        super(app, TraceInstructionProducer.getProperProcess(app), REGEX, produceMethod);
    }

    /*
     * Determine process to use based on file extension and OS
     */
    private static StringProcessRun getProperProcess(Application app) {

        var name = app.getElffile().getName();
        var extension = name.subSequence(name.length() - 3, name.length());

        // Output from GNU based objdump
        if (extension.equals("elf"))
            return GDBRun.newInstanceInteractive(app);

        // txt trace dump
        else
            return new TxtDump(app.getElffile());
    }

    @Override
    public boolean runUntil(long addr) {
        if ((this.prun instanceof GDBRun)) {
            var gdb = (GDBRun) this.prun;
            gdb.runUntil(addr);
            return true;
        }
        return false;
    }

    @Override
    public boolean runUntil(String namedTarget) {
        if ((this.prun instanceof GDBRun)) {
            var gdb = (GDBRun) this.prun;
            gdb.runUntil(namedTarget);
            return true;
        }
        return false;
    }

    @Override
    public void rawDump() {
        if ((this.prun instanceof GDBRun)) {
            var gdb = (GDBRun) this.prun;
            gdb.runToEnd();
            super.rawDump();
            gdb.killTarget();
            gdb.quit();
        } else
            super.rawDump();
    }

    @Override
    public Instruction nextInstruction() {
        if ((this.prun instanceof GDBRun)) {
            var gdb = (GDBRun) this.prun;

            // gdb will hang if any run commads are issued
            // (until i find away top send a CTRLC-C)
            if (gdb.isExitReached())
                return null;

            // registers first
            var regs = gdb.getRegisters();

            // then inst
            gdb.stepi();
            var line = gdb.getAddrAndInstruction();
            if (line == null)
                return null;

            var splits = line.split(":");
            var newinst = this.newInstance(splits[0], splits[1], regs); // TODO: turn this "regs" into a HashMap ? so
                                                                        // that the specialized class can come up later?
                                                                        // .e.g MicroBlazeRegisterDump
            // newinst.setRegisters(regs);
            return newinst;

        } else {
            return super.nextInstruction();
        }
    }

    @Override
    public void close() {
        if ((this.prun instanceof GDBRun)) {
            var gdb = (GDBRun) this.prun;
            gdb.quit();
        }
        super.close();
    }
}
