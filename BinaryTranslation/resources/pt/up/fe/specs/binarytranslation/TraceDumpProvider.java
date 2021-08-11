package pt.up.fe.specs.binarytranslation;

public class TraceDumpProvider implements ELFProvider {

    protected final ELFProvider original;

    public TraceDumpProvider(ELFProvider elfprovider) {
        this.original = elfprovider;
    }

    @Override
    public String getELFName() {
        return this.original.getELFName().replace(".elf", "_trace.txt");
    }

    @Override
    public String getResource() {
        return original.getResource();
    }
}