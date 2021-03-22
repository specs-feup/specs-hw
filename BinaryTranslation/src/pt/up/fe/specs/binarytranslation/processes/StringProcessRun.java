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
        return this.receive(10);
    }

    /*
     * 
     */
    public String receive(int mseconds) {
        String ret = null;
        try {
            // indefinite wait
            if (mseconds == -1)
                ret = this.stdoutConsumer.take(); // TODO: best?
            else
                ret = this.stdoutConsumer.poll(mseconds, TimeUnit.MILLISECONDS); // TODO: best?

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
