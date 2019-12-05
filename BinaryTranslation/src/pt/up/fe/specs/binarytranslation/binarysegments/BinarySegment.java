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

package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.Operand;

/**
 * 
 * @author Nuno
 *
 */
public interface BinarySegment {

    /*
     * 
     */
    BinarySegmentType getSegmentType();

    /*
     * Returns the total number of cycles represented by the segment
     */
    Integer getSegmentCycles();

    /*
     * Returns the total number of cycles considering the number of times the segment executes
     */
    Integer getExecutionCycles();

    /*
     * Number of instructions in the segment
     */
    int getSegmentLength();

    /*
     * Total execution latency of segment 
     */
    int getTotalLatency();

    /*
     * Retrieve list of Operands representing
     * registers which are inputs into the segment 
     */
    List<Operand> getLiveIns();

    /*
     * Retrieve list of Operands representing
     * registers which are output of the segment 
     */
    List<Operand> getLiveOuts();

    /*
     * Get list of the instructions in the segment
     */
    List<Instruction> getInstructions();

    /*
     * returns a string containing what "printSegment" prints
     */
    public String getRepresentation();

    /*
     * Prints the segment to system out
     */
    void printSegment();

    /*
     * get unique id
     */
    int getUniqueId();
}
