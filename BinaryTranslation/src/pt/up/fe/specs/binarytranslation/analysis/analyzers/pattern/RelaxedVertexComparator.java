/**
 *  Copyright 2021 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern;

import java.util.Comparator;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;

class RelaxedVertexComparator implements Comparator<BtfVertex> {

    @Override
    public int compare(BtfVertex o1, BtfVertex o2) {
        var type1 = o1.getType();
        var type2 = o2.getType();
        var label1 = o1.getLabel();
        var label2 = o2.getLabel();

        if (type1 == BtfVertexType.REGISTER && type2 == BtfVertexType.REGISTER)
            return 0;
        if (type1 == BtfVertexType.OPERATION && type2 == BtfVertexType.OPERATION) {
            if (label1.equals(label2))
                return 0;
            else if ((label1.equals("+") || label1.equals("-")) && (label2.equals("+") || label2.equals("-")))
                return 0;
            else
                return -1;
        }
        if (type1 == BtfVertexType.IMMEDIATE && type2 == BtfVertexType.IMMEDIATE) {
            return 0;
        }
        if (type1 == BtfVertexType.MEMORY && type2 == BtfVertexType.MEMORY)
            return 0;
        return -1;
    }
}