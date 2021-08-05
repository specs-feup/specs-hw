package pt.up.fe.specs.binarytranslation;

public abstract class AObjDumpProvider implements ELFProvider {

    private final ELFProvider original;

    public AObjDumpProvider(ELFProvider elfprovider) {
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

    @Override
    public Number getKernelStart() {
        return this.original.getKernelStart();
    }

    @Override
    public Number getKernelStop() {
        return this.original.getKernelStop();
    }
}
