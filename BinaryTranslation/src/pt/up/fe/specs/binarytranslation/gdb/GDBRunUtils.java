package pt.up.fe.specs.binarytranslation.gdb;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.Executors;

import pt.up.fe.specs.util.utilities.LineStream;

public class GDBRunUtils {

    protected static void attachThreads(GDBRun run) {

        // stdout thread
        Executors.newSingleThreadExecutor()
                .execute(() -> GDBRunUtils.stdoutThread(run));

        // stderr thread
        Executors.newSingleThreadExecutor()
                .execute(() -> GDBRunUtils.stderrThread(run));

        // stdin thread
        Executors.newSingleThreadExecutor()
                .execute(() -> GDBRunUtils.stdinThread(run));

        // discard header
        run.consumeAllGDBResponse();
        return;
    }

    private static void stdoutThread(GDBRun run) {

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

    private static void stderrThread(GDBRun run) {

        var lstream = LineStream.newInstance(run.getProc().getErrorStream(), "gdb_stderr");
        while (lstream.hasNextLine()) {
            lstream.nextLine();
        }
        lstream.close();

        // thread end
        return;
    }

    private static void stdinThread(GDBRun run) {

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
