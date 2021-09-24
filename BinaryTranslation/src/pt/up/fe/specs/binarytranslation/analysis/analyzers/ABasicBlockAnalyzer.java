package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.ZippedELFProvider;
import pt.up.fe.specs.binarytranslation.analysis.AnalysisUtils;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public abstract class ABasicBlockAnalyzer {
    private ATraceInstructionStream stream;
    protected ZippedELFProvider elf;
    private List<List<Instruction>> basicBlocks = new ArrayList<>();
    private List<BinarySegment> basicBlockSegments = new ArrayList<>();
    private int window = 0;

    public ABasicBlockAnalyzer(ATraceInstructionStream stream, ZippedELFProvider elf, int window) {
        this.stream = stream;
        this.elf = elf;
        this.window = window;
    }

    public ABasicBlockAnalyzer(List<List<Instruction>> basicBlocks) {
        this.basicBlocks = basicBlocks;
    }

    protected List<BinarySegment> getBasicBlockSegments() {
        if (this.basicBlocks.size() == 0) {
            System.out.println(elf.getFilename() + ": building detector for window size " + window);

            var detector = new TraceBasicBlockDetector(new DetectorConfigurationBuilder()
                                    .withMaxWindow(window)
                                    .build());
            basicBlockSegments = AnalysisUtils.getSegments(stream, detector);
        }
        return basicBlockSegments;
    }

    protected List<List<Instruction>> getBasicBlocks() {
        if (basicBlocks.size() == 0) {
            var bbs = getBasicBlockSegments();
            for (var bb : bbs) {
                basicBlocks.add(bb.getInstructions());
            }
        }
        return basicBlocks;
    }
}
