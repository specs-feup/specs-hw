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

package pt.up.fe.specs.binarytranslation.hardware;

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
public class HardwareModuleHeader {

    /*
     * Complete header text for this hardware module
     * (Verilog style comments presumed)
     */
    final private String headertext;

    public HardwareModuleHeader(BinarySegmentGraph graph) {

        // TODO add more stuff to header

        this.headertext = BinaryTranslationUtils.getSPeCSCopyright()
                + "\n\n/* \n* This module represents the following graph:\n\t" +
                "Application: " + graph.getSegment().getAppinfo().getAppName() + "\n\t" +
                "Graph Type: " + graph.getType().toString() + "\n\n*/";

        // TODO: do something better...
        // like a getHeader module for the graph, or something..
    }

    /*    public HardwareModuleHeader(GraphBundle bundle) {
    
    }*/

    public String getHeadertext() {
        return headertext;
    }
}
