package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import pt.up.fe.specs.binarytranslation.instruction.calculator.generator.PseudoInstructionCalculatorGenerator;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.InstructionCDFG;

public class InstructionCDFGHardwareValidationDataGenerator extends PseudoInstructionCalculatorGenerator{

    protected Map<Map<String, Number>, Map<String, Number>> validationData;
    
    public InstructionCDFGHardwareValidationDataGenerator() {
        this.validationData = new HashMap<>();
    }
    
    @Override
    protected void clearCache() {
        
        this.validationData.clear();
        
        super.clearCache();
    }
    
    public Map<Map<String, Number>, Map<String, Number>> getValidationData(){
        return this.validationData;
    }
    
    public void putInValidationData(Map<String,Number> key, Map<String, Number> value) {
        this.validationData.put(key, value);
    }
    
    public List<Map<String, Number>> getValidationInputData(){
        return new ArrayList<Map<String, Number>>(this.validationData.values());
    }
    
    public List<Map<String, Number>> getValidationOutputData(){
        return new ArrayList<Map<String, Number>>(this.validationData.keySet());
    }
    
    public String buildInputHexMemFile() {
        return this.buildHexMemFile(this.getValidationInputData());
    }
    
    public String buildOutputHexMemFile() {
        return this.buildHexMemFile(this.getValidationOutputData());
    }
    
    private String buildHexMemFile(List<Map<String, Number>> validationData) {
        
        StringBuilder fileBuilder = new StringBuilder();
        
        validationData.forEach(valueMap -> {
            
            Map<String, Number> validationDataSorted = new TreeMap<>(valueMap);
            
            
            validationDataSorted.forEach((uid, value) -> {

                if(value instanceof Float)
                    fileBuilder.append(Float.toHexString((Float) value));
                else if (value instanceof Integer)
                    fileBuilder.append(String.format("%08X", value.intValue()));
            });

            fileBuilder.append("\n");
  
        });
 
        return fileBuilder.toString();
    }

    public void generateValidationData(InstructionCDFG instruction, int samples) {
        
        Random inputDataGenerator = new Random();
        Map<String, Number> validationInputs = new LinkedHashMap<>();
        
        for(int i = 0; i < samples; i++) {
            
            validationInputs.clear();
            
            instruction.getDataInputsReferences().forEach(input -> validationInputs.put(input, inputDataGenerator.nextInt()));
            
            this.calculate(instruction.getInstruction().getPseudocode().getParseTree(), validationInputs);
            this.getValidationData().put(validationInputs, this.getOutputValuesMap());
            
        }
    }
    
}
