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

package pt.up.fe.specs.binarytranslation.hardware.testbench;

import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.hardware.HardwareModule;
import pt.up.fe.specs.binarytranslation.hardware.generation.AHardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysFFBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.InitialBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.NegEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.PosEdge;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ArrayDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.TimeScaleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.NotEqualsToExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.ImmediateOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfElseStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleInstance;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.task.ReadMemoryHexadecimalTask;

public class HardwareTestbenchGenerator extends AHardwareGenerator {

    // private static final String verificationStartInputSignal = "verify";
    // private static final String verificationOutputSignal = "verifyResults";
    // private static final String validationInputs = "inputs";
    // private static final String validationOutputs = "outputs";
    // private static final String moduleOutputs = "moduleOutputs";
    // private static final String validationCurrentIndex = "index";

    public static HardwareTestbench generate(HardwareModule module, int validationDataSize,
            String inputValidationFileName, String outputValidationFileName) {

        /*
         * Top level
         */
        var testbenchtree = new VerilogModuleTree(module.getName() + "_tb");
        testbenchtree.addStatement(new TimeScaleDeclaration());

        /*
         * Registers which are ports, and their ports
         */
        var verificationStartInputSignal = new RegisterDeclaration("verify", 1);
        var verificationOutputSignal = new RegisterDeclaration("verifyResults", 1);
        var verify = new InputPortDeclaration(verificationStartInputSignal);
        var verificationResult = new OutputPortDeclaration(verificationOutputSignal);

        /*
         * Registers & Wires
         */
        var moduleOutputs = new WireDeclaration("moduleOutputs", 32 * module.getOutputPorts().size());
        var validationCurrentIndex = new RegisterDeclaration("validationCurrentIndex", 32);

        /*
         * Register arrays (TODO: correct??)
         */
        var validationInputs = new RegisterDeclaration("inputs", 32 * module.getInputPorts().size());
        var validationOutputs = new RegisterDeclaration("outputs", 32 * module.getOutputPorts().size());
        var validation_inputs = new ArrayDeclaration(validationInputs, validationDataSize);
        var validation_outputs = new ArrayDeclaration(validationOutputs, validationDataSize);

        /*
         * Add all to declaration block of toplevel
         */
        testbenchtree.addDeclaration(verify).addDeclaration(verificationResult).addDeclaration(validation_inputs)
                .addDeclaration(validation_outputs).addDeclaration(moduleOutputs)
                .addDeclaration(validationCurrentIndex);

        /*
         * Initial block
         */
        var initialblock = new InitialBlock();
        testbenchtree.addStatement(initialblock);
        // initial block for loading the validation array files

        var immediate0_32 = ImmediateOperator.Zeroes(32);
        var block1 = new ProceduralBlockingStatement(validationCurrentIndex.getReference(), immediate0_32);
        initialblock.addChild(block1);

        // TODO: "inputValidationFileName" should be a File (already verified to exist!) and NOT a string
        var stat2 = new ReadMemoryHexadecimalTask(inputValidationFileName, validation_inputs);
        initialblock.addChild(stat2);
        // load input validation array

        // TODO: "outputValidationFileName" should be a File (already verified to exist!) and NOT a string
        var stat3 = new ReadMemoryHexadecimalTask(outputValidationFileName, validation_outputs);
        initialblock.addChild(stat3);
        // load output validation array

        var immediate0_1 = ImmediateOperator.Zeroes(1);
        var block2 = new ProceduralBlockingStatement(verificationOutputSignal.getReference(), immediate0_1);
        initialblock.addChild(block2);

        /*
         * Always block
         */
        var posedge1 = new PosEdge(verificationStartInputSignal.getReference());
        var alwaysblock1 = new AlwaysFFBlock(posedge1); // TODO: AlwaysFFBlock.atPosEdge(signal) --> static method?
        testbenchtree.addStatement(alwaysblock1);

        var immediate1_32 = ImmediateOperator.Ones(32);
        var addition1 = new AdditionExpression(validationCurrentIndex.getReference(), immediate1_32);
        var stat4 = new ProceduralNonBlockingStatement(validationCurrentIndex.getReference(), addition1);
        alwaysblock1.addChild(stat4);

        /*
         * Always block
         */
        var negedge1 = new NegEdge(verificationStartInputSignal.getReference());
        var alwaysblock2 = new AlwaysFFBlock(negedge1); // TODO: AlwaysFFBlock.atNegEdge(signal) --> static method?

        var immediate1_1 = ImmediateOperator.Ones(1);

        var ref1 = validationOutputs.getReference();
        var index1 = validationCurrentIndex.getReference();
        ref1.addSubscript(index1).addSubscript(0, 32);

        // TODO: factory methods like "Verilog.alwaysFF.atPosEdge(signal)... etc"

        // TODO: method "if.notEquals().else()" etc

        var notEqual = new NotEqualsToExpression(moduleOutputs.getReference(), ref1);
        var ifelse = new IfElseStatement(notEqual);
        var ifstat1 = new ProceduralNonBlockingStatement(verificationOutputSignal.getReference(), immediate0_1);
        var elsestat1 = new ProceduralNonBlockingStatement(verificationOutputSignal.getReference(), immediate1_1);
        ifelse.addIfStatement(ifstat1).addElseStatement(elsestat1);

        var subInputs = new ArrayList<HardwareNode>();

        for (int i = 0; i < (module.getInputPorts().size() * 32); i = i + 32) {
            var ref = validationInputs.getReference();
            var idx = validationCurrentIndex.getReference();
            ref.addSubscript(idx).addSubscript(i, i + 32);
            subInputs.add(ref);

            /*subInputs.add(new RangedSelection(new IndexedSelection(new VariableOperator(validationInputs),
                    new VariableOperator(validationCurrentIndex)), i, i + 32));*/
        }

        for (int i = 0; i < (module.getOutputPorts().size() * 32); i = i + 32) {
            var ref = moduleOutputs.getReference();
            subInputs.add(ref.addSubscript(i, i + 32));

            // subInputs.add(new RangedSelection(new VariableOperator(moduleOutputs), i, i + 32));
        }

        /*
         * Instantiate the Design Under Test (DUT)
         */
        testbenchtree.addStatement(new ModuleInstance(module, module.getName() + "_test", subInputs));

        return new HardwareTestbench(module.getName() + "_tb", testbenchtree);
    }
}