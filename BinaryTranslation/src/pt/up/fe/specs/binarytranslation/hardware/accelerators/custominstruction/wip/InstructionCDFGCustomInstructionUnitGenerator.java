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

import pt.up.fe.specs.binarytranslation.hardware.AHardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.generation.AHardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.generation.visitors.InstructionCDFGConverter;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;

public class InstructionCDFGCustomInstructionUnitGenerator extends AHardwareGenerator{

    private VerilogModuleTree module_tree;
    private ModuleDeclaration module;

    public AHardwareInstance generateHardware(InstructionCDFG icdfg) {
        
        this.module_tree = new VerilogModuleTree("test");
        this.module = module_tree.getModule();

        icdfg.getDataInputsNames().forEach(inputName ->  this.module_tree.addDeclaration(new InputPortDeclaration(inputName, 32)));
        icdfg.getDataOutputsNames().forEach(outputName -> this.module_tree.addDeclaration(new OutputPortDeclaration(outputName, 32)));

        InstructionCDFGConverter.convert(icdfg, this.module.addChild(new AlwaysCombBlock()));
        
        return new InstructionCDFGCustomInstructionUnit("testInstruction", module_tree);
    }
    
}
