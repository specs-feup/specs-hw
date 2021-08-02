package pt.up.fe.specs.binarytranslation;

import pt.up.fe.specs.util.providers.ResourceProvider;

public class ATraceDumpProvider implements ELFProvider {

    // this should be a type of obejcst which is also an ELFProvider but which
    // overrides getRousource!
    protected final ELFProvider original;

    public ATraceDumpProvider(ELFProvider elfprovider) {
        this.original = elfprovider;
    }

    /*
     * Override resource with its objdump
     */
    @Override
    public String getResource() {
        var filename = this.original.asTraceTxtDump();
        // TODO check file exist?
        return filename;
    }

    /*
     * Not valid for this class
     */
    @Override
    public String asTxtDump() {
        return null;
    }

    @Override
    public Number getKernelStart() {
        return this.original.getKernelStart();
    }

    @Override
    public Number getKernelStop() {
        return this.original.getKernelStop();
    }

    @Override
    public ResourceProvider getCPUName() {
        return this.original.getCPUName();
    }
}