/**
 * Copyright 2021 SPeCS.
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
 
package pt.up.fe.specs.binarytranslation.detection.detectors.fixed;

import java.util.List;

import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.detection.segments.SegmentContext;
import pt.up.fe.specs.binarytranslation.detection.segments.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

/**
 * Detects all basic blocks in an instruction trace; trace must be provided by any implementation of
 * {@link ATraceInstructionStream}
 * 
 * @author NunoPaulino
 *
 */
public class TraceBasicBlockDetector extends AFixedSizeBasicBlockDetector {

    /*
     * 
     */
    public TraceBasicBlockDetector() {
        super(DetectorConfigurationBuilder.defaultConfig());
    }

    public TraceBasicBlockDetector(DetectorConfiguration config) {
        super(config);
    }

    /*
     * 
     */
    @Override
    protected BinarySegment makeSegment(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new TraceBasicBlock(symbolicseq, contexts, this.getCurrentStream().getApp());
    }
}
