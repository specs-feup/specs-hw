package pt.up.fe.f4pga;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum F4PGAResourceBuildFile implements ResourceProvider {
	
	F4PGABASH_SCRIPT_TEMPLATE("pt/up/fe/f4pga/build.sh");

    private final String resource;

    private F4PGAResourceBuildFile(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }

}
