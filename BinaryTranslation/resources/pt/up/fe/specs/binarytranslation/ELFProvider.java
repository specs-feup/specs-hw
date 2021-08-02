package pt.up.fe.specs.binarytranslation;

import pt.up.fe.specs.util.providers.ResourceProvider;

public interface ELFProvider extends ResourceProvider {

    public String asTxtDump();

    public String asTraceTxtDump();

    public Number getKernelStart();

    public Number getKernelStop();
}
