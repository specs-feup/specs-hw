package pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.passes.validation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.generator.InstructionCDFGGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.validation.InstructionCDFGHardwareValidationDataGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.SegmentCDFG;

public class SegmentCDFGHardwareValidationDataGenerator extends InstructionCDFGHardwareValidationDataGenerator{

    private static final Map<String, String> operandNameToPseudoCodeName = Map.of("RD","RD", "RS1", "RA", "RS2", "RB");
    
    public void generateValidationData(SegmentCDFG segment, int samples) {
        
        Random inputDataGenerator = new Random();
        Map<String, Number> validationInputs;
        Map<String, Number> previousInstructionOutput;
        Map<String, Number> allInstructionsOutput;
        
        segment.generateDataInputs();
        
        for(int i = 0; i < samples; i++) {
            
            validationInputs = new TreeMap<>();
            allInstructionsOutput = new TreeMap<>();
            
            for(String reference : new TreeSet<String>(segment.getDataInputsReferences())) {
                validationInputs.put(reference, inputDataGenerator.nextInt());
            }
            
            previousInstructionOutput = new TreeMap<>(validationInputs);
            
            for(Instruction instruction : segment.getInstructions()) {
   
                // resolve names of input registers
                
                Map<String, Number> calculationOutput = this.calculate(instruction, previousInstructionOutput);
                
                
                
                //resolve names of output registers
                
                previousInstructionOutput.putAll(calculationOutput);
                allInstructionsOutput.putAll(calculationOutput);
                
               
                
            }
            
            
            
           
           this.putInValidationData(allInstructionsOutput,validationInputs);
            
        }
        
   
    }
    
}
