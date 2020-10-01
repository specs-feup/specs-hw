package pt.up.fe.specs.binarytranslationlauncher.options;

// public enum BinaryTranslationISAList implements StringProvider {
public enum BinaryTranslationISAList {

    MicroBlaze32Bit("MicroBlaze (32 bit)"),
    ARMaarch64("ARM (aarch64)"),
    RISCVrisc32iam("RISC-V (risc32iam)");

    // private static final Lazy<EnumHelperWithValue<BinaryTranslationISAList>> ENUM_HELPER = EnumHelperWithValue
    // .newLazyHelperWithValue(BinaryTranslationISAList.class);

    private String value;

    private BinaryTranslationISAList(String name) {
        this.value = name;
    }

    public String getValue() {
        return value;
    }

    // public static BinaryTranslationISAList getEnum(String value) {
    // return ENUM_HELPER.get().fromValue(value);
    // }
    //
    // @Override
    // public String getString() {
    // return value;
    // }

}
