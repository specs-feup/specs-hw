package pt.up.fe.specs.binarytranslation.test.multiconsumer;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.utils.replicator.ConsumerThread;
import pt.up.fe.specs.binarytranslation.utils.replicator.GenericObjectStream;
import pt.up.fe.specs.binarytranslation.utils.replicator.ObjectProducer;
import pt.up.fe.specs.binarytranslation.utils.replicator.ProducerEngine;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class MultiConsumerTest {

    /*
     * Test producer
     */
    private class StringProducer implements ObjectProducer<String> {

        private int producecount;

        public StringProducer(int producecount) {
            this.producecount = producecount;
        }

        @Override
        public String getPoison() {
            return "kill";
        }

        public String getString() {

            if (this.producecount-- == 0)
                return null;

            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();

            return random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        }

        @Override
        public void close() throws Exception {
            // TODO Auto-generated method stub
        }
    }

    /*
     * Test custom stream name
     */
    private class StringStream extends GenericObjectStream<String> {

        public StringStream(StringProducer producer, ChannelConsumer<String> consumer) {
            super(consumer, producer.getPoison());
        }
    }

    /*
     * Test consumer
     */
    private class StringConsumer {

        public StringConsumer() {
            // TODO Auto-generated constructor stub
        }

        public Integer consumeString(StringStream istream) {

            Integer hashcode = 0;
            String str = null;
            while ((str = istream.next()) != null) {
                hashcode += str.hashCode();
            }
            return hashcode;
        }

    }

    @Test
    public void test() {

        // host for threads
        var sb = new StringProducer(100000);
        var streamengine = new ProducerEngine<String, StringProducer>(sb, op -> op.getString(),
                cc -> new StringStream(sb, cc));

        // new consumer thread via lambda
        var threadlist = new ArrayList<ConsumerThread<String, ?>>();
        for (int i = 0; i < 20; i++)
            threadlist.add(streamengine.subscribe(os -> (new StringConsumer()).consumeString((StringStream) os)));

        // launch all threads (blocking)
        streamengine.launch();

        // consume
        for (int i = 0; i < 20; i++) {
            System.out.println(threadlist.get(i).getConsumeResult());
        }
    }
}
