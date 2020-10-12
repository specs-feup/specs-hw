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

package pt.up.fe.specs.binarytranslation.detection.segments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.BinaryTranslationOutput;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * 
 * @author Nuno
 *
 */
public interface BinarySegment extends BinaryTranslationOutput {

    /*
     * 
     */
    public BinarySegmentType getSegmentType();

    /*
     * Total execution latency of segment 
     */
    public int getLatency();

    /*
     * Returns the total number of cycles considering the number of times the segment executes
     */
    public int getExecutionCycles();

    /*
     * 
     */
    public Application getAppinfo();

    /*
     * 
     */
    public void setStaticCoverage(float scoverage);

    /*
     * 
     */
    public void setDynamicCoverage(float dcoverage);

    /*
     * 
     */
    public float getStaticCoverage();

    /*
     * 
     */
    public float getDynamicCoverage();

    /*
     * Number of instructions in the segment
     */
    public int getSegmentLength();

    /*
     * 
     */
    public List<SegmentContext> getContexts();

    /*
     * Get list of the instructions in the segment
     */
    public List<Instruction> getInstructions();

    /*
     * returns a string containing what "printSegment" prints
     */
    public String getRepresentation();

    /*
     * Prints the segment to system out
     */
    public void printSegment();

    /*
     * get unique id
     */
    public int getUniqueId();

    /*
     * 
     */
    public int getOccurences();
}
