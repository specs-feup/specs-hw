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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.subscript;

import org.jgrapht.alg.util.Pair;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.edge.modifier.AInstructionCDFGModifier;

public abstract class AInstructionCDFGSubscriptModifier extends AInstructionCDFGModifier{

    private Pair<Integer, Integer> subscript;
    
    protected AInstructionCDFGSubscriptModifier(int upper, int lower) {
        this.subscript = new Pair<Integer, Integer>(upper, lower);
    }
    
    public Integer getLowerBound() {
        return this.subscript.getSecond();
    }
    
    public Integer getUpperBound() {
        return this.subscript.getFirst();
    }
 
    @Override
    public String toString() {
        
        StringBuilder subscriptBuilder = new StringBuilder();
        
        subscriptBuilder.append("[");
        subscriptBuilder.append(subscript.getFirst());
        
        
        if(subscript.getFirst() != subscript.getSecond()) {
            subscriptBuilder.append(":");
            subscriptBuilder.append(subscript.getSecond());
        }
        
        subscriptBuilder.append("]");
        
        return subscriptBuilder.toString();
    }
    
}
