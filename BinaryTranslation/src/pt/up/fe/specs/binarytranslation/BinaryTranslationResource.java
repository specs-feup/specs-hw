package pt.up.fe.specs.binarytranslation;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum BinaryTranslationResource implements ResourceProvider {

    DOTTY_BINARY("dot"),
    GRAPH_HTML_TEMPLATE("pt/up/fe/binarytranslation/graph/graph_template.html"),
    SPECS_COPYRIGHT_TEXT("pt/up/fe/specs/binarytranslation/specs_cr_text.txt"),
    VERILATOR_TEMPLATE("pt/up/fe/specs/binarytranslation/hardware/verilator/verilator_testbench_template.cpp"),
    VIVADO_TCL_TIMING_VERIFICATION_TEMPLATE("pt/up/fe/specs/binarytranslation/hardware/vivado_timing_verification/vivado_tcl_timing_verification_template.tcl");

    private final String resource;

    private BinaryTranslationResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
