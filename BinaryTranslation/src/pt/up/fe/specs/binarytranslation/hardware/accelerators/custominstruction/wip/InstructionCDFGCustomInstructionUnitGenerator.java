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

import java.util.Map;

import pt.up.fe.specs.binarytranslation.hardware.AHardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.generation.AHardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.generation.visitors.InstructionCDFGConverter;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.SingleStatement;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;

public class InstructionCDFGCustomInstructionUnitGenerator extends AHardwareGenerator{

    private VerilogModuleTree module_tree;
    private ModuleDeclaration module;
    
    private InstructionCDFGConverter converter;
    
    private Map<AInstructionCDFGNode, SingleStatement> completed_statements;
    
    public void addComment(String commentText) {
        
    }
    
    
    
    public AHardwareInstance generateHardware(InstructionCDFG icdfg) {
        
        this.module_tree = new VerilogModuleTree("test");
        this.module = module_tree.getModule();
        
        
        
        icdfg.getDataInputs().forEach((input) -> {
            this.module_tree.addDeclaration(new PortDeclaration(input.getUID(), 32, ModulePortDirection.input));
        });
        
        icdfg.getDataOutputs().forEach((output) -> {
            this.module_tree.addDeclaration(new PortDeclaration(output.getUID(), 32, ModulePortDirection.output));
        });
        

        InstructionCDFGConverter.convertInstruction(this.module.addChild(new AlwaysCombBlock()), icdfg);
        
        
        return new InstructionCDFGCustomInstructionUnit("test", module_tree);
    }
    
}
