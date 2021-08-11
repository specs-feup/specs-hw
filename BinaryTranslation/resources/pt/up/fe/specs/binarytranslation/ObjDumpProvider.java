package pt.up.fe.specs.binarytranslation;

public class ObjDumpProvider implements ELFProvider {

    private final ELFProvider original;

    public ObjDumpProvider(ELFProvider elfprovider) {
        this.original = elfprovider;
    }

    @Override
    public String getELFName() {
        return this.original.getELFName().replace(".elf", "_objdump.txt");
    }

    @Override
    public String getResource() {
        return original.getResource();
    }
}
