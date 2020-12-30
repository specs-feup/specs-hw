package org.specs.BinaryTranslation;

import pt.up.fe.specs.util.providers.ResourceProvider;

public interface ELFProvider extends ResourceProvider {

    @Override
    public String getResource();

    public String asTxtDump();

    public Number getKernelStart();

    public Number getKernelStop();
}
