package org.specs.MicroBlaze.provider;

public enum MicroBlazeTiny implements MicroBlazeELFProvider {

    tiny("org/specs/MicroBlaze/asm/tiny.txt");

    private final String resource;
    private final String elfName;

    private MicroBlazeTiny(String fullPath) {
        this.resource = fullPath;
        this.elfName = name() + ".txt";
    }

    @Override
    public String getELFName() {
        return this.elfName;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
