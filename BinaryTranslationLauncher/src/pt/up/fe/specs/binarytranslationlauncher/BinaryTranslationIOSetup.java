package pt.up.fe.specs.binarytranslationlauncher;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.SetupFieldOptions.DefaultValue;
import pt.up.fe.specs.guihelper.SetupFieldOptions.SingleSetup;

public enum BinaryTranslationIOSetup implements SetupFieldEnum, SingleSetup, DefaultValue {

    // I/O
    InputPath(FieldType.string),
    OutputFolder(FieldType.string),
    CleanOutputFolder(FieldType.bool);

    private final FieldType fieldType;

    private BinaryTranslationIOSetup(FieldType fieldType) {
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
        return "IO Options";
    }
}
