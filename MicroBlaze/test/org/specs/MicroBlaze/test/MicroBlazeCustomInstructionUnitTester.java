/**
 * Copyright 2020 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.specs.MicroBlaze.test;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.detection.*;
import pt.up.fe.specs.binarytranslation.graphs.GraphBundle;
import pt.up.fe.specs.binarytranslation.hardware.custominstruction.CustomInstructionUnitGenerator;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeCustomInstructionUnitTester {

    private File openFile() {

        // static
        File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky.txt");

        // dynamic
        // File fd = SpecsIo.resourceCopy("org/specs/MicroBlaze/asm/cholesky_trace.txt");

        fd.deleteOnExit();
        return fd;
    }

    @Test
    public void testCustomUnitGenerate() {

        // get static frequent sequences from program
        FrequentStaticSequenceDetector bbd = null;
        SegmentBundle bundle = null;
        GraphBundle graphs = null;
        try (MicroBlazeElfStream el = new MicroBlazeElfStream(openFile())) {

            // new detector
            bbd = new FrequentStaticSequenceDetector(el);

            // TODO: code dies if bbd.detectSegments is called out of the
            // scope of this try-catch block, since the stream "el" is closed,
            // therefore the detector fails ungracefully (silently blocks)

            // generate segment bundle, and graph bundle
            bundle = bbd.detectSegments();
            graphs = GraphBundle.newInstance(bundle);
        }

        // generate custom instruction unit for one of the graphs
        var customInstGen = new CustomInstructionUnitGenerator();

        var graph = graphs.getGraphs(data -> data.getLiveins().size() > 0 && data.getLiveouts().size() > 0).get(0);
        var ciUnit = customInstGen.generateHardware(graph);

        // emit code for first graph with non-zero number of liveouts and liveins
        graph.generateOutput();
        ciUnit.emit(System.out);
    }
}
