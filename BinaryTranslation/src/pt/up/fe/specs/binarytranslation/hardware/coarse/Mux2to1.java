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

package pt.up.fe.specs.binarytranslation.hardware.coarse;

public class Mux2to1 extends CoarseGrainedUnit {

    /*
    public VariableOperator i0 = addInputPort("I0", 32);
    public VariableOperator i1 = addInputPort("I1", 32);
    public VariableOperator select = addInputPort("select", 1);
    public VariableOperator out = addOutputPort("Out", 32);
    
    IfElseStatement ie1 = alwayscomb("muxBlock")._ifelse(select);
         
        
        ie1.then().nonBlocking(out, i1);
        ie1.orElse().nonBlocking(out, i0);
    
    public Mux2to1() {
        super(Mux2to1.class.getSimpleName());
    };*/

    public Mux2to1(int bitwidth) {
        super(Mux2to1.class.getSimpleName());

        var i0 = addInputPort("IO", bitwidth);
        var i1 = addInputPort("I1", bitwidth);
        var sel = addInputPort("select", 1);
        var out = addOutputPort("Out", bitwidth);

        var block = alwayscomb("muxBlock");
        var ie1 = block._ifelse(sel);
        ie1.then().nonBlocking(out, i1);
        ie1.orElse().nonBlocking(out, i0);
    }
}
