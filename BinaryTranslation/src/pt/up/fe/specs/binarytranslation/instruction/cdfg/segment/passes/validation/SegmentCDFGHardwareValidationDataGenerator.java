package pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.passes.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.validation.InstructionCDFGHardwareValidationDataGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.segment.SegmentCDFG;

public class SegmentCDFGHardwareValidationDataGenerator extends InstructionCDFGHardwareValidationDataGenerator{

    public void generateValidationData(SegmentCDFG segment, int samples) {
        
        Random inputDataGenerator = new Random();
        Map<String, Number> validationInputs;
        Map<String, Number> previousInstructionOutput;
        Map<String, Number> allInstructionsOutput;
        
        segment.generateDataInputs();
        
        for(int i = 0; i < samples; i++) {
            
            validationInputs = new TreeMap<>();
            allInstructionsOutput = new TreeMap<>();
            
            for(String reference : segment.getDataInputsReferences()) {
                validationInputs.put(reference, inputDataGenerator.nextInt());
            }
            
            previousInstructionOutput = new TreeMap<>(validationInputs);
            
            for(Instruction instruction : segment.getInstructions()) {
   
                this.calculate(instruction.getPseudocode().getParseTree(), previousInstructionOutput);

                
                //previousInstructionOutput = new HashMap<>(this.getOutputValuesMap());
                //allInstructionsOutput.putAll(this.getOutputValuesMap());
                //previousInstructionOutput = new HashMap<>(validationInputs);
                //previousInstructionOutput.putAll(allInstructionsOutput);
                previousInstructionOutput.putAll(this.getOutputValuesMap());
                allInstructionsOutput.putAll(this.getOutputValuesMap());
  
                
            }
            
            this.clearCache();
            
            System.out.println(validationInputs);
            
            this.getValidationData().put(validationInputs, allInstructionsOutput);
            
        }
    }
    
}
