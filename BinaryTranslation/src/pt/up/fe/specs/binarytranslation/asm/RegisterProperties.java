package pt.up.fe.specs.binarytranslation.asm;

import java.util.List;

public interface RegisterProperties {
    
    public boolean isStackPointer(String reg);
    
    public boolean isTemporary(String reg);
    
    public boolean isParameter(String reg);
    
    public boolean isZero(String reg);
    
    public boolean isReturn(String reg);

    public List<String> getAllRegisters();
}

