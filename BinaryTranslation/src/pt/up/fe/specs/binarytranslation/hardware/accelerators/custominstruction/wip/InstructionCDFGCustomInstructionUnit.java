/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.wip;

import pt.up.fe.specs.binarytranslation.hardware.AHardwareModule;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;

public class InstructionCDFGCustomInstructionUnit extends AHardwareModule {

    // private List<Port> inputs, outputs;
    // Port is template class??

    // maybe to simplify, all units should be stream type? fifo interface?z

    // private List<FunctionalUnit> units;

    // private List<HardwareRegisters> registers;
    // HardwareRegister is a template?? templated for bitwidth?
    // or is a register of a generic type of hwdata, including crisp structs etc?

    // private int depth;
    // private String unitName; // how to create names?

    // NOTE: the AHardwareInstance class may maintain its List of strings for the code
    // but I can generate that list to emit, using the private fields of
    // each hardwareinstance subtype

    // TODO: constructor which makes use of all the fields above

    /*
     * Custom Instruction Units represent a single graph implemented as single-cycle operation
     */
   
    /**
     * Should be called by {@code CustomInstructionUnitGenerator}. Basic constructor which receives a list of lines of
     * code directly. Most naive method of definition of hardware module.
     * 
     * @param code
     * @return
     */
    protected InstructionCDFGCustomInstructionUnit(String instanceName, VerilogModuleTree tree) {
        super(instanceName, tree);
    }
      
    }