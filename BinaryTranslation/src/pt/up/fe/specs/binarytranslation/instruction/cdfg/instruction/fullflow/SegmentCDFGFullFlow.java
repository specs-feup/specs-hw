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
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.hardware.accelerators.custominstruction.wip.InstructionCDFGCustomInstructionUnitGenerator;
import pt.up.fe.specs.binarytranslation.hardware.analysis.timing.TimingAnalysisRun;
import pt.up.fe.specs.binarytranslation.hardware.analysis.timing.TimingVerificationVivado;
import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareFolderGenerator;
import pt.up.fe.specs.binarytranslation.hardware.testbench.HardwareTestbenchGenerator;
import pt.up.fe.specs.binarytranslation.hardware.testbench.VerilatorTestbenchGenerator;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general.GeneralFlowGraph;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.dot.InstructionCDFGDOTExporter;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.resolve_names.InstructionCDFGNameResolver;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.SegmentCDFG;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.passes.validation.SegmentCDFGHardwareValidationDataGenerator;
import pt.up.fe.specs.binarytranslation.processes.VerilatorCompile;
import pt.up.fe.specs.binarytranslation.processes.VerilatorMakeTestbench;
import pt.up.fe.specs.binarytranslation.processes.VerilatorRun;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;

public class SegmentCDFGFullFlow {
    
    private BinarySegment segment;

    private List<Instruction> instructions;
  
    private SegmentCDFG scdfg;
    
    private String pathToMakeDirectory;
    
    private String segmentName;

    private HardwareModule generatedModule;
    private int moduleTestbenchSamples;
    
    
    public SegmentCDFGFullFlow(String path ,BinarySegment segment, int testbenchSamples) {
        
        this(segment.getInstructions(),"Segment_"+ Integer.toUnsignedString(segment.getUniqueId()), testbenchSamples, path);
        this.segment = segment;
        
    }
    
    public SegmentCDFGFullFlow(List<Instruction> instructions, String segmentName, int testbenchSamples, String pathToMakeDirectory) {
        
        this.instructions = instructions;

        this.pathToMakeDirectory = pathToMakeDirectory + segmentName;

        this.segmentName = segmentName;
        
        this.moduleTestbenchSamples = testbenchSamples;
        
        this.segment = null;
    }
    
    
    public void generateFolderStructures() {
        HardwareFolderGenerator.generate(pathToMakeDirectory);
    }

    public void exportDetectedSegment() throws IOException {
        
        String detectedSegmentFileName = HardwareFolderGenerator.getBTFFolder(pathToMakeDirectory) + "/" + this.segmentName + ".txt";
        
        File detectedSegmentFile = new File(detectedSegmentFileName);
        
        if(!detectedSegmentFile.createNewFile()) {
            throw new IOException();
        }
        
        FileWriter segmentFileWriter = new FileWriter(detectedSegmentFileName);
    
        segmentFileWriter.write(this.segment.getRepresentation());
        
        segmentFileWriter.close();
        
    }
    
    public void generateCDFG() {
        System.out.print("Generating SegmentCDFG...");
        this.scdfg = new SegmentCDFG(instructions);
        this.scdfg.generate();
        System.out.println("\tDONE");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void exportInstructionCDFGAsDOT() throws IOException {
        System.out.print("Exporting InstructionCDFG as DOT...");
        InstructionCDFGDOTExporter exp = new InstructionCDFGDOTExporter();
        Writer writer = new StringWriter();
        exp.exportGraph((GeneralFlowGraph)this.scdfg, this.segmentName, writer);
        System.out.println("\t" + InstructionCDFGDOTExporter.generateGraphURL(writer.toString()));
        InstructionCDFGDOTExporter.generateGraphvizFile(HardwareFolderGenerator.getBTFFolder(pathToMakeDirectory), this.segmentName, writer.toString());
        exp.emit((GeneralFlowGraph)this.scdfg, this.segmentName, writer, HardwareFolderGenerator.newFile(HardwareFolderGenerator.getBTFFolder(pathToMakeDirectory), this.segmentName, "dot"));
        
    }

    public void resolveInstructionCDFGNames() {
        InstructionCDFGNameResolver.resolve(this.scdfg);
    }
    
    public void generateHardwareModule() throws IOException {
        System.out.print("Generating HW module...");
        InstructionCDFGCustomInstructionUnitGenerator moduleGenerator = new InstructionCDFGCustomInstructionUnitGenerator(this.scdfg, this.segmentName);
        this.generatedModule = moduleGenerator.generateHardware();
        this.generatedModule.emit(HardwareFolderGenerator.newHardwareHDLFile(pathToMakeDirectory, this.generatedModule.getName(), "sv"));
        
        System.out.println("\tDONE");
    }
    
    public void generateHardwareModuleValidationData() throws IOException {
        
        SegmentCDFGHardwareValidationDataGenerator validation = new SegmentCDFGHardwareValidationDataGenerator();
        
        validation.generateValidationData(this.scdfg, moduleTestbenchSamples);
        System.out.print("Generating HW module testbench validation input memory file...");
        HardwareFolderGenerator.newHardwareTestbenchFile(pathToMakeDirectory, "input", "mem").write(validation.buildInputHexMemFile().getBytes());
        System.out.println("\tDONE");
        
        System.out.print("Generating HW module testbench validation output memory file...");
        HardwareFolderGenerator.newHardwareTestbenchFile(pathToMakeDirectory, "output", "mem").write(validation.buildOutputHexMemFile().getBytes());
        System.out.println("\tDONE");
    }
    
    public void generateHardwareModuleTestbench() throws IOException {
        System.out.print("Generating HW module testbench...");
        HardwareTestbenchGenerator.generate(
                this.generatedModule, 
                this.moduleTestbenchSamples, 
                new File(this.pathToMakeDirectory + "/hw/tb/input.mem") , 
                new File(this.pathToMakeDirectory + "/hw/tb/output.mem"))
        .emit(HardwareFolderGenerator.newHardwareTestbenchFile(this.pathToMakeDirectory, this.generatedModule.getName() + "_tb", "sv"));
        
        System.out.println("\tDONE");
    }
    
    public void generateVerilatorTestbench() throws Exception {
        System.out.print("Generating HW module Verilator testbench...");
        VerilatorTestbenchGenerator.emit(HardwareFolderGenerator.newHardwareTestbenchFile(pathToMakeDirectory,this.segmentName + "_tb", "cpp"), generatedModule.getName(), moduleTestbenchSamples);
        
        VerilatorCompile verilator_compile = new VerilatorCompile(pathToMakeDirectory, this.segmentName);
        verilator_compile.start();
        int compilationOK = verilator_compile.getProc().waitFor();
        verilator_compile.close();
        
        if(compilationOK != 0) {
            System.out.println(verilator_compile.receive());
            throw new Exception();
        }
        
        VerilatorMakeTestbench verilator_make = new VerilatorMakeTestbench(pathToMakeDirectory, this.segmentName);
        verilator_make.start();
        verilator_make.getProc().waitFor();
        verilator_make.close();
        
        System.out.println("\tDONE");
    }
    
    public void runVerilatorTestbench() throws Exception {
        System.out.print("Running Verilator testbench...");
        
        VerilatorRun runVerilator = new VerilatorRun(this.pathToMakeDirectory, this.segmentName);
        runVerilator.start();
        runVerilator.getProc().waitFor();
        boolean passedSimulation = runVerilator.simulate();
        runVerilator.close();
        
        if(!passedSimulation) {
            System.out.println(runVerilator.receive());
            throw new Exception();
        }
        
        System.out.println("\tDONE");
    }
    
    public void runVivadoTCL() throws IOException, InterruptedException, ExecutionException{
        System.out.print("Performing timing analysis (vivado) on generated module...");
        
        TimingVerificationVivado verificator = new TimingVerificationVivado(this.pathToMakeDirectory, this.segmentName);
        
        verificator.start();
        verificator.getProc().waitFor();
        verificator.close();
        
        System.out.println("\tDONE");
    }
    
    
    public void performIcetimeTimingAnalysis() throws IOException {
        System.out.print("Performing timing analysis (icetime) on generated module...");
        TimingAnalysisRun.start(pathToMakeDirectory, this.segmentName);
        System.out.println("\tDONE");
    }
    
    public void emitBashScript() {
        

        
    }
    
    public void runAll()  {

        this.generateFolderStructures();
        
        if(this.segment != null) {
        
            try {
                this.exportDetectedSegment();
            } catch (Exception e) {
                System.out.println("ERROR: Could not export detected segment");
                System.out.flush();
                e.printStackTrace();
                
                return;
            }
            
        }
        
        this.generateCDFG();
/*
        try {
            this.exportInstructionCDFGAsDOT();
        } catch (Exception e) {
            System.out.println("ERROR: Could not export CDFG as DOT");
        }
*/        
        this.resolveInstructionCDFGNames();
        
        try {
            this.exportInstructionCDFGAsDOT();
        } catch (Exception e) {
            System.out.println("ERROR: Could not export CDFG as DOT");
            System.out.flush();
            e.printStackTrace();
            
            return;
        }
       
        
        
        try {
            this.generateHardwareModule();
        } catch (Exception e) {
            System.out.println("ERROR: Could not generate HW module");
            System.out.flush();
            e.printStackTrace();
            
            return;
        }

        
        try {
            this.generateHardwareModuleValidationData();
        } catch (Exception e) {
            System.out.println("ERROR: Could not generate HW module validation data");
            System.out.flush();
            e.printStackTrace();
            
            return;
        }
        try {
            this.generateHardwareModuleTestbench();
        } catch (Exception e) {
            System.out.println("ERROR: Could not generate HW module testbench");
            System.out.flush();
            e.printStackTrace();
            
            return;
        }
        
        try {
            this.generateVerilatorTestbench();
        } catch (Exception e) {
            System.out.println("ERROR: Could not generate HW module verilator testbench");
            System.out.flush();
            e.printStackTrace();
            
            return;
        }
        
        try {
            this.runVerilatorTestbench();
        } catch (Exception e) {
            System.out.println("ERROR: Could not run Verilator testbench");
            System.out.flush();
            e.printStackTrace();
            
            return;
        }

        try {
            this.runVivadoTCL();
        } catch (Exception e) {
            System.out.println("ERROR: Could not run Vivado");
            System.out.flush();
            e.printStackTrace();
            
            return;
        }
        /*    
        this.performIcetimeTimingAnalysis();
        */
        
    }
 

    
}
