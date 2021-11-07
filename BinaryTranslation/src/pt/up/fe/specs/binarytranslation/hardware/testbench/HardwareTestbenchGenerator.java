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

package pt.up.fe.specs.binarytranslation.hardware.testbench;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.AHardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.generation.AHardwareGenerator;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysAtBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.InitialBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ArrayDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.TimeScaleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.InputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.ImmediateReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.IndexSelection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.RangeSelection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.NotEqualsToExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.system_task.DisplayTask;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.system_task.FinishTask;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.system_task.ReadMemoryHexadecimalTask;


public class HardwareTestbenchGenerator extends AHardwareGenerator{

    public static AHardwareTestbench generate(AHardwareInstance module, int validationDataSize, String inputValidationFileName, String outputValidationFileName) {
       
        var testbenchtree = new VerilogModuleTree(module.getName() + "_tb");
        var testbench = testbenchtree.getModule();

        testbenchtree.getRoot().addChildLeftOf(testbench, new TimeScaleDeclaration());
        
        var loadNext = new InputPortDeclaration("loadNext", 1);
        testbenchtree.addDeclaration(loadNext);
        var verify = new InputPortDeclaration("verify", 1);
        testbenchtree.addDeclaration(verify);
        
        var index = testbench.addChild(new RegisterDeclaration("index", 32));
        
        // add modules to be tested ports
        
        var validation_inputs = testbench.addChild(new ArrayDeclaration(new RegisterDeclaration("inputs", 32 * module.getInputPorts().size()), validationDataSize));  // input validation array
        var validation_outputs = testbench.addChild(new ArrayDeclaration(new RegisterDeclaration("outputs", 32 * module.getOutputPorts().size()),validationDataSize));// output validation array
        
        var module_outputs = testbench.addChild(new WireDeclaration("moduleOutputs", 32 * module.getOutputPorts().size()));
        
        var initial_block = testbench.addChild(new InitialBlock()); // initial block for loading the validation array files
        initial_block.addChild(new ProceduralBlockingStatement(new VariableReference("index"), new ImmediateReference(0, 32)));
        initial_block.addChild(new ReadMemoryHexadecimalTask(inputValidationFileName, (ArrayDeclaration) validation_inputs)); // load input validation array
        initial_block.addChild(new ReadMemoryHexadecimalTask(outputValidationFileName, (ArrayDeclaration) validation_outputs));// load output validation array
        
        var always_block = testbench.addChild(new AlwaysAtBlock(new VariableReference(loadNext.getVariableName())));
        always_block.addChild(new ProceduralNonBlockingStatement(new VariableReference("index"), new AdditionExpression(new VariableReference("index") , new ImmediateReference(1, 32))));
        
        var alwaysBlockVerify = testbench.addChild(new AlwaysAtBlock(new VariableReference(verify.getVariableName())));
       
        var alwaysBlockVerify_failed = alwaysBlockVerify.addChild(
                new IfStatement(
                        new NotEqualsToExpression(
                                new VariableReference("moduleOutputs"), 
                                new IndexSelection(new VariableReference("outputs"), new VariableReference("index")
                                        )
                                )
                        )
                );
        
        alwaysBlockVerify_failed.addChild(new DisplayTask("Validation error"));
        alwaysBlockVerify_failed.addChild(new FinishTask());
        
        //always_block.addChild(new ContinuousStatement((VariableReference) iterator, new AdditionExpression((HardwareExpression) iterator, new ImmediateReference(1, 32))));
        
        List<HardwareNode> subInputs = new ArrayList<>();
        
        for(int i = 0; i < (module.getInputPorts().size() * 32); i = i + 32) {

            subInputs.add(new IndexSelection(new RangeSelection(new VariableReference("inputs"), i, i + 32), new VariableReference("index")));
        }
        
        for(int i = 0; i < (module.getOutputPorts().size() * 32); i = i + 32) {

            subInputs.add(new RangeSelection(new VariableReference("moduleOutputs"), i, i + 32));
        }
        
 
        
        var modulestatement = testbench.addChild(new ModuleStatement(module, "test", subInputs));
        

        return new HardwareTestbench("test_tb", testbenchtree);
    }
    
}
