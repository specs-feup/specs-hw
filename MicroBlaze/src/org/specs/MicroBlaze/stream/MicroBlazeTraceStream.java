package org.specs.MicroBlaze.stream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class MicroBlazeTraceStream implements InstructionStream {

    private BufferedReader gdbout;
    private BufferedWriter gdbin;
    private Process gdb;
    private Process qemu;

    public MicroBlazeTraceStream(File elfname) {

        // 1. make the .gdbinit file
        try {
            PrintWriter gdbinit = new PrintWriter(".gdbinit");
            gdbinit.println("target remote localhost:1234");
            gdbinit.println("set logging on");
            gdbinit.println("set logging file " + elfname.getName() + ".log");
            gdbinit.println("set height 0");
            gdbinit.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. launch processes
        try {
            // convert elf to bin
            var objcpypb = new ProcessBuilder(
                    Arrays.asList("mb-objcopy", "-O", "binary", elfname.getAbsolutePath(), "test.bin"));
            var objcopy = objcpypb.start();
            try {
                objcopy.waitFor();
                objcopy.destroy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // launch QEMU for Microblaze
            var qemupb = new ProcessBuilder(Arrays.asList("qemu-system-microblaze",
                    "-machine petalogix-ml605", "-m 256", "-nographic", "-s", "-S", "-kernel", "test.bin"));
            qemu = qemupb.start();

            // launch gdb
            var gdbpb = new ProcessBuilder(Arrays.asList("mb-gdb"));
            gdb = gdbpb.start();
            gdbout = new BufferedReader((new InputStreamReader(gdb.getInputStream())));
            gdbin = new BufferedWriter((new OutputStreamWriter(gdb.getOutputStream())));

            // read all starting garbage from gdb
            while (!gdbout.ready())
                ;

            while (gdbout.ready())
                System.out.print(gdbout.readLine() + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Instruction nextInstruction() {

        // instruct to step and disassemble
        try {
            gdbin.write("disas/r $pc, $pc+1");
            gdbin.flush();

            gdbin.write("stepi");
            gdbin.flush();

            /*
            var ln1 = gdbout.readLine();
            var ln2 = gdbout.readLine();
            var ln3 = gdbout.readLine();
            
            System.out.print(ln1);
            System.out.print(ln2);
            System.out.print(ln3);
            */

            // read all starting garbage from gdb
            while (!gdbout.ready())
                ;

            while (gdbout.ready())
                System.out.print(gdbout.readLine() + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.TRACE;
    }

    @Override
    public void close() {
        qemu.destroy();
        gdb.destroy();

        try {
            gdbin.close();
            gdbout.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
    }
}
