package pt.up.fe.specs.binarytranslation;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.util.providers.ResourceProvider;

public interface ELFProvider extends ResourceProvider {

    default public String asTxtDump() {
        return this.getResource().replace(".elf", "_objdump.txt");
    }

    default public String asTraceTxtDump() {
        return this.getResource().replace(".elf", "_trace.txt");
    }

    default public Application toApplication() {
        return null;
    }

    public Number getKernelStart();

    public Number getKernelStop();

    public ResourceProvider getCPUName();
}
