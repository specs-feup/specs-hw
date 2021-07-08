package pt.up.fe.specs.binarytranslation.analysis.analyzers;

import org.specs.BinaryTranslation.ELFProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.TraceBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public abstract class ABasicBlockAnalyzer {
    protected ATraceInstructionStream stream;
    protected ELFProvider elf;

    public ABasicBlockAnalyzer(ATraceInstructionStream stream, ELFProvider elf) {
        this.stream = stream;
        this.elf = elf;
    }

    protected TraceBasicBlockDetector buildDetector(int window) {
        stream.silent(false);
//        if (elf.getKernelStart() != null)
//            stream.advanceTo(elf.getKernelStart().longValue());
        
        System.out.println("Building detector for window size " + window);

        var detector = new TraceBasicBlockDetector(
                elf.getKernelStart() != null ? new DetectorConfigurationBuilder()
                        .withMaxWindow(window)
                        //.withStartAddr(elf.getKernelStart())
                        //.withStopAddr(elf.getKernelStop())
                        .build()
                        : new DetectorConfigurationBuilder()
                                .withMaxWindow(window)
                                .build());
        return detector;
    }
}
