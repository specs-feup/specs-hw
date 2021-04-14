package pt.up.fe.specs.binarytranslation.analysis;

import java.util.List;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.analysis.inouts.BasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.inouts.SimpleBasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.inouts.TraceInOuts;
import pt.up.fe.specs.binarytranslation.analysis.inouts.elimination.OutElimination;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class InOutAnalyzer extends ATraceAnalyzer {
    public enum InOutMode {
        BASIC_BLOCK,
        SIMPLE_BASIC_BLOCK,
        TRACE,
        ELIMINATION
    };

    public InOutAnalyzer(ATraceInstructionStream stream, ELFProvider elf) {
        super(stream, elf);
    }
    
    public void analyse(InOutMode mode, int window) {
        var det = buildDetector(window);
        
        List<BinarySegment> bbs = AnalysisUtils.getSegments(stream, det);
        
        if (mode == InOutMode.TRACE) {
            List<Instruction> insts = det.getProcessedInsts();
            System.out.println(insts.size() + " instructions detected");
            TraceInOuts inouts = new TraceInOuts(insts, bbs);
            inouts.calculateInOuts();
            
            inouts.printUseDefRegisters();
            inouts.printSequenceInOuts();
        }
        if (mode == InOutMode.BASIC_BLOCK) {
            for (BinarySegment bb : bbs) {
                BasicBlockInOuts inouts = new BasicBlockInOuts(bb);
                inouts.calculateInOuts();

                inouts.printUseDefRegisters();
                inouts.printSequenceInOuts();
                inouts.printResult();
            }
        }
        if (mode == InOutMode.SIMPLE_BASIC_BLOCK) {
            for (BinarySegment bb : bbs) {
                SimpleBasicBlockInOuts inouts = new SimpleBasicBlockInOuts(bb);
                inouts.calculateInOuts();
                inouts.printResult();
                
            }
        }
        if (mode == InOutMode.ELIMINATION) {
            for (BinarySegment bb : bbs) {
                System.out.println("Running elimination analysis for detected Basic Block");
                bb.printSegment();
                System.out.println("");
                var elim = new OutElimination(bb, det.getProcessedInsts());
                elim.eliminate(15);
            }
        }
    }
}
