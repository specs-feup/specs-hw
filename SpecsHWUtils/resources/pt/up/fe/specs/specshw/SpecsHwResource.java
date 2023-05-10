/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.specshw;

import pt.up.fe.specs.util.providers.ResourceProvider;

public enum SpecsHwResource implements ResourceProvider {

    // DOTTY_BINARY("dot"),
    GRAPH_HTML_TEMPLATE("pt/up/fe/specs/specshw/graph/graph_template.html"),
    VERILATOR_TEMPLATE("pt/up/fe/specs/specshw/hardware/verilator/verilator_testbench_template.cpp"),
    VIVADO_TCL_TIMING_VERIFICATION_TEMPLATE(
            "pt/up/fe/specs/specshw/hardware/vivado_timing_verification/vivado_tcl_timing_verification_template.tcl"),
    SPECS_COPYRIGHT_TEXT("pt/up/fe/specs/specshw/specs_cr_text.txt"),
    nullResource(null);

    private final String resource;

    private SpecsHwResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String getResource() {
        return resource;
    }
}
