package pt.up.fe.specs.binarytranslation.stream.multistream;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.NullInstruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelProducer;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.threadstream.ObjectStream;

public class InstructionStreamProducer implements Runnable, AutoCloseable {

    /*
     * Source object
     */
    private final InstructionStream istream;

    /*
     * Variable number of channels to feed consumers
     */
    private List<ChannelProducer<Instruction>> producers;

    /*
     * "Constructor" for stream objects to feed to consumers
     */
    private final Function<ChannelConsumer<Instruction>, ObjectStream<Instruction>> cons;

    /*
     * Variable number of channels to feed consumers
     */
    private List<ConcurrentChannel<Instruction>> channels;

    public InstructionStreamProducer(InstructionStream istream) {
        this(istream, cc -> new InstructionStreamChannel(istream, cc));
    }

    public InstructionStreamProducer(InstructionStream istream,
            Function<ChannelConsumer<Instruction>, ObjectStream<Instruction>> cons) {
        this.istream = istream;
        this.cons = cons;
        this.channels = new ArrayList<ConcurrentChannel<Instruction>>();
        this.producers = new ArrayList<ChannelProducer<Instruction>>();
    }

    /*
     * creates a new channel into which this runnable object will pump data, with depth 1
     */
    public ObjectStream<Instruction> newChannel() {
        return this.newChannel(1);
    }

    /*
     * creates a new channel into which this runnable object will pump data
     */
    public ObjectStream<Instruction> newChannel(int depth) {

        /*
         * Need new channel
         */
        var channel = new ConcurrentChannel<Instruction>(depth);
        this.channels.add(channel);
        this.producers.add(channel.createProducer());

        /*
         * Give channel consumer object to consumer?
         */
        return this.cons.apply(channel.createConsumer());
        // return new InstructionStreamChannel(this.istream, channel.createConsumer());
    }

    /*
     * get specific channel
     */
    public ConcurrentChannel<Instruction> getChannel(int index) {
        return channels.get(index);
    }

    /*
     * ChannelProducer returns false immediately if fail to insert, so repeat
     */
    private void insertToken(ChannelProducer<Instruction> prod, Instruction inst) {
        while (!prod.offer(inst))
            ;
    }

    /*
     * Thread workload (putting objects into blocking channels)
     */
    @Override
    public void run() {
        while (this.istream.hasNext() && this.channels.size() > 0) {

            // insert to all channels
            var inst = this.istream.next();
            for (var producer : this.producers) {
                insertToken(producer, inst);
            }
        }

        // insert poison terminator to all channels
        for (var producer : this.producers) {
            insertToken(producer, NullInstruction.NullInstance);
        }
    }

    @Override
    public void close() throws Exception {
        this.istream.close();
    }
}