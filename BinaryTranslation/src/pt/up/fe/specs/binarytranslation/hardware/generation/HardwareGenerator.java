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
 
package pt.up.fe.specs.binarytranslation.hardware.generation;

import java.util.List;

import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.hardware.HardwareArchitecture;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;

public interface HardwareGenerator {

    /*
     * Create a type of hardware instance from one binary segment
     */
    default public HardwareArchitecture generateHardware(BinarySegmentGraph graph) {
        return null;
    }

    /*
     * Create a type of hardware instance from binary segments
     */
    default public HardwareArchitecture generateHardware(List<BinarySegmentGraph> graphs) {
        return null;
    }

    /*
     * Create a type of hardware instance from a bundle of graphs 
     */
    default public HardwareArchitecture generateHarware(GraphBundle gbundle) {
        return null;
    }

    /*
     * 
     */
    default public HardwareArchitecture generateHarware(Instruction inst) {
        return null;
    }
}
