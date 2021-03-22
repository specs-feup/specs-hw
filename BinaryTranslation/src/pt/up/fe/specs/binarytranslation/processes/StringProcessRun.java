package pt.up.fe.specs.binarytranslation.processes;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelProducer;

public class StringProcessRun extends AProcessRun {

    private final ChannelConsumer<String> stdoutConsumer;
    private final ChannelProducer<String> stdinProducer;

    public StringProcessRun(List<String> args) {
        super(args);
        this.stdoutConsumer = this.stdout.createConsumer();
        this.stdinProducer = this.stdin.createProducer();
    }

    /*
     * into process stdin
     */
    public void send(String cmd) {
        this.stdinProducer.put(cmd);
    }

    /*
     * from process stdout
     */
    public String receive() {
        return this.receive(1);
    }

    /*
     * 
     */
    public String receive(int seconds) {
        String ret = null;
        try {
            // indefinite wait
            if (seconds == -1)
                ret = this.stdoutConsumer.take(); // TODO: best?
            else
                ret = this.stdoutConsumer.poll(seconds, TimeUnit.SECONDS); // TODO: best?
            // when launching QEMU under GDB, the "target remote" command
            // takes longer than 10ms to complete, hence the 1s timeout for
            // all reads from stdout of the process
            // This shouldn't introduce delays in any other circumstances
            // since poll returns immediately

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
