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

import pt.up.fe.specs.binarytranslation.hardware.HardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.generation.visitors.HardwareStatementGenerator;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysCombBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.PortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.meta.HardwareCommentNode;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.PseudoInstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.passes.ApplyInstructionPass;

public class SingleInstructionModuleGenerator implements HardwareGenerator {

    public SingleInstructionModuleGenerator() {
        // TODO: add generation parameters
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
    public HardwareInstance generateHarware(Instruction inst) {

        // The VerilogModuleTree
        var moduletree = new VerilogModuleTree(inst.getName());
        var module = moduletree.getModule();

        // Information we can only grasp from the instruction encoding //////////////////////////

        // build all ports, based on operands
        var instOperands = inst.getData().getOperands();
        for (var op : instOperands) {

            var name = op.getRepresentation();
            var width = op.getProperties().getWidth();

            if (op.isImmediate())
                continue;

            else if (op.isRead())
                moduletree.addDeclaration(new PortDeclaration(name, width, ModulePortDirection.input));

            else if (op.isWrite())
                moduletree.addDeclaration(new PortDeclaration(name, width, ModulePortDirection.output));
        }

        // Information we can only grasp from the pseudocode (i.e. instruction implementation) //

        // get AST of instruction
        var ast = new InstructionAST(inst);

        // TODO: a first pass which replaces AST nodes with info frmo the instruction, specifically,
        // replaces OperandASTNode with LIveinNode,Liveout,internal internal extends to -> variablenode and
        // immediatenode

        // Apply first pass (attaches executed instruction information to the AST of the instruction)
        ast.accept(new ApplyInstructionPass());

        // convert all statements to Verilog code components
        var generator = new HardwareStatementGenerator();
        var statements = ((PseudoInstructionASTNode) ast.getRootnode()).getStatements();

        // add comment
        module.addChild(new HardwareCommentNode("implementation for instruction: " + inst.getRepresentation()));

        // needs an always_comb block
        if (statements.size() > 1) {
            var block = new AlwaysCombBlock();
            module.addChild(block);
            for (var statement : statements) {
                block.addChild(generator.generateBlocking(statement));
            }
        }

        // else use a single assign statement
        else
            module.addChild(generator.generateAssign(statements.get(0)));

        // from AST
        // var target = statement.getTarget();
        // var expr = statement.getExpr();

        // to hardware language tree
        // var varref = new VariableReference(target.getOperandName());
        // var hwexpression = HardwareGenerationUtils.convertASTStatement(expr);

        // TODO: this conversion could/should result in a generic HardwareNode
        // e.g. the division operator in an expression should be implemented as a module
        // or an expression can contain other expressions as operands

        // root.addChild(HardwareGenerationUtils.convertASTStatement(statement));

        return new SingleInstructionModule(inst.getName(), moduletree);
    }

    //////////////

}
