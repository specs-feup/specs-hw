package pt.up.fe.specs.binarytranslation.stream;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.asm.processor.RegisterTable;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.system.OutputType;
import pt.up.fe.specs.util.system.StreamToString;
import pt.up.fe.specs.util.utilities.LineStream;
import pt.up.fe.specs.util.utilities.Replacer;

public abstract class ATraceInstructionStream implements TraceInstructionStream {

    protected final LineStream insts;

    protected ATraceInstructionStream(File elfname, ResourceProvider gdbtmpl,
            String gdbexe, String qemuexe) {

        ConcurrentChannel<LineStream> lineStreamChannel = new ConcurrentChannel<>(1);

        String elfpath = elfname.getAbsolutePath();
        var gdbScript = new Replacer(gdbtmpl);
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<QEMUBIN>", "/usr/bin/" + qemuexe);
        SpecsIo.write(new File("tmpscript.gdb"), gdbScript.toString());

        var futureSimulatorError = SpecsSystem
                .getFuture(() -> ATraceInstructionStream.runSimulator(lineStreamChannel, gdbexe));

        // Wait for a second, to check if there is an exception when launching the simulator
        var error = SpecsSystem.get(futureSimulatorError, 1, TimeUnit.SECONDS);

        if (error != null) {
            throw new RuntimeException(
                    "Could not properly launch the simulator", error);
        }

        // No error detected, obtain LineStream
        LineStream insts = null;
        try {
            insts = lineStreamChannel.createConsumer().poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (insts == null) {
            throw new RuntimeException("Could not obtain output stream of GDB");
        }

        this.insts = insts;
    }

    private static Exception runSimulator(ConcurrentChannel<LineStream> lineStreamChannel, String gdbexe) {

        Function<InputStream, Boolean> stdout = inputStream -> {
            lineStreamChannel.createProducer().offer(LineStream.newInstance(inputStream, "gdb_stdout"));
            return true;
        };

        Function<InputStream, String> stderr = new StreamToString(false, true, OutputType.StdErr);

        try {
            var command = Arrays.asList(gdbexe, "-x", "tmpscript.gdb");
            var output = SpecsSystem.runProcess(command, new File("."), stdout, stderr);

            // Throw an exception of any error is present
            var err = output.getStdErr();
            if (!err.isEmpty()) {
                return new RuntimeException("Problems while running GDB:\n" + err);
            }

            return null;

        } catch (Exception e) {
            return e;
        }
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
        insts.close();
    }
}
