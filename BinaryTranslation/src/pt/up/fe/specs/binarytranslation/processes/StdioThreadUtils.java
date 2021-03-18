package pt.up.fe.specs.binarytranslation.processes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.Executors;

import pt.up.fe.specs.util.utilities.LineStream;

public class StdioThreadUtils {

    protected static void attachThreads(ProcessRun run) {

        StdioThreadUtils.attachIn(run);
        StdioThreadUtils.attachOut(run);
    }

    protected static void attachOut(ProcessRun run) {

        // stdout thread
        Executors.newSingleThreadExecutor()
                .execute(() -> StdioThreadUtils.stdoutThread(run));

        // stderr thread
        Executors.newSingleThreadExecutor()
                .execute(() -> StdioThreadUtils.stderrThread(run));
    }

    public static void attachIn(ProcessRun run) {

        // stdin thread
        Executors.newSingleThreadExecutor()
                .execute(() -> StdioThreadUtils.stdinThread(run));
    }

    private static void stdoutThread(ProcessRun run) {

        var lstream = LineStream.newInstance(run.getProc().getInputStream(), "gdb_stdout");
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

    private static void stderrThread(ProcessRun run) {

        var lstream = LineStream.newInstance(run.getProc().getErrorStream(), "gdb_stderr");
        while (lstream.hasNextLine()) {
            lstream.nextLine();
        }
        lstream.close();

        // thread end
        return;
    }

    private static void stdinThread(ProcessRun run) {

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
