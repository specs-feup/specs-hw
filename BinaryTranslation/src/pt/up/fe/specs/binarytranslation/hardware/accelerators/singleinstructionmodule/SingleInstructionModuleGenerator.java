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

import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareGenerationUtils;
import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.tree.HardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.tree.ModuleHeader;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareRootNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModuleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.PortDeclaration;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.ast.InstructionAST;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.PseudoInstructionASTNode;

public class SingleInstructionModuleGenerator implements HardwareGenerator {

    public SingleInstructionModuleGenerator() {
        // TODO: add generation parameters
    }

    /**
     * 
     * @param inst
     *            The instruction thats being used for generation
     */
    @Override
    public HardwareInstance generateHarware(Instruction inst) {

        // Information we can only grasp from the instruction encoding //////////////////////////
        var root = new HardwareRootNode();
        var header = new ModuleHeader();
        root.addChild(header);

        var module = new ModuleDeclaration(inst.getName());
        root.addChild(module);

        // build all ports, based on operands
        var instOperands = inst.getData().getOperands();
        for (var op : instOperands) {

            var name = op.getRepresentation();
            var width = op.getProperties().getWidth();

            if (op.isImmediate())
                continue;

            else if (op.isRead())
                module.addChild(new PortDeclaration(name, width, ModulePortDirection.input));

            else if (op.isWrite())
                module.addChild(new PortDeclaration(name, width, ModulePortDirection.output));
        }

        // Information we can only grasp from the pseudocode (i.e. instruction implementation) //

        // get AST of instruction
        var ast = new InstructionAST(inst);

        // convert ASM names to concrete register names and immediate values
        // HardwareGenerationUtils.convertOperandsToConcrete(ast);

        // TODO: create transformation passes over the AST to replace the OperandASTNodes into
        // more concrete data?
        // specifically, I need to attach field information to the field names
        // e.g., a register "RA" in the pseudocode is a 32bit register, which
        // in a specific execution is called "r3"
        // This information is held in the "MicroBlazeOperand" class (and siblings)
        // via the AInstructionProperties family of classes

        // convert all statements to Verilog code components
        for (var statement : ((PseudoInstructionASTNode) ast.getRootnode()).getStatements()) {

            // from AST
            // var target = statement.getTarget();
            // var expr = statement.getExpr();

            // to hardware language tree
            // var varref = new VariableReference(target.getOperandName());
            // var hwexpression = HardwareGenerationUtils.convertASTStatement(expr);

            // TODO: this conversion could/should result in a generic HardwareNode
            // e.g. the division operator in an expression should be implemented as a module
            // or an expression can contain other expressions as operands

            module.addChild(HardwareGenerationUtils.convertASTStatement(statement));
        }

        return new SingleInstructionModule(root);
    }

    // TODO: instantiate a walker, and give it the instance of the HardwareInstance, and the InstructionAST, after
    // the prologue of the HardwareInstace tree has been built?

    /*
    // The list of components
    List<HardwareComponent> components = new ArrayList<HardwareComponent>();
    components.add(new ModuleHeader(inst));
    
    // module declaration
    components.add(new PlainCode("module " + inst.getClass().getTypeName().toString() + "_" + inst.getName()
            + inst.getInstruction() + ";"));
    
    // build all ports, based on operands
    var instOperands = inst.getData().getOperands();
    for (var op : instOperands) {
        if (!op.isImmediate())
            components.add(PortDeclaration.newPort(op)); // TODO: how to create output register types??
    }
    
    // get AST of instruction
    var ast = new InstructionAST(inst);
    
    // TODO: a visitor which replaces asm fields with operand names
    
    // then a visitor which adds subscripts to the fields?
    
    // get tokens that are ASM FIELDS, as Strings
    // I need this to know which asmfields are which operands
    // var fields = PseudoInstructionGetters.getAsmFields(tree);
    
    // build mapping between asmFieldNames and Verilog signal names here??
    // TODO
    
    // TODO make a visitor like getResultRegister, getOperation, getLOperand, etc??
    // do this per statement rule?
    
    // start block
    components.add(new PlainCode("always_comb begin"));
    
    // for every statement in the instruction
    for (var s : tree.statement()) {
        // the expression!
        components.add(HardwareGenerationUtils.generateAssignStatement(inst, s));
    }
    components.add(new PlainCode("end"));
    components.add(new PlainCode("endmodule"));
    */

    // find operator
    /* String statmentOperator = null;
    for (var child : expr.children) {
        if (child instanceof OperatorContext) {
            statmentOperator = child.getText();
            break;
        }
    }*/
    // TODO: replace this with other action, especially if operator is divisor

    // find operands
    /*int i = 0;
    OperandContext[] opCtx = { null, null };
    for (var child : expr.children) {
    
        // Expression could be a OperandContext, which is
        // either a NumberContext or AsmFieldContext
        if (child instanceof ExpressionContext) {
            opCtx[i] = (OperandContext) child.getChild(0);
        }
    }*/

    /*
    var grandchild = child.getChild(0);
    var aux = getOperandByAsmField(instOperands, grandchild.getText());
    
    if (grandchild instanceof OperandContext) {
        exprString += cleaner(aux.getRepresentation());
    }
    
    else if (child instanceof NumberContext) {
        exprString += aux.getRepresentation().replace("0x",
                (aux.getProperties().getWidth() - 1) + "\'");
    }*/

    // TODO: new AssignmentStatement(target, source) - variable types??

    // var nodes = PseudoInstructionGetters.getAllNodes(s);
}
