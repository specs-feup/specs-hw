package pt.up.fe.specs.binarytranslation.test.multiconsumer;

import java.util.Random;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.stream.replicator.ConsumerThread;
import pt.up.fe.specs.binarytranslation.stream.replicator.ObjectStream;
import pt.up.fe.specs.binarytranslation.stream.replicator.ProducerEngine;

public class MultiConsumerTest {

    /*
     * Test class
     */
    private class StringProducer {

        private int producecount;

        public StringProducer(int producecount) {
            this.producecount = producecount;
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

        // file

        // host for threads
        var sb = new StringProducer(20);
        var streamengine = new ProducerEngine<String, StringProducer>(sb, op -> op.getString());

        // consumers
        var consumer1 = new StringConsumer();

        // consumer thread hosters
        var consumethread1 = new ConsumerThread<String, Integer>(os -> consumer1.consumeString(os));

        // host threads
        streamengine.subscribe(consumethread1);

        // launch all threads (blocking)
        streamengine.launch();

        var result1 = consumethread1.getConsumeResult();
        System.out.print(result1);
    }
}
