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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.edge;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;

public abstract class AGenericCDFGEdge extends DefaultEdge{
  
    public abstract AGenericCDFGEdge duplicate();
    
    public abstract Attribute getDotArrowhead();
    public abstract Attribute getDotLabel();
    
    public Object getTarget() {
        return super.getTarget();
    }
    
    public Object getSource() {
        return super.getSource();
    }
}
