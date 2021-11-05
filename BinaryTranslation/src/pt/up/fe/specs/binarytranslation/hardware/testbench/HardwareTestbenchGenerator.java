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

import pt.up.fe.specs.binarytranslation.hardware.AHardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.tree.VerilogModuleTree;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.InitialBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.IntegerDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterArrayDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.TimeScaleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.ImmediateReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.VariableReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ContinuousStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.DelayDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.system_task.ReadMemoryHexadecimalTask;


public class HardwareTestbenchGenerator {

    public static AHardwareTestbench generate(AHardwareInstance module, String inputValidationFileName, String outputValidationFileName) {
       
        var testbenchtree = new VerilogModuleTree("test");
        var testbench = testbenchtree.getModule();

        testbench.addChild(new TimeScaleDeclaration());
        
        // add modules to be tested ports
        
        
        var validation_inputs = testbench.addChild(new RegisterArrayDeclaration("inputs", 0, 0));  // input validation array
        var validation_outputs = testbench.addChild(new RegisterArrayDeclaration("outputs", 0, 0));// output validation array
        var clock = testbench.addChild(new RegisterDeclaration("clock", 1));    // clock for latching input (i dont think its needed)
        
        var iterator = testbench.addChild(new IntegerDeclaration("i")); // integer for iterating over the validation arrays
        
        var initial_block = testbench.addChild(new InitialBlock()); // inital block for loading the validation array files
        initial_block.addChild(new ReadMemoryHexadecimalTask(inputValidationFileName, validation_inputs)); // load input validation array
        initial_block.addChild(new ReadMemoryHexadecimalTask(outputValidationFileName, validation_outputs));// load output validation array
        
        var always_block = testbench.addChild(new AlwaysBlock());
        
        always_block.addChild(new DelayDeclaration(10));
        
        always_block.addChild(new DelayDeclaration(100));
        always_block.addChild(new ContinuousStatement((VariableReference) iterator, new AdditionExpression((HardwareExpression) iterator, new ImmediateReference(1, 32))));
        
        
        //testbench.addChild(new ModuleStatement(module, "test"));
        
        
        return null;
    }
    
}
