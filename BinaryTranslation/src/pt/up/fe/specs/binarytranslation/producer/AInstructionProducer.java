package pt.up.fe.specs.binarytranslation.producer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.LineStream;

public abstract class AInstructionProducer implements InstructionProducer {

    /*
     * Interesting as output, and should be queryable by over-arching BTF chain
     */
    @Expose
    private final Application app;

    /*
     * Internal status
     */
    private final Process proc;
    private final LineStream insts;

    /*
     * Init by chilren
     */
    private final Pattern regex;
    private final BiFunction<String, String, Instruction> produceMethod;

    public AInstructionProducer(Application app, ProcessBuilder builder, ResourceProvider regex,
            BiFunction<String, String, Instruction> produceMethod) {
        this.app = app;
        this.proc = AInstructionProducer.newProcess(builder);
        this.insts = AInstructionProducer.newLineStream(this.proc);
        this.regex = Pattern.compile(regex.getResource());
        this.produceMethod = produceMethod;
    }

    @Override
    public Application getApp() {
        return app;
    }

    @Override
    public void rawDump() {
        String line = null;
        while ((line = insts.nextLine()) != null) {
            System.out.print(line + "\n");
        }
    }

    private static Process newProcess(ProcessBuilder builder) {

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

        // No error detected, obtain LineStream via a concurrentchannel to allow for a
        // small wait for the exe to generate the stdout
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
     * Initialized by non-abstract children methods
     */
    private Instruction newInstance(String address, String instruction) {
        return this.produceMethod.apply(address, instruction);
    }

    /*
     * Initialized by non-abstract children methods
     */
    private Pattern getRegex() {
        return this.regex;
    }

    @Override
    public Instruction nextInstruction() {

        // skip until regex match
        if (!this.advanceLineToValid(this.insts, getRegex())) {
            return null;
        }

        var addressAndInst = SpecsStrings.getRegex(this.insts.nextLine(), getRegex());
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        var newinst = this.newInstance(addr, inst);
        return newinst;
    }

    @Override
    public void close() {

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}