package pt.up.fe.specs.binarytranslation.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.NullInstruction;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.utilities.LineStream;

public abstract class AInstructionStream implements InstructionStream {

    @Expose
    protected long numinsts;

    @Expose
    protected long numcycles;

    @Expose
    protected Application appInfo;

    private boolean isClosed = false;
    private final Process proc;
    protected final LineStream insts;
    private Instruction currentInstruction, nextInstruction;

    public AInstructionStream(ProcessBuilder builder) {
        this.proc = AInstructionStream.newStreamGenerator(builder);
        this.insts = AInstructionStream.newLineStream(proc);
        this.numinsts = 0;
        this.numcycles = 0;
        this.currentInstruction = null;
        this.nextInstruction = this.getnextInstruction();
    }

    private static Process newStreamGenerator(ProcessBuilder builder) {

        // start gdb
        Process proc = null;
        try {
            builder.directory(new File("."));
            builder.redirectErrorStream(true); // redirects stderr to stdout
            proc = builder.start();

        } catch (IOException e) {
            throw new RuntimeException("Could not run process bin with name: " + proc);
        }

        return proc;
    }

    private static LineStream newLineStream(Process proc) {

        /*
         * TODO: determine why ConcurrentChannel is required here
         */

        // No error detected, obtain LineStream
        LineStream insts = null;
        try {
            ConcurrentChannel<LineStream> lineStreamChannel = new ConcurrentChannel<>(1);
            InputStream inputStream = proc.getInputStream();
            lineStreamChannel.createProducer().offer(LineStream.newInstance(inputStream, "proc_stdout"));
            insts = lineStreamChannel.createConsumer().poll(1, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (insts == null) {
            throw new RuntimeException("Could not obtain output stream of stream generation process");
        }

        return insts;

        /*InputStream inputStream = proc.getInputStream();
        return LineStream.newInstance(inputStream, "proc_stdout");*/
    }

    @Override
    public void rawDump() {
        String line = null;
        while ((line = insts.nextLine()) != null) {
            System.out.print(line + "\n");
        }
    }

    @Override
    public boolean hasNext() {
        return this.nextInstruction != null;
    }

    @Override
    public long getNumInstructions() {
        return this.numinsts;
    }

    @Override
    public long getCycles() {
        return this.numcycles;
    }

    @Override
    public Application getApplicationInformation() {
        return appInfo;
    }

    /*
     * Skips input lines until the next one matches the regex
     * returns true if eventually theres a valid regex line, or false if stream is over
     */
    private boolean advanceLineToValid(LineStream insts, Pattern regex) {
        String line = null;
        while (((line = insts.peekNextLine()) != null) && !SpecsStrings.matches(line, regex)) {
            insts.nextLine();
        }

        return (line == null) ? false : true;
    }

    /*
     * Must be implemented by children
     */
    protected abstract Pattern getRegex();

    /*
     * Must be implemented by children
     */
    protected abstract Instruction newInstance(String address, String instruction);

    /*
     * May be override by children (i.e., {@MicroBlazeTraceStream})
     */
    protected Instruction getnextInstruction() {

        // skip until regex match
        if (!this.advanceLineToValid(this.insts, getRegex()))
            return null;

        var addressAndInst = SpecsStrings.getRegex(this.insts.nextLine(), getRegex());
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        var newinst = this.newInstance(addr, inst);
        return newinst;
    }

    @Override
    public Instruction nextInstruction() {

        if (nextInstruction == null) {
            return NullInstruction.NullInstance;
        }

        this.currentInstruction = this.nextInstruction;
        this.nextInstruction = this.getnextInstruction();
        this.numcycles += this.currentInstruction.getLatency();
        this.numinsts++;
        return this.currentInstruction;
    }

    @Override
    public void close() {

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        insts.close();
        this.isClosed = true;
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }
}
