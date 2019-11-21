package org.specs.Arm.stream;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.specs.Arm.ArmResource;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.TraceInstructionStream;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.asm.processor.RegisterTable;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.system.OutputType;
import pt.up.fe.specs.util.system.StreamToString;
import pt.up.fe.specs.util.utilities.LineStream;
import pt.up.fe.specs.util.utilities.Replacer;

public class ArmTraceStream implements TraceInstructionStream {

    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
    private final LineStream insts;

    private static ArmTraceStream newInstance(File elfname) {

        ConcurrentChannel<LineStream> lineStreamChannel = new ConcurrentChannel<>(1);

        String elfpath = elfname.getAbsolutePath();
        var gdbScript = new Replacer(ArmResource.QEMU_AARCH64_GDB_TEMPLATE);
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<QEMUBIN>", "/usr/bin/qemu-system-aarch64");
        SpecsIo.write(new File("tmpscript.gdb"), gdbScript.toString());

        var executor = Executors.newSingleThreadScheduledExecutor();
        executor.execute(() -> ArmTraceStream.runSimulator(lineStreamChannel));
        executor.shutdown();

        LineStream insts = null;

        try {
            insts = lineStreamChannel.createConsumer().poll(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (insts == null) {
            throw new RuntimeException("Could not launch simulator, timed-out after 5 seconds");
        }

        return new ArmTraceStream(insts);
    }

    private ArmTraceStream(LineStream insts) {
        this.insts = insts;
    }

    public ArmTraceStream(File elfname) {
        this(newInstance(elfname).insts);
    }

    private static void runSimulator(ConcurrentChannel<LineStream> lineStreamChannel) {

        Function<InputStream, Boolean> stdout = inputStream -> {
            lineStreamChannel.createProducer().offer(LineStream.newInstance(inputStream, "gdb_stdout"));
            return true;
        };

        Function<InputStream, String> stderr = new StreamToString(false, true, OutputType.StdErr);

        try {
            SpecsSystem.runProcess(Arrays.asList("aarch64-none-elf-gdb2",
                    "-x", "tmpscript.gdb"), new File("."), stdout, stderr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rawDump() {
        String line = null;
        while ((line = insts.nextLine()) != null) {
            System.out.print(line + "\n");
        }
    }

    @Override
    public Instruction nextInstruction() {

        String line = null;
        while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, REGEX))
            ;

        if (line == null) {
            return null;
        }

        var addressAndInst = SpecsStrings.getRegex(line, REGEX);
        var addr = addressAndInst.get(0).trim();
        var inst = addressAndInst.get(1).trim();
        return ArmInstruction.newInstance(addr, inst);
    }

    @Override
    public void close() {
        insts.close();
    }

    @Override
    public Instruction nextTraceInstruction() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getNumInstructions() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public RegisterTable getRegisters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getCycles() {
        // TODO Auto-generated method stub
        return 0;
    }
}
