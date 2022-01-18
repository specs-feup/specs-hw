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

import pt.up.fe.specs.binarytranslation.hardware.generation.AHardwareGenerator;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.declaration.ArrayDeclaration;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;
import pt.up.fe.specs.crispy.ast.task.ReadMemoryHexadecimalTask;

public class HardwareTestbenchGenerator extends AHardwareGenerator {

    public static HardwareModule generate(HardwareModule module, int validationDataSize,
            File inputValidationFile, File outputValidationFile) throws NoSuchFileException {

        if (!inputValidationFile.exists()) {
            throw new NoSuchFileException("File "
                    + inputValidationFile.getName() + " does not exist!");
        }

        if (!outputValidationFile.exists()) {
            throw new NoSuchFileException("File "
                    + outputValidationFile.getName() + " does not exist!");
        }

        /*
         * Top level wrapper for verilator compliance (??)
         * (Dynamically generated module, i.e., not from defined class)
         */
        var testbenchModule = new HardwareModule(module.getName() + "_tb");

        /*
         * Ports
         */
        var verifStart = testbenchModule.addInputPort("verify", 1);
        var verifOutput = testbenchModule.addOutputRegisterPort("verifyResults", 1);

        /*
         * Registers
         */
        var index = testbenchModule.addRegister("index", 32);
        
        var inputs = testbenchModule.addArray(
                ArrayDeclaration.ofRegisters("inputs",
                        32 * module.getInputPorts().size(), validationDataSize));
        var outputs = testbenchModule.addArray(
                ArrayDeclaration.ofRegisters("outputs",
                        32 * module.getOutputPorts().size(), validationDataSize));
        
        /*
         * Wires
         */
        var moduleOutputs = testbenchModule.addWire("moduleOutputs", 32 * module.getOutputPorts().size());

        /*
         * Initial block
         */
        testbenchModule.initial("")
                ._do(index.blocking(0))
                ._do(new ReadMemoryHexadecimalTask(inputValidationFile.getName(), inputs))
                ._do(new ReadMemoryHexadecimalTask(outputValidationFile.getName(), outputs))
                ._do(verifOutput.blocking(0));

        /*
         * Always block
         */
        testbenchModule.alwaysposedge("block0", verifStart)
                ._do(index.nonBlocking(index.add(1)));

        /*
         * Always block
         */
        testbenchModule.alwaysnegedge("block1", verifStart)
                ._ifelse(moduleOutputs.noteq(outputs.idx(index)))
                .then()._do(verifOutput.nonBlocking(0))
                .orElse()._do(verifOutput.nonBlocking(1));

        var subInputs = new ArrayList<HardwareOperator>();

        for (int i = 0; i < (module.getInputPorts().size() * 32); i = i + 32)
            subInputs.add(inputs.idx(index).idx(i + 31, i));

        for (int i = 0; i < (module.getOutputPorts().size() * 32); i = i + 32)
            subInputs.add(moduleOutputs.idx(i + 31, i));

        /*
         * Place the Design Under Test (DUT) into the body of this wrapper
         */
        testbenchModule.instantiate(module, subInputs);

        return testbenchModule;
        // return new HardwareTestbench(module.getName() + "_tb", testbenchModule);
    }
}