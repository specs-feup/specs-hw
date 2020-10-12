package pt.up.fe.specs.binarytranslation.stream.multistream;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelProducer;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;

public class InstructionStreamProducer implements Runnable, AutoCloseable {

    /*
     * Source object
     */
    private final InstructionStream istream;

    /*
     * Variable number of channels to feed consumers
     */
    private List<ChannelProducer<Instruction>> producers;
    private List<ConcurrentChannel<Instruction>> channels;

    public InstructionStreamProducer(InstructionStream istream) {
        this.istream = istream;
        this.channels = new ArrayList<ConcurrentChannel<Instruction>>();
        this.producers = new ArrayList<ChannelProducer<Instruction>>();
    }

    /*
     * creates a new channel into which this runnable object will pump data, with depth 1
     */
    public InstructionStreamChannel newChannel() {
        return this.newChannel(1);
    }

    /*
     * creates a new channel into which this runnable object will pump data
     */
    public InstructionStreamChannel newChannel(int depth) {

        /*
         * Need new channel
         */
        var channel = new ConcurrentChannel<Instruction>(depth);
        this.channels.add(channel);
        this.producers.add(channel.createProducer());

        /*
         * Give channel consumer object to consumer?
         */
        return new InstructionStreamChannel(this.istream, channel.createConsumer());
    }

    /*
     * get specific channel
     */
    public ConcurrentChannel<Instruction> getChannel(int index) {
        return channels.get(index);
    }

    /*
     * Thread workload (putting objects into blocking channels)
     */
    @Override
    public void run() {
        while (this.istream.hasNext() && this.channels.size() > 0) {
            var instruction = this.istream.nextInstruction();

            // quick hack
            if (instruction == null)
                break;

            for (var producer : this.producers) {

                // ChannelProducer returns false immediately if fail to insert, so repeat
                while (!producer.offer(instruction))
                    ;
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.istream.close();
    }
}
