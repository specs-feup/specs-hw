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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.InitialBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs.AlwaysBlock;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.RegisterArrayDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.TimeScaleDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.DelayDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.system_task.ReadMemoryHexadecimalTask;


public class HardwareTestbenchGenerator {

    public static AHardwareTestbench generate(AHardwareInstance module) {
       
        var testbenchtree = new VerilogModuleTree("test");
        var testbench = testbenchtree.getModule();

        testbench.addChild(new TimeScaleDeclaration());
        
        // add modules to be tested ports
        
        
        var validation_inputs = testbench.addChild(new RegisterArrayDeclaration("inputs", 0, 0));
        var validation_outputs = testbench.addChild(new RegisterArrayDeclaration("outputs", 0, 0));
        
        var initial_block = testbench.addChild(new InitialBlock());
        initial_block.addChild(new ReadMemoryHexadecimalTask(null, validation_inputs));
        
        
        var always_block = testbench.addChild(new AlwaysBlock());
        
        
        always_block.addChild(new DelayDeclaration(null));
        
        
        testbench.addChild(new ModuleStatement(module, "test"));
        
        
        return null;
    }
    
}
