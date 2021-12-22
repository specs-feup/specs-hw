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

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

import pt.up.fe.specs.binarytranslation.hardware.HardwareArchitecture;
import pt.up.fe.specs.binarytranslation.hardware.generation.AHardwareGenerator;
import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.constructs.AlwaysFFBlock;
import pt.up.fe.specs.crispy.ast.constructs.NegEdge;
import pt.up.fe.specs.crispy.ast.constructs.PosEdge;
import pt.up.fe.specs.crispy.ast.declaration.ArrayDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.RegisterDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.TimeScaleDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.WireDeclaration;
import pt.up.fe.specs.crispy.ast.definition.HardwareModule;
import pt.up.fe.specs.crispy.ast.definition.HardwareTestbench;
import pt.up.fe.specs.crispy.ast.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.crispy.ast.expression.comparison.NotEqualsToExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.statement.IfElseStatement;
import pt.up.fe.specs.crispy.ast.statement.ModuleInstance;
import pt.up.fe.specs.crispy.ast.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.crispy.ast.statement.ProceduralNonBlockingStatement;
import pt.up.fe.specs.crispy.ast.task.ReadMemoryHexadecimalTask;


public class HardwareTestbenchGenerator extends AHardwareGenerator {

     private static final String verificationStartInputSignal = "verify";
     private static final String verificationOutputSignal = "verifyResults";
    // private static final String validationInputs = "inputs";
    // private static final String validationOutputs = "outputs";
    // private static final String moduleOutputs = "moduleOutputs";
    // private static final String validationCurrentIndex = "index";

    public static HardwareTestbench generate(HardwareArchitecture module, int validationDataSize, File inputValidationFile, File outputValidationFile) throws NoSuchFileException{
        
        if(!inputValidationFile.exists()) {
            throw new NoSuchFileException("File " + inputValidationFile.getName() + " does not exist!");
        }
        
        if(!outputValidationFile.exists()) {
            throw new NoSuchFileException("File " + outputValidationFile.getName() + " does not exist!");
        }
        
        /*
         * Top level
         */
        var testbenchModule = new HardwareModule(module.getName() + "_tb");
        testbenchModule.addChild(new TimeScaleDeclaration());

        /*
         * Registers which are ports, and their ports
         */

        var verificationStartInputSignal = testbenchModule.addInputPort(HardwareTestbenchGenerator.verificationStartInputSignal, 1);
        var verificationOutputSignal  = testbenchModule.addOutputPort(HardwareTestbenchGenerator.verificationOutputSignal, 1);
        
        /*
         * Registers & Wires
         */
        var moduleOutputs = testbenchModule.addWire(new WireDeclaration("moduleOutputs", 32 * module.getOutputPorts().size()));
        var validationCurrentIndex = testbenchModule.addRegister(new RegisterDeclaration("validationCurrentIndex", 32));

        /*
         * Register arrays (TODO: correct??)
         */
        var validationInputs = new RegisterDeclaration("inputs", 32 * module.getInputPorts().size());
        var validationOutputs = new RegisterDeclaration("outputs", 32 * module.getOutputPorts().size());
        var validation_inputs = new ArrayDeclaration(validationInputs, validationDataSize);
        var validation_outputs = new ArrayDeclaration(validationOutputs, validationDataSize);

        /*
         * Initial block
         */
        
        var initialBlock = testbenchModule.initial("");

        // initial block for loading the validation array files
        
        initialBlock.addChild(new ProceduralBlockingStatement(validationCurrentIndex, new Immediate(0, 32)));
        initialBlock.addChild(new ReadMemoryHexadecimalTask(inputValidationFile.getName(), validation_inputs));
        initialBlock.addChild(new ReadMemoryHexadecimalTask(outputValidationFile.getName(), validation_outputs));
        initialBlock.addChild(new ProceduralBlockingStatement(verificationOutputSignal, new Immediate(0, 1)));

        /*
         * Always block
         */

        testbenchModule.addBlock(new AlwaysFFBlock(new PosEdge(verificationStartInputSignal)))
        .addStatement(new ProceduralNonBlockingStatement(validationCurrentIndex, new AdditionExpression(validationCurrentIndex, new Immediate(0xFFFFFFFF, 32))));

        /*
         * Always block
         */
        var negedge1 = new NegEdge(verificationStartInputSignal);
        var alwaysblock2 = new AlwaysFFBlock(negedge1); // TODO: AlwaysFFBlock.atNegEdge(signal) --> static method?


        var ref1 = validationOutputs.getReference();
        ref1.addSubscript(validationCurrentIndex).addSubscript(0, 32);

        // TODO: factory methods like "Verilog.alwaysFF.atPosEdge(signal)... etc"

        // TODO: method "if.notEquals().else()" etc
        
        testbenchModule._ifelse(new NotEqualsToExpression(moduleOutputs, ref1))
            .then(new ProceduralNonBlockingStatement(verificationOutputSignal, new Immediate(0, 1)))
            .orElse(new ProceduralNonBlockingStatement(verificationOutputSignal, new Immediate(1, 1)));
        
        var subInputs = new ArrayList<HardwareNode>();

        for (int i = 0; i < (module.getInputPorts().size() * 32); i = i + 32) {
            var ref = validationInputs.getReference();
            ref.addSubscript(validationCurrentIndex).addSubscript(i, i + 32);
            subInputs.add(ref);

            /*subInputs.add(new RangedSelection(new IndexedSelection(new VariableOperator(validationInputs),
                    new VariableOperator(validationCurrentIndex)), i, i + 32));*/
        }

        for (int i = 0; i < (module.getOutputPorts().size() * 32); i = i + 32) {
            subInputs.add(moduleOutputs.addSubscript(i, i + 32));

            // subInputs.add(new RangedSelection(new VariableOperator(moduleOutputs), i, i + 32));
        }

        /*
         * Instantiate the Design Under Test (DUT)
         */
        
        testbenchModule.addInstance(new ModuleInstance(module, module.getName() + "_test", subInputs));

        return new HardwareTestbench(module.getName() + "_tb", testbenchModule);
    }
}