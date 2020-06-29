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

package pt.up.fe.specs.binarytranslation.hardware.tree;

import pt.up.fe.specs.binarytranslation.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;

/**
 * 
 * @author Nuno
 *
 */
public class ModuleHeader extends AHardwareNode {

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

    /*
     * (non-Javadoc)
     * @see pt.up.fe.specs.binarytranslation.hardware.component.HardwareComponent#getAsString()
     */
    public ModuleHeader() {
        this.headertext = BinaryTranslationUtils.getSPeCSCopyright() + "\n";
        // TODO: add more info
    }

    @Override
    public String getAsString() {
        return headertext;
    }
}
