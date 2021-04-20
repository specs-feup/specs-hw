package pt.up.fe.specs.binarytranslation.processes;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;

public abstract class AProcessRun implements ProcessRun, AutoCloseable {

    /*
     * 
     */
    protected Process proc;
    protected final ConcurrentChannel<String> stdout, stdin;

    public AProcessRun(List<String> args) {
        this.stdout = new ConcurrentChannel<String>(1);
        this.stdin = new ConcurrentChannel<String>(1);
        this.proc = BinaryTranslationUtils.newProcess(new ProcessBuilder(args));
    }

    @Override
    public ConcurrentChannel<String> getStdin() {
        return this.stdin;
    }

    @Override
    public ConcurrentChannel<String> getStdout() {
        return this.stdout;
    }

    @Override
    public Process getProc() {
        return this.proc;
    }

    @Override
    public boolean isAlive() {
        return this.proc.isAlive();
    }

    /*
     * 
     */
    protected void attachThreads() {
        this.attachStdOut();
        this.attachStdErr();
        this.attachStdIn();
    }

    /*
     * 
     */
    protected void attachStdOut() {
        Executors.newSingleThreadExecutor()
                .execute(() -> StdioThreads.stdoutThread(this));
    }

    /*
     * 
     */
    protected void attachStdErr() {
        Executors.newSingleThreadExecutor()
                .execute(() -> StdioThreads.stderrThread(this));
    }

    /*
     * 
     */
    protected void attachStdIn() {
        Executors.newSingleThreadExecutor()
                .execute(() -> StdioThreads.stdinThread(this));
    }

    /*
     * 
     */
    @Override
    public void close() {

        try {
            if (!proc.waitFor(2, TimeUnit.SECONDS))
                proc.destroy();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
