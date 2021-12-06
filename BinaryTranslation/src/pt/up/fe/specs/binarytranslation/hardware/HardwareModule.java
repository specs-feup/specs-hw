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

package pt.up.fe.specs.binarytranslation.hardware;

import java.io.OutputStream;
import java.util.List;

public interface HardwareModule {

    /*
     * 
     */
    public void emit(OutputStream os);

    /*
     * 
     */
    public void emit();

    /*
     * 
     */
    public String getName();

    /*
     * Only the names of the ports, to help instantiation
     */
    public List<String> getPorts();

    /*
     * 
     */
    public List<String> getInputPorts();

    /*
     * 
     */
    public List<String> getOutputPorts();

    /*
     * 
     
    public String getName();
    
    public List<PortDeclaration> getOutputPorts(); // TODO: i need another class besises PortDeclaration to represent
                                                   // this at this level...
    
    public List<PortDeclaration> getInputPorts();
    
    public List<PortDeclaration> getPorts();*/
}
