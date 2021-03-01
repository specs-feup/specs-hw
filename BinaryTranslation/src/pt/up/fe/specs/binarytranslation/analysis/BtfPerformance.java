package pt.up.fe.specs.binarytranslation.analysis;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.producer.AInstructionProducer;
import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;

public class BtfPerformance {
    private AInstructionProducer provider;

    public BtfPerformance (AInstructionProducer provider) {
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
