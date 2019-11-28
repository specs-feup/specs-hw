package pt.up.fe.specs.binarytranslation.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.utilities.LineStream;

public abstract class AStaticInstructionStream extends AInstructionStream {

    private final Process objdump;
    protected long numinsts;
    protected long numcycles;
    protected final LineStream insts;

    protected static Process newTraceDump(File elfname, String objdumpexe) {

        // start gdb
        Process objdump = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(Arrays.asList(objdumpexe, "-d", elfname.getAbsolutePath()));
            builder.directory(new File("."));
            builder.redirectErrorStream(true); // redirects stderr to stdout
            objdump = builder.start();

        } catch (IOException e) {
            throw new RuntimeException("Could not run objdump bin with name: " + objdump);
        }

        return objdump;
    }

    protected static LineStream newLineStream(Process objdump) {

        // No error detected, obtain LineStream
        LineStream insts = null;
        try {
            ConcurrentChannel<LineStream> lineStreamChannel = new ConcurrentChannel<>(1);
            InputStream inputStream = objdump.getInputStream();
            lineStreamChannel.createProducer().offer(LineStream.newInstance(inputStream, "objdump_stdout"));
            insts = lineStreamChannel.createConsumer().poll(1, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (insts == null) {
            throw new RuntimeException("Could not obtain output stream of GDB");
        }

        return insts;
    }

    protected AStaticInstructionStream(File elfname, String objdumpbexe) {
        this.objdump = AStaticInstructionStream.newTraceDump(elfname, objdumpbexe);
        this.insts = AStaticInstructionStream.newLineStream(objdump);
        this.numinsts = 0;
        this.numcycles = 0;
    }

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.STATIC_ELF;
    }
}
