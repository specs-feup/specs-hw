/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.binarytranslation.binarysegments;

import java.util.Map;

/**
 * Holds the mapping of a symbolic representation of a register or value (e.g., r<a>, r<b>.. to a specific register or
 * value of the originating processor instruction trace). This is required to generate CDFGs and/or final outputs for
 * every in/out context
 * 
 * @author nuno
 *
 */
public class SegmentContext {

    private Integer startaddr;
    private Map<String, String> context;

    public SegmentContext(Integer startaddr, Map<String, String> context) {
        this.startaddr = startaddr;
        this.context = context;
    }

    public Integer getStartaddr() {
        return startaddr;
    }

    /*
     * returns String representation of non symbolic originating operand (i.e. r4, or w7)
     */
    public String resolve(String symbol) {
        return this.context.get(symbol);
    }
}
