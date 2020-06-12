/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.hardware.component;

import java.io.OutputStream;
import java.time.LocalDateTime;

import pt.up.fe.specs.binarytranslation.*;
import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

/**
 * 
 * @author Nuno
 *
 */
public class ModuleHeader implements HardwareComponent {

    /*
     * Complete header text for this hardware module
     * (Verilog style comments presumed)
     */
    final private String headertext;

    public ModuleHeader(BinarySegmentGraph graph) {

        // TODO add more stuff to header

        this.headertext = BinaryTranslationUtils.getSPeCSCopyright()
                + "\n\n/* \n * This module represents the following graph:\n" +
                " * Application: " + graph.getSegment().getAppinfo().getAppName() + "\n" +
                " * Graph Type: " + graph.getType().toString() + "\n" +
                " * Number of contexts: " + graph.getSegment().getContexts().size() + "\n" +
                " * Number of nodes: " + graph.getNodes().size() + "\n" +
                " * Achieved IPC: " + graph.getEstimatedIPC() + "\n" +
                " */\n";

        // TODO: do something better...
        // like a getHeader module for the graph, or something..
    }

    /*    public HardwareModuleHeader(GraphBundle bundle) {
    
    }*/

    public String getAsString() {
        return headertext;
    }

    public void emit(OutputStream os) {
        // TODO
    }
}
