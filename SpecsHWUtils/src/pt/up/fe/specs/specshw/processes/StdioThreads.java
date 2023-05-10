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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.utilities.LineStream;

public class StdioThreads {

    private static LineStream newLineStream(InputStream stream, String name) {

        // No error detected, obtain LineStream via a concurrentchannel to allow for a
        // small wait for the exe to generate the stdout
        LineStream insts = null;
        try {
            var lineStreamChannel = new ConcurrentChannel<LineStream>(1);
            lineStreamChannel.createProducer().offer(LineStream.newInstance(stream, name));
            insts = lineStreamChannel.createConsumer().poll(1, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (insts == null) {
            throw new RuntimeException("Could not obtain output stream of stream generation process");
        }

        return insts;
    }

    /*
    protected static void stdoutThread(ProcessRun run, BiFunction<ProcessRun, LineStream, Boolean> processing) {
        
        var lstream = StdioThreads.newLineStream(run.getProc().getInputStream(), "proc_stdout");
        var producer = run.getStdout().createProducer();
        while(processing.apply(run, lstream));
        lstream.close();
        
    }*/

    /*
     * Appropriate for non interactive processes with single launch and full stdout consumption
     * e.g. elfdumps, cats from files, etc
     */
    protected static void stdoutThreadFreeRun(ProcessRun run) {

        var lstream = StdioThreads.newLineStream(run.getProc().getInputStream(), "proc_stdout");
        var producer = run.getStdout().createProducer();

        // this thread will block here if "nextLine" is waiting for content
        // of if main thread has not read the concurrentchannel for the
        // previous stdout line
        while (lstream.hasNextLine()) {
            var peek = lstream.peekNextLine();
            if (peek != null) {
                producer.put(peek);
                lstream.nextLine();
            }
        }

        lstream.close();

        // thread end
        return;
    }

    /*
     * Appropriate for interactive processes where process death is
     * caused by command given by BTF (e.g., GDBRun)
     */
    protected static void stdoutThreadInteractive(ProcessRun run) {

        var streamReader = new InputStreamReader(run.getProc().getInputStream());
        var br = new BufferedReader(streamReader);
        var producer = run.getStdout().createProducer();

        try {
            while (run.getProc().isAlive()) {
                var nextLine = br.readLine(); // blocking
                if (nextLine == null)
                    break; // process has died after last check of isAlive (?)
                           // or I tried to send null to the process?? (this is the outgoing thread)

                producer.put(nextLine);
            }
            br.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();

        }

        // thread end
        return;
    }

    protected static void stderrThread(ProcessRun run) {

        var lstream = StdioThreads.newLineStream(run.getProc().getErrorStream(), "proc_stderror");
        while (lstream.hasNextLine()) {
            lstream.nextLine();
        }
        lstream.close();

        // thread end
        return;
    }

    protected static void stdinThread(ProcessRun run) {

        var bw = new BufferedWriter(new OutputStreamWriter(run.getProc().getOutputStream()));
        var consumer = run.getStdin().createConsumer();
        try {
            while (run.getProc().isAlive()) {
                var sendToProc = consumer.poll(5, TimeUnit.SECONDS);
                if (sendToProc == null)
                    continue;

                bw.write(sendToProc); // blocking
                bw.newLine();
                bw.flush();
            }

            /*
            while (true) {
                var st = consumer.take(); // wait for "main" thread to produce stuff
                if (run.getProc().isAlive()) { // can only send to proc if proc is alive
                    bw.write(st);
                    bw.newLine();
                    bw.flush();
                } else
                    break;
            }*/
            bw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // thread end
        return;
    }
}
