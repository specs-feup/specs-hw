package pt.up.fe.specs.binarytranslationlauncher;

import java.util.ArrayList;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.SetupFieldOptions.DefaultValue;
import pt.up.fe.specs.guihelper.SetupFieldOptions.MultipleChoice;
import pt.up.fe.specs.guihelper.SetupFieldOptions.SingleSetup;
import pt.up.fe.specs.util.utilities.StringList;

public enum BinaryTranslationStreamSetup implements SetupFieldEnum, SingleSetup, MultipleChoice, DefaultValue {

    // ISA (necessary while auto-detect doesnt work?
    InstructionSet(FieldType.multipleChoice),

    // using objedump works out of the box
    /*
     * objdump -a cholesky.elf 
        
        cholesky.elf:     file format elf32-little
        cholesky.elf
    
     */

    // type
    StaticAnalysis(FieldType.bool),
    Simulation(FieldType.bool);

    private final FieldType fieldType;

    private BinaryTranslationStreamSetup(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public FieldType getType() {
        return fieldType;
    }

    /*
     * Basically functions as a section title
     */
    @Override
    public String getSetupName() {
        return "Instruction Stream Options";
    }

    @Override
    public StringList getChoices() {

        if (this == InstructionSet) {
            var list = new ArrayList<String>();
            list.add("MicroBlaze (32 bit)");
            list.add("ARM (aarch64)");
            list.add("RISC-V (risc32iam)");
            // TODO: replace with BinaryTranslationUtils.getSupportedISAS()
            return new StringList(list);
        }

        return null;
    }
}
