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

package pt.up.fe.specs.binarytranslation.hardware.generation;

import java.util.List;

import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.hardware.HardwareArchitecture;

public abstract class AHardwareGenerator implements HardwareGenerator {

    // TODO maybe this module should be an abstract class to, since theres lots of "verlig modules" i can generate
    // either that, or i reverse the relationship between stuff:
    // ....// HardwareGenerator (I)
    // ....// AHardwareGenerator (AC)
    // ......// SingleCycleModule (C) --> could internally resolve the language?
    // ......// PipelinedDataPathModule (C) --> could internally resolve the language?
    // ......// LoopAcceleratorModule (C) --> could internally resolve the language?

    @Override
    public HardwareArchitecture generateHardware(BinarySegmentGraph graph) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HardwareArchitecture generateHardware(List<BinarySegmentGraph> graphs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HardwareArchitecture generateHarware(GraphBundle gbundle) {
        // TODO Auto-generated method stub
        return null;
    }

}
