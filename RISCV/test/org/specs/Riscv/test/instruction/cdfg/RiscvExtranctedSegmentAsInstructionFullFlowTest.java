package org.specs.Riscv.test.instruction.cdfg;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentStaticSequenceDetector;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.InstructionPseudocode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.fullflow.InstructionCDFGFullFlow;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.binarytranslation.lex.listeners.TreeDumper;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.StaticInstructionStream;

public class RiscvExtranctedSegmentAsInstructionFullFlowTest {

    private int numberOfSamples = 100;
    private String pathToDirectory = "/home/soiboi/Desktop/RISCV_FULLFLOW/";
    

    private class SegmentParseTree implements Instruction, InstructionPseudocode{
        
        private String name;
        private String pseudocode;
        
        public SegmentParseTree(String name, String pseudocode) {
            this.name = name;
            this.pseudocode = pseudocode;
        }
        
        public SegmentParseTree(String name, List<Instruction> instructions) {
            this.name = name;
            
            this.pseudocode = "";
            
            for(Instruction singleInstruction : instructions) {
                System.out.println(singleInstruction.getPseudocode().getParseTree().getText());
                this.pseudocode += singleInstruction.getPseudocode().getParseTree().getText();
            }
            System.out.println(this.pseudocode);
        }
        
        public PseudoInstructionContext getParseTree() {
            return (new PseudoInstructionParser(new CommonTokenStream(new PseudoInstructionLexer(new ANTLRInputStream(this.pseudocode))))).pseudoInstruction();
        }
        
        public String getName() {
            return this.name;
        }

        @Override
        public String getCode() {
            return this.pseudocode;
        }
        
        @Override
        public InstructionPseudocode getPseudocode() {
            return this;
        }
        
        public void printParseTree() {
            PseudoInstructionContext parse = this.getParseTree();
            var walker = new ParseTreeWalker();
            walker.walk(new TreeDumper(), parse);
        }
        
    }
    
    protected DetectorConfigurationBuilder getConfig(InstructionStream istream) {
        var builder = new DetectorConfigurationBuilder();
        builder.withSkipToAddr(istream.getApp().getKernelStart());
        builder.withPrematureStopAddr(istream.getApp().getKernelStop());
        builder.withStartAddr(istream.getApp().getKernelStart());
        builder.withStopAddr(istream.getApp().getKernelStop());
        return builder;
    }
    
    protected  SegmentBundle getFrequentStaticSequences(StaticInstructionStream istream) {
        
        var detector = new FrequentStaticSequenceDetector(getConfig(istream).withMaxWindow(3).build());
        return detector.detectSegments(istream);
        
    }
    
    @Test
    public void testFullFlow() throws IOException, InterruptedException, ExecutionException {
        
        for(var benchmark : RiscvLivermoreN100im.values()) {
            
            SegmentBundle benchmarkSegments = this.getFrequentStaticSequences(benchmark.toStaticStream());
            
            for(var segment : benchmarkSegments.getSegments()) {
                
                SegmentParseTree spt = new SegmentParseTree(segment.getJSONName().replace(".json", ""), segment.getInstructions());
                
                InstructionCDFGFullFlow fullFlow = new InstructionCDFGFullFlow(spt, this.numberOfSamples, this.pathToDirectory);
                
                fullFlow.runAll();
                
            }
            
            
        }
        
    }
    
}
