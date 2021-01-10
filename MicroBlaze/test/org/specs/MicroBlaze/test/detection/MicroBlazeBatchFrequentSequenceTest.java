package org.specs.MicroBlaze.test.detection;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.specs.BinaryTranslation.ELFProvider;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.fixed.FrequentTraceSequenceDetector;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.threadstream.ProducerEngine;

public class MicroBlazeBatchFrequentSequenceTest {

    private List<SegmentBundle> getSegments(ELFProvider elf) {

        // file
        File fd = SpecsIo.resourceCopy(elf.getResource());
        fd.deleteOnExit();

        // producer
        var iproducer = new MicroBlazeTraceProvider(fd);

        // host for threads
        var streamengine = new ProducerEngine<>(iproducer, op -> op.nextInstruction(),
                cc -> new MicroBlazeElfStream(fd, cc));

        // for all window sizes
        for (int i = 2; i < 10; i++) {

            var detector1 = new FrequentTraceSequenceDetector(
                    new DetectorConfigurationBuilder().withMaxWindow(i).build());
            streamengine.subscribe(istream -> detector1.detectSegments((InstructionStream) istream));
        }

        // launch all threads (blocking)
        streamengine.launch();

        var listOfSegs = new ArrayList<SegmentBundle>();
        for (var consumer : streamengine.getConsumers()) {
            var results1 = (SegmentBundle) consumer.getConsumeResult();
            listOfSegs.add(results1);
        }

        return listOfSegs;
    }

    @Test
    public void MicroBlazeFrequentSequenceDetect() {

        // Class<?> producers[] = { MicroBlazeStaticProvider.class /*, MicroBlazeTraceProvider.class*/ };

        /*        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(FrequentStaticSequenceDetector.class);
        profilerList.add(InstructionHistogram.class);*/

        // for (var file : MicroBlazeLivermoreELFN100.values()) {
        for (var file : Arrays.asList(MicroBlazeLivermoreELFN100.innerprod100)) {

            var segs = getSegments(file);
            for (var seg : segs) {
                seg.toJSON();
            }

            /*// for (var file : Arrays.asList(MicroBlazeLivermoreELFN100.linrec100)) {
            for (var producer : producers) {
            
                var result = InstructionStreamProfilingUtils.profile(
                        file.getResource(), producer, AUXMAP.get(producer),
                        profilerList, file.getKernelStart(), file.getKernelStop());
            
                for (var r : result) {
                    r.toJSON();
                }
            }*/
        }
    }
}
