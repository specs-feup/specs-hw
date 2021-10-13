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
 
package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.AInstructionProducer;

public class BtfPerformanceAnalyzer {
    private AInstructionProducer provider;

    public BtfPerformanceAnalyzer(AInstructionProducer provider) {
        this.provider = provider;
    }

    public long calcInstructionsPerSecond() {
        long startTime = System.nanoTime();
        Instruction inst = provider.nextInstruction();
        if (inst == null)
            return -1;

        int instCount = 0;
        while (inst != null) {
            instCount++;
            inst = provider.nextInstruction();
        }

        long stopTime = System.nanoTime();
        long avgTimePerInst = (stopTime - startTime) / instCount;
        long avgTimeMicro = TimeUnit.MICROSECONDS.convert(avgTimePerInst, TimeUnit.NANOSECONDS);
        long instPerSec = 1000000 / avgTimeMicro;

        System.out.println("Total instructions: " + instCount);
        System.out.println("Avg time per inst: " + avgTimeMicro + "microsseconds");
        System.out.println("Inst per second: " + instPerSec);

        return instPerSec;
    }
}
