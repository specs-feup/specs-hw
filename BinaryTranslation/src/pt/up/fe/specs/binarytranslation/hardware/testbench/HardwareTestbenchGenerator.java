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
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port.OutputPortDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.comparison.NotEqualsToExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.ImmediateReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.VariableReference;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.selection.IndexSelection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.selection.RangeSelection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.signal_change.NegedgeSignalChange;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.signal_change.PosedgeSignalChange;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfElseStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ModuleStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.system_task.ReadMemoryHexadecimalTask;

public class HardwareTestbenchGenerator extends AHardwareGenerator{

    private static final String verificationStartInputSignal = "verify"; 
    private static final String verificationOutputSignal = "verifyResults";
    private static final String validationInputs = "inputs";
    private static final String validationOutputs = "outputs";
    private static final String moduleOutputs = "moduleOutputs";
    private static final String validationCurrentIndex = "index";
    
    public static AHardwareTestbench generate(AHardwareInstance module, int validationDataSize, String inputValidationFileName, String outputValidationFileName) {
       
        var testbenchtree = new VerilogModuleTree(module.getName() + "_tb");
        var testbench = testbenchtree.getModule();

        new TimeScaleDeclaration(testbenchtree);
 
        var verify = new InputPortDeclaration(new RegisterDeclaration(verificationStartInputSignal, 1), testbenchtree);
        var verificationResult = new OutputPortDeclaration(new RegisterDeclaration(verificationOutputSignal, 1), testbenchtree);
        
        var index = testbench.addChild(new RegisterDeclaration(validationCurrentIndex, 32));

        // add modules to be tested ports
        
        var validation_inputs = testbench.addChild(new ArrayDeclaration(new RegisterDeclaration(validationInputs, 32 * module.getInputPorts().size()), validationDataSize));  // input validation array
        var validation_outputs = testbench.addChild(new ArrayDeclaration(new RegisterDeclaration(validationOutputs, 32 * module.getOutputPorts().size()),validationDataSize));// output validation array
        
        var module_outputs = testbench.addChild(new WireDeclaration(moduleOutputs, 32 * module.getOutputPorts().size()));
        
        var initial_block = testbench.addChild(new InitialBlock()); // initial block for loading the validation array files
        initial_block.addChild(new ProceduralBlockingStatement(new VariableReference(validationCurrentIndex), new ImmediateReference(0, 32)));
        initial_block.addChild(new ReadMemoryHexadecimalTask(inputValidationFileName, (ArrayDeclaration) validation_inputs)); // load input validation array
        initial_block.addChild(new ReadMemoryHexadecimalTask(outputValidationFileName, (ArrayDeclaration) validation_outputs));// load output validation array
        initial_block.addChild((new ProceduralBlockingStatement(new VariableReference(verificationOutputSignal), new ImmediateReference(0, 1))));
        
        var always_block = testbench.addChild(new AlwaysAtBlock(new PosedgeSignalChange(new VariableReference(verificationStartInputSignal))));
        always_block.addChild(new ProceduralNonBlockingStatement(new VariableReference(validationCurrentIndex), new AdditionExpression(new VariableReference(validationCurrentIndex) , new ImmediateReference(1, 32))));
        
        var alwaysBlockVerify = testbench.addChild(new AlwaysAtBlock(new NegedgeSignalChange(new VariableReference(verificationStartInputSignal))));
       
        var alwaysBlockVerify_failed = alwaysBlockVerify.addChild(
                new IfElseStatement(new NotEqualsToExpression(
                        new VariableReference(moduleOutputs), 
                        new RangeSelection (new IndexSelection(new VariableReference(validationOutputs), new VariableReference(validationCurrentIndex)), 0, 32))
                        )
                .addIfStatement(new ProceduralNonBlockingStatement(new VariableReference(verificationOutputSignal), new ImmediateReference(0, 1)))
                .addElseStatement(new ProceduralNonBlockingStatement(new VariableReference(verificationOutputSignal), new ImmediateReference(1, 1)))
                );

        List<HardwareNode> subInputs = new ArrayList<>();
        
        for(int i = 0; i < (module.getInputPorts().size() * 32); i = i + 32) 
            subInputs.add(new RangeSelection (new IndexSelection(new VariableReference(validationInputs), new VariableReference(validationCurrentIndex)), i, i + 32));
        
        for(int i = 0; i < (module.getOutputPorts().size() * 32); i = i + 32) 
            subInputs.add(new RangeSelection(new VariableReference(moduleOutputs), i, i + 32));

        new ModuleStatement(module, module.getName() + "_test", subInputs, testbench);

        return new HardwareTestbench(module.getName() + "_tb", testbenchtree);
    }
    
}
