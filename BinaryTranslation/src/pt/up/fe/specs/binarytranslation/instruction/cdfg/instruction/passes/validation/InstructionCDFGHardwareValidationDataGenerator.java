package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.passes.validation;

import java.util.Collection;
import java.util.HashMap;
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
    
    public Collection<Map<String, Number>> getValidationInputData(){
        return this.validationData.keySet();
    }
    
    public Collection<Map<String, Number>> getValidationOutputData(){
        return this.validationData.values();
    }
    
    public String buildInputHexMemFile() {
        return this.buildHexMemFile(this.getValidationInputData());
    }
    
    public String buildOutputHexMemFile() {
        return this.buildHexMemFile(this.getValidationOutputData());
    }
    
    private String buildHexMemFile(Collection<Map<String, Number>> validationData) {
        
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
        Map<String, Number> validationInputs = new HashMap<>();
        
        for(int i = 0; i < samples; i++) {
            
            validationInputs.clear();
            
            instruction.getDataInputsReferences().forEach(input -> validationInputs.put(input, inputDataGenerator.nextInt()));
            
            this.calculate(instruction.getInstructionParseTree(), validationInputs);
            this.getValidationData().put(validationInputs, this.getOutputValuesMap());
            
        }
    }
    
}
