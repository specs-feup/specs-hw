package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.util.List;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.BasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.OutElimination;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.SimpleBasicBlockInOuts;
import pt.up.fe.specs.binarytranslation.analysis.analyzers.inouts.TraceInOuts;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class InOutAnalyzer extends ABasicBlockAnalyzer {
    public enum InOutMode {
        BASIC_BLOCK,
        SIMPLE_BASIC_BLOCK,
        TRACE,
        ELIMINATION
    };

    public InOutAnalyzer(ATraceInstructionStream stream, ELFProvider elf, int window) {
        super(stream, elf, window);
    }
    
    public void analyse(InOutMode mode) {
        var bbs = this.getBasicBlocks();
        
        if (mode == InOutMode.BASIC_BLOCK) {
            for (var bb : bbs) {
                BasicBlockInOuts inouts = new BasicBlockInOuts(bb);
                inouts.calculateInOuts();

                inouts.printUseDefRegisters();
                inouts.printSequenceInOuts();
                inouts.printResult();
            }
        }
        if (mode == InOutMode.SIMPLE_BASIC_BLOCK) {
            for (var bb : bbs) {
                SimpleBasicBlockInOuts inouts = new SimpleBasicBlockInOuts(bb);
                inouts.calculateInOuts();
                inouts.printResult();
                
            }
        }
        if (mode == InOutMode.ELIMINATION) {
            for (var bb : bbs) {
                System.out.println("Running elimination analysis for detected Basic Block");
                System.out.println("");
//                var elim = new OutElimination(bb, det.getProcessedInsts());
//                elim.eliminate(15);
            }
        }
    }
}
