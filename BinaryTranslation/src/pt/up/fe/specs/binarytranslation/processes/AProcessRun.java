package pt.up.fe.specs.binarytranslation.processes;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelProducer;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;

public abstract class AProcessRun implements ProcessRun, AutoCloseable {

    /*
     * 
     */
    private Process proc;
    private final ConcurrentChannel<String> stdout, stdin;
    private final ChannelConsumer<String> stdoutConsumer;
    private final ChannelProducer<String> stdinProducer;

    public AProcessRun(List<String> args) {
        this.stdout = new ConcurrentChannel<String>(1);
        this.stdoutConsumer = this.stdout.createConsumer();
        this.stdin = new ConcurrentChannel<String>(1);
        this.stdinProducer = this.stdin.createProducer();
        this.proc = BinaryTranslationUtils.newProcess(new ProcessBuilder(args));
        // StdioThreadUtils.attachThreads(this);
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

    /*
     * into process stdin
     */
    protected void send(String cmd) {
        this.stdinProducer.put(cmd);
    }

    /*
     * from process stdout
     */
    public String receive() {
        String ret = null;
        try {
            ret = this.stdoutConsumer.poll(10, TimeUnit.MILLISECONDS); // TODO: best?

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /*
     * 
     */
    @Override
    public void close() {

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
