package pt.up.fe.specs.binarytranslation;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum BinaryTranslationResource implements ResourceProvider {

    DOTTY_BINARY("dot"),
    GRAPH_HTML_TEMPLATE("pt/up/fe/BinaryTranslation/graph/graph_template.html"),
    SPECS_COPYRIGHT_TEXT("pt/up/fe/specs/BinaryTranslation/specs_cr_text.txt"),
    VERILATOR_TEMPLATE("/pt/up/fe/specs/binarytranslation/hardware/verilator/verilator_testbench_template.cpp");

    private final String resource;

    private BinaryTranslationResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
