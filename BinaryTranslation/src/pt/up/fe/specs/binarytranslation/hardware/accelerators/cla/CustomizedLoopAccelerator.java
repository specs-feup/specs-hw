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
 
package pt.up.fe.specs.binarytranslation.hardware.accelerators.cla;

import java.util.List;

import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.hardware.AHardwareArchitecture;

/**
 * This type of accelerator is only composed of a parameter list to support the synthesis of a legacy CLA design (see
 * https://bitbucket.org/nmcp88/cla-ipcores)
 */
public class CustomizedLoopAccelerator extends AHardwareArchitecture {

    /*
     * Should represent a Basic Block or Megablock loop
     */
    private BinarySegmentGraph graph;

    /*
     * Modulo reservation table
     */
    private List<List<Integer>> mrt;

    // TODO issue: how to translate specific instructions from any isa to FUs?
    // generate the FU?

    /**
     * Should be called by {@code CustomizedLoopAcceleratorGenerator}. Basic constructor which receives a list of lines
     * of code directly. Most naive method of definition of hardware module.
     * 
     * @param code
     * @return
     */
    protected CustomizedLoopAccelerator(BinarySegmentGraph graph) {
        super(null, null); // quick fix
        this.graph = graph;
    }
}
