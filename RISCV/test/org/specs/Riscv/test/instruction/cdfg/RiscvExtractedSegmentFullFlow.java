package org.specs.Riscv.test.instruction.cdfg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    private int numberOfSamples = 100;
    private String pathToDirectory = "/home/soiboi/Desktop/RISCV_FULLFLOW/";
    private String finalPath;
    
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
    
    protected SegmentBundle getFrequentStaticSequences(StaticInstructionStream istream, int windowSize) {
        
        var detector = new FrequentStaticSequenceDetector(getConfig(istream).withMaxWindow(windowSize).build());
        
        return detector.detectSegments(istream);
    }
    
    public void work(StaticInstructionStream istream, int windowSize) {
        
        SegmentBundle bundle = this.getFrequentStaticSequences(istream, windowSize);
        
       bundle.getSegments().forEach(segment -> {
            
           
            segment.printSegment();
            
            System.out.println("\n");
            
            SegmentCDFGFullFlow fullFlow = new SegmentCDFGFullFlow(finalPath, segment, numberOfSamples);

   
            fullFlow.runAll();
   
            
        });
    }
    
    @Test
    public void initialTest() {
        
        for(var benchmark : RiscvLivermoreN100im.values()) {
            
            this.finalPath = this.pathToDirectory +  benchmark.getELFName().replace(".elf", "") + "/";
            
            try {
                Files.createDirectory(Paths.get(this.finalPath));
            } catch (IOException e) {
                continue;
            }
            
            for(int i = 2; i < 6; i++) {
                this.work(benchmark.toStaticStream(), i);
                System.out.println("\n\n\n\n\n");
                System.out.flush();
                break;
            }
        break;
        }

    }
}
