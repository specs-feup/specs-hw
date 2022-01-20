package org.specs.Riscv.test.instruction.cdfg;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.fullflow.SegmentCDFGFullFlow;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;

public class RiscvExtractedSegmentFullFlow {

    private int numberOfSamples = 10000;
    private String pathToDirectory = "/home/soiboi/Desktop/RISCV_FULLFLOW/";
    
    protected DetectorConfigurationBuilder getConfig(InstructionStream istream) {
        var builder = new DetectorConfigurationBuilder();
        builder.withSkipToAddr(istream.getApp().getKernelStart());
        builder.withPrematureStopAddr(istream.getApp().getKernelStop());
        builder.withStartAddr(istream.getApp().getKernelStart());
        builder.withStopAddr(istream.getApp().getKernelStop());
        return builder;
    }
    
    protected void getStaticBasicBlocks(StaticInstructionStream istream) {
        var detector = new StaticBasicBlockDetector(getConfig(istream).withMaxWindow(6).build());
        var bundle = detector.detectSegments(istream);
        bundle.printBundle();
    }
    
    protected  SegmentBundle getFrequentStaticSequences(StaticInstructionStream istream) {
        
        var detector = new FrequentStaticSequenceDetector(getConfig(istream).withMaxWindow(2).build());
        
        return detector.detectSegments(istream);
    }
    
    public void work(StaticInstructionStream istream) {
        
        SegmentBundle bundle = this.getFrequentStaticSequences(istream);
        
       bundle.getSegments().forEach(segment -> {
            
           
            segment.printSegment();
            
            SegmentCDFGFullFlow fullFlow = new SegmentCDFGFullFlow(segment.getInstructions(), segment.getJSONName().replace(".json", ""), numberOfSamples, pathToDirectory);

   
            fullFlow.runAll();
   
            
        });
    }
    
    @Test
    public void initialTest() {
        
        for(var benchmark : RiscvLivermoreN100im.values()) {
            this.work(benchmark.toStaticStream());
            break;
        }

    }
}
