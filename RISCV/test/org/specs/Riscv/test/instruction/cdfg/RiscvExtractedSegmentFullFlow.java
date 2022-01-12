package org.specs.Riscv.test.instruction.cdfg;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.dot.InstructionCDFGDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.resolve_names.InstructionCDFGNameResolver;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.SegmentCDFG;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;

public class RiscvExtractedSegmentFullFlow {

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
        
        var detector = new FrequentStaticSequenceDetector(getConfig(istream).withMaxWindow(3).build());
        
        return detector.detectSegments(istream);
    }
    
    @Test
    public void initialTest() {
        
        SegmentBundle bundle = this.getFrequentStaticSequences(RiscvLivermoreN100im.cholesky.toStaticStream());
        
        
        
        bundle.getSegments().forEach(segment -> {
            
            SegmentCDFG scdfg = new SegmentCDFG(segment.getInstructions());
            scdfg.generate();
            
            InstructionCDFGDOTExporter exp = new InstructionCDFGDOTExporter();
            Writer writer = new StringWriter();
            exp.exportGraph((GeneralFlowGraph)scdfg, "test", writer);
            
            InstructionCDFGNameResolver.resolve(scdfg);
            
            segment.printSegment();
            
            scdfg.refresh();
            
            System.out.println(InstructionCDFGDOTExporter.generateGraphURL(writer.toString()));
            
        });
        
    }
}
