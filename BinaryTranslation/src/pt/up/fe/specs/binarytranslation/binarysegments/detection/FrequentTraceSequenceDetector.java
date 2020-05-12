/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.binarytranslation.binarysegments.detection;

import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.FrequentTraceSequence;
import pt.up.fe.specs.binarytranslation.binarysegments.SegmentContext;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * 
 * @author nuno
 *
 */
public class FrequentTraceSequenceDetector extends AFrequentSequenceDetector {

    /*
     * Public constructor
     */
    public FrequentTraceSequenceDetector(InstructionStream istream) {
        super(istream);
    }

    protected BinarySegment makeFrequentSequence(List<Instruction> symbolicseq, List<SegmentContext> contexts) {
        return new FrequentTraceSequence(symbolicseq,
                contexts, this.istream.getApplicationInformation());
    }
}
