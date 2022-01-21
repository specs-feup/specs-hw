/**
 *  Copyright 2022 SPeCS.
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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.fullflow;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.ExecutionException;

import pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.wip.InstructionCDFGCustomInstructionUnitGenerator;
import pt.up.fe.specs.binarytranslation.hardware.analysis.timing.TimingAnalysisRun;
import pt.up.fe.specs.binarytranslation.hardware.analysis.timing.TimingVerificationVivado;
import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareFolderGenerator;
import pt.up.fe.specs.binarytranslation.hardware.testbench.HardwareTestbenchGenerator;
import pt.up.fe.specs.binarytranslation.hardware.testbench.VerilatorTestbenchGenerator;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.dot.InstructionCDFGDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.generator.InstructionCDFGGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.resolve_names.InstructionCDFGNameResolver;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.validation.InstructionCDFGHardwareValidationDataGenerator;
import pt.up.fe.specs.binarytranslation.processes.VerilatorCompile;
import pt.up.fe.specs.binarytranslation.processes.VerilatorRun;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;

public class InstructionCDFGFullFlow {

    private Instruction instruction;
  
    private InstructionCDFG icdfg;
    
    private String systemPath;
    private String wslPath;

    private HardwareModule generatedModule;
    private int moduleTestbenchSamples;
    
    /** Applies the full flow of the toolchain on a single instruction (linux only)
     * 
     * @param instruction Instruction to apply the full flow on
     * @param testbenchSamples Number of samples to generate for the testbench
     * @param systemPath The path for the folder in your main OS 
     */
    public InstructionCDFGFullFlow(Instruction instruction, int testbenchSamples, String systemPath) {
        this(instruction, testbenchSamples, systemPath, systemPath);
    }
    
    /** <b>Use this constructor only if you are using WSL, otherwise use the single path constructor</b>
     * <br><br>Applies the full flow of the toolchain on a single instruction (windows only)
     * @param instruction Instruction to apply the full flow on
     * @param testbenchSamples Number of samples to generate for the testbench
     * @param systemPath The path for the folder in your main OS (windows in wsl)
     * @param wslPath The path for the folder in you guest OS (linux in wsl)
     */
    public InstructionCDFGFullFlow(Instruction instruction, int testbenchSamples, String systemPath, String wslPath) {
        
        this.instruction = instruction;

        
        this.systemPath = systemPath + instruction.getName();
        this.wslPath = wslPath + instruction.getName();

        this.moduleTestbenchSamples = testbenchSamples;
    }
    
    
    public void generateFolderStructures() {
        HardwareFolderGenerator.generate(systemPath);
    }

    public void generateInstructionCDFG() {
        System.out.print("Generating InstructionCDFG...");
        this.icdfg = (new InstructionCDFGGenerator()).generate(this.instruction);
        System.out.println("\tDONE");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void exportInstructionCDFGAsDOT() throws IOException {
        System.out.print("Exporting InstructionCDFG as DOT...");
        InstructionCDFGDOTExporter exp = new InstructionCDFGDOTExporter();
        Writer writer = new StringWriter();
        exp.exportGraph((GeneralFlowGraph)this.icdfg, this.instruction.getName(), writer);
        System.out.println("\t" + InstructionCDFGDOTExporter.generateGraphURL(writer.toString()));
        InstructionCDFGDOTExporter.generateGraphvizFile(HardwareFolderGenerator.getBTFFolder(systemPath), this.instruction.getName(), writer.toString());
        exp.emit((GeneralFlowGraph)this.icdfg, this.instruction.getName(), writer, HardwareFolderGenerator.newFile(HardwareFolderGenerator.getBTFFolder(systemPath), this.instruction.getName(), "dot"));
        
    }

    public void resolveInstructionCDFGNames() {
        InstructionCDFGNameResolver.resolve(icdfg);
    }
    
    public void generateHardwareModule() throws IOException {
        System.out.print("Generating HW module...");
        InstructionCDFGCustomInstructionUnitGenerator moduleGenerator = new InstructionCDFGCustomInstructionUnitGenerator(icdfg, this.instruction.getName());
        this.generatedModule = moduleGenerator.generateHardware();
        this.generatedModule.emit(HardwareFolderGenerator.newHardwareHDLFile(systemPath, this.generatedModule.getName(), "sv"));
        
        System.out.println("\tDONE");
    }
    
    public void generateHardwareModuleValidationData() throws IOException {
        
        InstructionCDFGHardwareValidationDataGenerator validation = new InstructionCDFGHardwareValidationDataGenerator();
        
        validation.generateValidationData(this.icdfg, moduleTestbenchSamples);
        System.out.print("Generating HW module testbench validation input memory file...");
        HardwareFolderGenerator.newHardwareTestbenchFile(systemPath, "input", "mem").write(validation.buildInputHexMemFile().getBytes());
        System.out.println("\tDONE");
        
        System.out.print("Generating HW module testbench validation output memory file...");
        HardwareFolderGenerator.newHardwareTestbenchFile(systemPath, "output", "mem").write(validation.buildOutputHexMemFile().getBytes());
        System.out.println("\tDONE");
    }
    
    public void generateHardwareModuleTestbench() throws IOException {
        System.out.print("Generating HW module testbench...");
        HardwareTestbenchGenerator.generate(generatedModule, moduleTestbenchSamples, new File(this.systemPath + "/hw/tb/input.mem") , new File(this.systemPath + "/hw/tb/output.mem")).emit(HardwareFolderGenerator.newHardwareTestbenchFile(systemPath, generatedModule.getName() + "_tb", "sv"));
        System.out.println("\tDONE");
    }
    
    public void generateVerilatorTestbench() throws IOException {
        System.out.print("Generating HW module Verilator testbench...");
        VerilatorTestbenchGenerator.emit(HardwareFolderGenerator.newHardwareTestbenchFile(systemPath, this.instruction.getName() + "_tb", "cpp"), generatedModule.getName(), moduleTestbenchSamples);
        System.out.println("\tDONE");
    }
    
    public boolean runVerilatorTestbench() throws IOException {
        System.out.print("Running Verilator testbench...");
        
        VerilatorCompile verilator_compile = new VerilatorCompile(wslPath, this.instruction.getName());
        
        verilator_compile.start();
        
        VerilatorRun verilator = new VerilatorRun(wslPath+"/hw/tb", this.instruction.getName());
        
        verilator.start();
        
        if(!verilator.simulate()) {
            System.out.println("ERROR: Validation failed !!!");
            verilator.close();
            return false;
        }
        
        verilator.close();
        
        return true;
    }
    
    public void runVivadoTCL() throws IOException, InterruptedException, ExecutionException{
        System.out.print("Performing timing analysis (vivado) on generated module...");
        
        TimingVerificationVivado verificator = new TimingVerificationVivado(wslPath, this.instruction.getName());
        
        Process verificatorProcess = verificator.start();
        
        verificatorProcess.waitFor();
        
        System.out.println("\tDONE");
    }
    
    public void performIcetimeTimingAnalysis() throws IOException {
        System.out.print("Performing timing analysis (icetime) on generated module...");
        TimingAnalysisRun.start(wslPath, this.instruction.getName());
        System.out.println("\tDONE");
    }
    
    public void runAll() throws IOException, InterruptedException, ExecutionException {

       
        
        this.generateFolderStructures();
        this.generateInstructionCDFG();
        
//this.exportInstructionCDFGAsDOT();
        
        
        this.resolveInstructionCDFGNames();
        this.exportInstructionCDFGAsDOT();
        
        this.generateHardwareModule();
        
        this.generateHardwareModuleValidationData();
        /*this.generateHardwareModuleTestbench();
        this.generateVerilatorTestbench();
        
        
        System.out.println("\nTiming verification results from Vivado:\n");
        TimingVerificationVivado.getVerificationResultsParsed(wslPath, this.instruction.getName())
            .forEach((deviceName, ListOfResults) -> {
                System.out.println("\tDevice: " + deviceName);
                System.out.println("\t\tTotal delay " + ListOfResults.get(0) + "ns ");
                System.out.println("\t\t\tModule delay only " + ListOfResults.get(1)+" ("+ListOfResults.get(2)+"%)");
                System.out.println("\t\t\tDelay due to routing " + ListOfResults.get(3)+" ("+ListOfResults.get(4)+"%)\n");
                        
            });
        
        //this.runVivadoTCL();
        
        /*
        if(!this.runVerilatorTestbench())
            return;
        
        this.performIcetimeTimingAnalysis();
        */
    }
 
    
}
