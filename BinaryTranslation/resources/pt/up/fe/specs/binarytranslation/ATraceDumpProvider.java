package pt.up.fe.specs.binarytranslation;

public class ATraceDumpProvider implements ELFProvider {

    protected final ELFProvider original;

    public ATraceDumpProvider(ELFProvider elfprovider) {
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

    @Override
    public Number getKernelStart() {
        return this.original.getKernelStart();
    }

    @Override
    public Number getKernelStop() {
        return this.original.getKernelStop();
    }
}