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

package pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.wip;

import pt.up.fe.specs.binarytranslation.hardware.AHardwareArchitecture;
import pt.up.fe.specs.binarytranslation.hardware.generation.AHardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.generation.visitors.InstructionCDFGConverter;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;

public class InstructionCDFGCustomInstructionUnitGenerator extends AHardwareGenerator {

    private VerilogModuleTree moduletree;
    private ModuleHeader module;

    public AHardwareArchitecture generateHardware(InstructionCDFG icdfg, String moduleName) {

        this.moduletree = new VerilogModuleTree(moduleName);
        this.module = moduletree.getModule();

        /*
         * don't pass this.module_tree into the constructor of PortDeclarations; 
         * you should be able to construct a port, and then associate it with something
         * when its returned
         * 
         * nmcp
         */
        for (var in : icdfg.getDataInputsNames())
            moduletree.addPort(new InputPortDeclaration(in, 32));

        for (var out : icdfg.getDataOutputsNames())
            moduletree.addPort(new OutputPortDeclaration(out, 32));

        /*
        icdfg.getDataInputsNames().forEach(inputName ->  new InputPortDeclaration(new WireDeclaration(inputName, 32), this.module_tree));
        icdfg.getDataOutputsNames().forEach(outputName -> new OutputPortDeclaration(new RegisterDeclaration(outputName, 32), this.module_tree));
        */

        InstructionCDFGConverter.convert(icdfg, this.module.addChild(new AlwaysCombBlock()));

        return new InstructionCDFGCustomInstructionUnit(moduleName, moduletree);
    }

}
