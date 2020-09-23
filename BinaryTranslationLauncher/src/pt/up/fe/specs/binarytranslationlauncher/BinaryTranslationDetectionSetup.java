package pt.up.fe.specs.binarytranslationlauncher;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.SetupFieldOptions.DefaultValue;
import pt.up.fe.specs.guihelper.SetupFieldOptions.SingleSetup;

public enum BinaryTranslationDetectionSetup implements SetupFieldEnum, SingleSetup, DefaultValue {

    // detectors
    FrequentSequence(FieldType.bool),
    BasicBlock(FieldType.bool);

    private final FieldType fieldType;

    private BinaryTranslationDetectionSetup(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public FieldType getType() {
        return this.fieldType;
    }

    /*
     * Basically functions as a section title
     */
    @Override
    public String getSetupName() {
        return "Segment Detection Options";
    }
}
