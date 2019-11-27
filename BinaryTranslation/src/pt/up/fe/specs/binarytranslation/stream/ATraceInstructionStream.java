package pt.up.fe.specs.binarytranslation.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.asm.processor.RegisterTable;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.LineStream;
import pt.up.fe.specs.util.utilities.Replacer;

public abstract class ATraceInstructionStream implements TraceInstructionStream {

    private final Process gdb;
    protected final LineStream insts;

    protected static Process newSimulator(File elfname, ResourceProvider gdbtmpl,
            String gdbexe, String qemuexe) {

        String elfpath = elfname.getAbsolutePath();
        var gdbScript = new Replacer(gdbtmpl);
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<QEMUBIN>", "/usr/bin/" + qemuexe);
        SpecsIo.write(new File("tmpscript.gdb"), gdbScript.toString());

        // start gdb
        Process gdb = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(Arrays.asList(gdbexe, "-x", "tmpscript.gdb"));
            builder.directory(new File("."));
            gdb = builder.start();

        } catch (IOException e) {
            throw new RuntimeException("Could not run GDB bin with name: " + gdbexe);
        }

        return gdb;
    }

    protected static LineStream newLineStream(Process gdb) {

        // No error detected, obtain LineStream
        LineStream insts = null;
        try {
            ConcurrentChannel<LineStream> lineStreamChannel = new ConcurrentChannel<>(1);
            InputStream inputStream = gdb.getInputStream();
            lineStreamChannel.createProducer().offer(LineStream.newInstance(inputStream, "gdb_stdout"));
            insts = lineStreamChannel.createConsumer().poll(1, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (insts == null) {
            throw new RuntimeException("Could not obtain output stream of GDB");
        }

        return insts;
    }

    protected ATraceInstructionStream(File elfname, ResourceProvider gdbtmpl,
            String gdbexe, String qemuexe) {
        this.gdb = ATraceInstructionStream.newSimulator(elfname, gdbtmpl, gdbexe, qemuexe);
        this.insts = ATraceInstructionStream.newLineStream(gdb);
    }

    public void rawDump() {
        String line = null;
        while ((line = insts.nextLine()) != null) {
            System.out.print(line + "\n");
        }
    }

    public long getNumInstructions() {
        // TODO Auto-generated method stub
        return 0;
    }

    public RegisterTable getRegisters() {
        // TODO Auto-generated method stub
        return null;
    }

    public long getCycles() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void close() {

        // wait for gdb to die before closing everything
        try {
            gdb.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        insts.close();
    }
}
