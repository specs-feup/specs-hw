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

package pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.wip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.up.fe.specs.binarytranslation.hardware.generation.visitors.wip.InstructionCDFGConverter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.InputPort;
import pt.up.fe.specs.crispy.ast.expression.operator.OutputPort;

public class InstructionCDFGCustomInstructionUnitGenerator{

    private HardwareModule module;
    private String moduleName;
    
    private InstructionCDFG icdfg;
    
    private Set<InputPort> inputPorts;
    private Set<OutputPort> outputPorts;
    
    public InstructionCDFGCustomInstructionUnitGenerator(InstructionCDFG icdfg, String moduleName) {
        
        this.icdfg = icdfg;

        this.moduleName = moduleName;
        
        this.inputPorts = new HashSet<>();
        this.outputPorts = new HashSet<>();
        
    }
    
    public InstructionCDFG getInstructionCDFG() {
        return this.icdfg;
    }
    
    public HardwareModule getHardwareModule() {
        return this.module;
    }
    
    public String getHardwareModuleName() {
        return this.moduleName;
    }
    
    public Set<InputPort> getInputPorts(){
        return this.inputPorts;
    }
    
    public Set<OutputPort> getOutputPorts(){
        return this.outputPorts;
    }
    
    private void initializeModule() {
        
        this.module = new InstructionCDFGCustomInstructionUnit(moduleName);
        
        this.inputPorts.clear();
        this.outputPorts.clear();
        
    }
    
    private void generatePorts() {
        
        this.icdfg.getDataInputsNames()
            .stream()
            .filter(name -> !this.icdfg.getDataOutputsNames().contains(name))
            .forEach(inputPortName -> this.inputPorts.add(this.module.addInputPort(inputPortName, 32)));      
        
        this.icdfg.getDataOutputsNames().forEach(outputPortName -> this.outputPorts.add(this.module.addOutputRegisterPort(outputPortName, 32)));
        
    }
    
    private void generateLogic(HardwareNode blockHardwareNode) {
        InstructionCDFGConverter.convert(icdfg, this.module, blockHardwareNode);
    }
    
    public HardwareModule generateHardware() {
        
        this.icdfg.refresh();
        this.initializeModule();
        this.generatePorts();
        this.generateLogic(this.module.alwayscomb());

        return this.module;
    }

}
