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

package org.specs.Riscv.test.instruction.cdfg;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import org.specs.Riscv.instruction.RiscvInstruction;
import org.specs.Riscv.instruction.RiscvPseudocode;

import pt.up.fe.specs.binarytranslation.hardware.AHardwareInstance;
import pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.wip.InstructionCDFGCustomInstructionUnitGenerator;
import pt.up.fe.specs.binarytranslation.hardware.analysis.timing.TimingAnalysisRun;
import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareFolderGenerator;
import pt.up.fe.specs.binarytranslation.hardware.testbench.HardwareTestbenchGenerator;
import pt.up.fe.specs.binarytranslation.hardware.testbench.VerilatorRun;
import pt.up.fe.specs.binarytranslation.hardware.testbench.VerilatorTestbenchGenerator;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.PseudoInstructionCalculatorGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.dot.InstructionCDFGDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.generator.InstructionCDFGGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.resolve_names.InstructionCDFGNameResolver;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionLexer;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;
import pt.up.fe.specs.crispy.ast.statement.ModuleInstance;

public class RiscvCDFGGenerationTest {

    public PseudoInstructionContext getParseTree(String pseudocode) {
        var parser = new PseudoInstructionParser(new CommonTokenStream(new PseudoInstructionLexer(new ANTLRInputStream(pseudocode))));
        return parser.pseudoInstruction();
    }
    
    private String systemPath = "C:\\Users\\joaom\\Desktop\\" + "btf_riscv_test" ;
    private String wslPath = "/mnt/c/Users/joaom/Desktop/" + "btf_riscv_test";
    
    private InstructionCDFG icdfg;
    
    private ModuleInstance generatedModule;
   
    private RiscvPseudocode instructionPseudoCode = RiscvPseudocode.add;
    private String instructionName = "";
    
    private final int moduleTestbenchSamples = 1000;
    
    public void generateFolderStructures() {
        HardwareFolderGenerator.generate(systemPath);
    }

    public void generateInstructionCDFG() {
        System.out.print("Generating InstructionCDFG...");
        this.icdfg = (new InstructionCDFGGenerator()).generate(this.instructionPseudoCode.getParseTree());
        System.out.println("\tDONE");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void exportInstructionCDFGAsDOT() throws IOException {
        System.out.print("Exporting InstructionCDFG as DOT...");
        InstructionCDFGDOTExporter exp = new InstructionCDFGDOTExporter();
        Writer writer = new StringWriter();
        exp.exportGraph((GeneralFlowGraph)this.icdfg, this.instructionName, writer);
        System.out.println("\t" + InstructionCDFGDOTExporter.generateGraphURL(writer.toString()));
        InstructionCDFGDOTExporter.generateGraphvizFile(HardwareFolderGenerator.getBTFFolder(systemPath), this.instructionName, writer.toString());
        exp.emit((GeneralFlowGraph)this.icdfg, this.instructionName, writer, HardwareFolderGenerator.newFile(HardwareFolderGenerator.getBTFFolder(systemPath), this.instructionName, "dot"));
        
    }

    public void resolveInstructionCDFGNames() {
        InstructionCDFGNameResolver.resolve(icdfg);
    }
    
    public void generateHardwareModule() throws IOException {
        System.out.print("Generating HW module...");
        InstructionCDFGCustomInstructionUnitGenerator moduleGenerator = new InstructionCDFGCustomInstructionUnitGenerator();
        this.generatedModule = moduleGenerator.generateHardware(icdfg, this.instructionName);
        this.generatedModule.emit(HardwareFolderGenerator.newHardwareHDLFile(systemPath, this.generatedModule.getName(), "sv"));
        
        System.out.println("\tDONE");
    }
    
    public void generateHardwareModuleValidationData() throws IOException {
        PseudoInstructionCalculatorGenerator validation = new PseudoInstructionCalculatorGenerator();
        Map<Map<String, Number>, Map<String, Number>> validationData = PseudoInstructionCalculatorGenerator.generateValidationData(instructionPseudoCode.getParseTree(), icdfg.getDataInputsReferences(), moduleTestbenchSamples);

        System.out.print("Generating HW module testbench validation input memory file...");
        HardwareFolderGenerator.newHardwareTestbenchFile(systemPath, "input", "mem").write(PseudoInstructionCalculatorGenerator.generateHexMemFile(validationData.keySet()).getBytes());
        System.out.println("\tDONE");
        
        System.out.print("Generating HW module testbench validation output memory file...");
        HardwareFolderGenerator.newHardwareTestbenchFile(systemPath, "output", "mem").write(PseudoInstructionCalculatorGenerator.generateHexMemFile(validationData.values()).getBytes());
        System.out.println("\tDONE");
    }
    
    public void generateHardwareModuleTestbench() throws IOException {
        System.out.print("Generating HW module testbench...");
        HardwareTestbenchGenerator.generate(generatedModule, moduleTestbenchSamples,  "input.mem", "output.mem").emit(HardwareFolderGenerator.newHardwareTestbenchFile(systemPath, generatedModule.getName() + "_tb", "sv"));
        System.out.println("\tDONE");
    }
    
    public void generateVerilatorTestbench() throws IOException {
        System.out.print("Generating HW module Verilator testbench...");
        VerilatorTestbenchGenerator.emit(HardwareFolderGenerator.newHardwareTestbenchFile(systemPath, this.instructionName + "_tb", "cpp"), generatedModule.getName(), moduleTestbenchSamples);
        System.out.println("\tDONE");
    }
    
    public boolean runVerilatorTestbench() throws IOException {
        System.out.print("Running Verilator testbench...");
        if(!VerilatorRun.start(wslPath+"/hw/tb", this.instructionName)) {
            System.out.println("ERROR: Validation failed !!!");
            return false;
        }
        return true;
    }
    
    public void performIcetimeTimingAnalysis() throws IOException {
        System.out.print("Performing timing analysis (icetime) on generated module...");
        TimingAnalysisRun.start(wslPath, this.instructionName);
        System.out.println("\tDONE");
    }
    
    @Test
    public void testFullFlow() throws IOException {
        
        String syspath = this.systemPath;
        String wslpath = this.wslPath;
        
        System.out.println(RiscvPseudocode.values().length);
        
        for(var instructionpc : RiscvPseudocode.values()) {
            
            System.out.println("\n\nINFO: PERFORMING FULL FLOW ON INSTRUCTION " + instructionpc.getName() + "\n");
            
            try {
            this.instructionPseudoCode = instructionpc;
            this.instructionName = instructionpc.getName();
            this.systemPath = syspath + "\\" + this.instructionName;
            this.wslPath = wslpath + "/" + this.instructionName;
            
            this.generateFolderStructures();
            this.generateInstructionCDFG();
            this.exportInstructionCDFGAsDOT();
            this.generateHardwareModule();
       
            this.generateHardwareModuleValidationData();
            this.generateHardwareModuleTestbench();
            this.generateVerilatorTestbench();
            if(!this.runVerilatorTestbench())
                throw new Exception();
            this.performIcetimeTimingAnalysis();
            
            }catch(Exception e) {
                System.out.println("ERROR: unable to perform full flow on instruction " + instructionpc.getName());
            }
        }
    }
   
}
