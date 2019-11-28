package pt.up.fe.specs.binarytranslation.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.utilities.LineStream;

public abstract class AInstructionStream implements InstructionStream {

    protected Process proc;
    protected long numinsts;
    protected long numcycles;
    protected final LineStream insts;

    protected static Process newStreamGenerator(ProcessBuilder builder) {

        // start gdb
        Process proc = null;
        try {
            builder.directory(new File("."));
            builder.redirectErrorStream(true); // redirects stderr to stdout
            proc = builder.start();

        } catch (IOException e) {
            throw new RuntimeException("Could not run process bin with name: " + objdump);
        }

        return proc;
    }

    protected static LineStream newLineStream(Process proc) {

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
            throw new RuntimeException("Could not obtain output stream of GDB");
        }

        return insts;
    }

    public void rawDump() {
        String line = null;
        while ((line = insts.nextLine()) != null) {
            System.out.print(line + "\n");
        }
    }

    @Override
    public boolean hasNext() {
        return this.insts.hasNextLine();
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
    public void close() {

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        insts.close();
    }
}
