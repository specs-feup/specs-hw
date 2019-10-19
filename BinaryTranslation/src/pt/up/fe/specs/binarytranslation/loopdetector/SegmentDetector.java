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

package pt.up.fe.specs.binarytranslation.loopdetector;

import java.util.List;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;

/**
 * Detects PipelinableLoops
 * 
 * @author JoaoBispo
 *
 */
public interface SegmentDetector extends AutoCloseable {

    /*
     * Returns (eventually) objects of type "Loop" or list of "Loop", or other segments,
     * Example loops may include: megablock, basicblock, or frequent instruction sequences     
     */
    List<BinarySegment> detectSegments();
}
