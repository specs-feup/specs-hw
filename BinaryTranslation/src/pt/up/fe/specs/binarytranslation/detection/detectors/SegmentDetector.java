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

package pt.up.fe.specs.binarytranslation.detection.detectors;

import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * Detects PipelinableLoops
 * 
 * @author JoaoBispo
 *
 */
public interface SegmentDetector {

    /*
     * Returns (eventually) objects of type "Loop" or list of "Loop", or other segments,
     * Example loops may include: megablock, basicblock, or frequent instruction sequences     
     */
    SegmentBundle detectSegments(InstructionStream istream);

    /*
     * Returns how much of the program the detected segments represent
     * for static segments, it represents how much of the ELF is covered (not very significant)
     * for trace segments, it represents execution coverage 
     */
    // public float getCoverage(int segmentSize);

    // TODO: move get coverage to SegmentBundle? makes more sense...
}
