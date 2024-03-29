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
 
package pt.up.fe.specs.binarytranslation.test.profile;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.InstructionProducer;
import pt.up.fe.specs.binarytranslation.profiling.InstructionStreamProfiler;
import pt.up.fe.specs.binarytranslation.profiling.data.InstructionProfileResult;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.threadstream.ProducerEngine;

public class InstructionStreamProfilingUtils {

    private static InstructionStreamProfiler buildProfiler(Class<?> profilerClass) {

        /*
         * Construct profiler
         */
        Constructor<?> profilecons;
        try {
            profilecons = profilerClass.getConstructor();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        InstructionStreamProfiler profiler = null;
        try {
            profiler = (InstructionStreamProfiler) profilecons.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return profiler;
    }

    public static List<InstructionProfileResult> profile(Application app,
            Class<?> producerClass, Class<?> streamClass, List<Class<?>> profilerClass) {

        return InstructionStreamProfilingUtils.profile(app, producerClass, streamClass, profilerClass, -1, -1);
    }

    public static List<InstructionProfileResult> profile(Application app,
            Class<?> producerClass, Class<?> streamClass,
            List<Class<?>> profilerClass, Number startAddr, Number stopAddr) {

        /*
         * producer constructor
         */
        Constructor<?> producerConst;
        try {
            producerConst = producerClass.getConstructor(Application.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*
         * stream constructor
         */
        Constructor<?> streamcons;
        try {
            streamcons = streamClass.getConstructor(File.class, ChannelConsumer.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var resultList = new ArrayList<InstructionProfileResult>();
        try (var iproducer = (InstructionProducer) producerConst.newInstance(app)) {

            // host for threads
            var streamengine = new ProducerEngine<Instruction, InstructionProducer>(iproducer,
                    op -> op.nextInstruction(), cc -> {
                        try {
                            var istream = (InstructionStream) streamcons.newInstance(app, cc);
                            // istream.silent(true);
                            return istream;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

            // profilers
            for (var profclass : profilerClass) {

                // consumers
                var profiler = InstructionStreamProfilingUtils.buildProfiler(profclass);
                profiler.setStartAddr(startAddr);
                profiler.setStopAddr(stopAddr);

                // add to engine
                // streamengine.subscribe(consumer);
                streamengine.subscribe(os -> (profiler.profile((InstructionStream) os)));
            }

            // launch all threads (blocking)
            streamengine.launch();

            // quick hack line for measurements TODO: remove
            var istream = (InstructionStream) streamengine.getConsumer(0).getOstream();
            System.out.println(app.getAppName() + ": " + istream.getNumInstructions());

            // get results
            for (int i = 0; i < profilerClass.size(); i++) {
                var profilerresults = (InstructionProfileResult) streamengine.getConsumer(i).getConsumeResult();
                resultList.add(profilerresults);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return resultList;
    }
}
