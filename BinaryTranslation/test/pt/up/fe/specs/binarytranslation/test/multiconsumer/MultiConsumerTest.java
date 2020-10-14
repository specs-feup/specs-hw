package pt.up.fe.specs.binarytranslation.test.multiconsumer;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.stream.replicator.ConsumerThread;
import pt.up.fe.specs.binarytranslation.stream.replicator.ObjectProducer;
import pt.up.fe.specs.binarytranslation.stream.replicator.ObjectStream;
import pt.up.fe.specs.binarytranslation.stream.replicator.ProducerEngine;

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
    }

    /*
     * Test consumer
     */
    private class StringConsumer {

        public StringConsumer() {
            // TODO Auto-generated constructor stub
        }

        public Integer consumeString(ObjectStream<String> istream) {

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
        var sb = new StringProducer(20);
        var streamengine = new ProducerEngine<String, StringProducer>(sb, op -> op.getString());

        // new consumer thread via lambda
        var threadlist = new ArrayList<ConsumerThread<String, ?>>();
        for (int i = 0; i < 4; i++)
            threadlist.add(streamengine.subscribe(os -> (new StringConsumer()).consumeString(os)));

        // launch all threads (blocking)
        streamengine.launch();

        for (int i = 0; i < 4; i++) {
            System.out.println(threadlist.get(i).getConsumeResult());
        }
    }
}
