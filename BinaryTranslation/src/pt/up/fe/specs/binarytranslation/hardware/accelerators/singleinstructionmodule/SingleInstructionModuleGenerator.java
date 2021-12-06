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

package pt.up.fe.specs.binarytranslation.hardware.accelerators.singleinstructionmodule;

import pt.up.fe.specs.binarytranslation.hardware.HardwareModule;
import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.generation.visitors.InstructionASTConverter;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareCommentNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.task.HardwareErrorMessage;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNodeType;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;

public class SingleInstructionModuleGenerator implements HardwareGenerator {

    public SingleInstructionModuleGenerator() {
        // TODO: add generation parameters
        // e.g.,
        // 1. number of register stages
        // 2. insert handshaking logic
        // 3. insert halt signal
    }

    // TODO: if the pseudoinstruction has multiple statements, then a single verilog "assign" will not work, sicne it
    // violates verilog rules
    // I have to choose what to generate based on general characteristics of the AST..
    // for example, if the same ASM field is assigned a value more than once (does this even happen??)

    /**
     * 
     * @param inst
     *            The instruction thats being used for generation
     */
    @Override
    public HardwareModule generateHarware(Instruction inst) {

        // The VerilogModuleTree
        var moduletree = new VerilogModuleTree(inst.getName() + "_" + inst.getInstruction());
        var module = moduletree.getModule();

        /////////////////////////////////////////////////////////////////////////////////////////
        // Information we can only grasp from the instruction encoding //////////////////////////

        // build all ports, based on operands
        var instOperands = inst.getData().getOperands();
        for (var op : instOperands) {

            var name = op.getRepresentation().replace("[", "").replace("]", "");
            var width = op.getProperties().getWidth();

            if (op.isImmediate())
                continue;

            // TODO: solve inputs/outputs with same name!

            else if (op.isRead())
                moduletree.addDeclaration(new InputPortDeclaration(name, width));

            else if (op.isWrite())
                moduletree.addDeclaration(new OutputPortDeclaration(name, width));
        }

        /////////////////////////////////////////////////////////////////////////////////////////
        // Information we can only grasp from the pseudocode (i.e. instruction implementation) //

        // get AST of instruction
        var ast = new InstructionAST(inst);

        // TODO: a first pass which replaces AST nodes with info from the instruction, specifically,
        // replaces OperandASTNode with LIveinNode,Liveout,internal internal extends to -> variablenode and
        // immediatenode

        // Apply first pass (attaches executed instruction information to the AST of the instruction)
        ast.accept(new ApplyInstructionPass());

        // add comment
        module.addChild(new HardwareCommentNode("implementation for instruction: " + inst.getRepresentation()));

        // new converter
        var converter = new InstructionASTConverter();

        // convert all statements to Verilog code components
        if (ast.getRootnode().getType() == InstructionASTNodeType.PseudoInstructionNode)
            module.addChild(converter.convertInstruction(module, (PseudoInstructionASTNode) ast.getRootnode()));

        else
            module.addChild(new HardwareErrorMessage("Couldn't convert this instruction: " + inst.getRepresentation()));

        return new SingleInstructionModule(inst.getName(), moduletree);
    }
}
