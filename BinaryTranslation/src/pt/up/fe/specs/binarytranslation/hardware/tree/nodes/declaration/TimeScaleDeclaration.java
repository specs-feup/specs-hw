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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;

public class TimeScaleDeclaration extends HardwareDeclaration{
    
    public TimeScaleDeclaration() {
        
    }
    
    @Override
    protected HardwareNode copyPrivate() {
        return new TimeScaleDeclaration();
    }
    
    @Override
    public String getAsString() {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("'timescale ");
        
        // put time unit
        builder.append("1ns");
        
        builder.append("/");
        
        // put time precision
        builder.append("10ps");
        
        return builder.toString();
    }

}
