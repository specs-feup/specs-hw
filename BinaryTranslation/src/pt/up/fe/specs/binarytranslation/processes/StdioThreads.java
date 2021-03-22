package pt.up.fe.specs.binarytranslation.processes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.utilities.LineStream;

public class StdioThreads {

    private static LineStream newLineStream(Process proc, String name) {

        // No error detected, obtain LineStream via a concurrentchannel to allow for a
        // small wait for the exe to generate the stdout
        LineStream insts = null;
        try {
            var lineStreamChannel = new ConcurrentChannel<LineStream>(1);
            var inputStream = proc.getInputStream();
            lineStreamChannel.createProducer().offer(LineStream.newInstance(inputStream, name));
            insts = lineStreamChannel.createConsumer().poll(1, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (insts == null) {
            throw new RuntimeException("Could not obtain output stream of stream generation process");
        }

        return insts;
    }

    protected static void stdoutThread(ProcessRun run) {

        // var lstream = LineStream.newInstance(run.getProc().getInputStream(), "gdb_stdout");
        var lstream = StdioThreads.newLineStream(run.getProc(), "proc_stdout");
        var producer = run.getStdout().createProducer();

        // this thread will block here if "nextLine" is waiting for content
        // of if main thread has not read the concurrentchannel for the
        // previous stdout line
        while (lstream.hasNextLine()) {
            producer.put(lstream.peekNextLine());
            lstream.nextLine();
        }

        lstream.close();

        // thread end
        return;
    }

    protected static void stderrThread(ProcessRun run) {

        // var lstream = LineStream.newInstance(run.getProc().getErrorStream(), "proc_stderr");
        var lstream = StdioThreads.newLineStream(run.getProc(), "proc_stderror");
        while (lstream.hasNextLine()) {
            lstream.nextLine();
        }
        lstream.close();

        // thread end
        return;
    }

    protected static void stdinThread(ProcessRun run) {

        var bw = new BufferedWriter(new OutputStreamWriter(run.getProc().getOutputStream()));
        var consumer = run.getStdin().createConsumer();
        try {
            while (run.getProc().isAlive()) {
                bw.write(consumer.take());
                bw.newLine();
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // thread end
        return;
    }
}
