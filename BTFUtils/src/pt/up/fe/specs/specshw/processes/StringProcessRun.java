/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package pt.up.fe.specs.specshw.processes;

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
        return this.receive(2);
    }

    /*
     * 
     */
    @Override
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
        // System.out.println(ret);
        return ret;
    }
}
