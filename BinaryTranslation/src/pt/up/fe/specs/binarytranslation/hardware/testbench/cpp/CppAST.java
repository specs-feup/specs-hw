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

package pt.up.fe.specs.binarytranslation.hardware.testbench.cpp;

import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.CppNode;
import pt.up.fe.specs.binarytranslation.hardware.testbench.cpp.nodes.statement.CppNullStatement;

/** This C/C++ AST is being developed to be used for generating Verilator testbenchs and module validation data, and possibly gem5 decoder.isa files (they use a C++ syntax)<br>
 * The AST is a direct copy of the Clang's AST. This is because instead of making an AST specific for the current needs, a more generic AST is implemented and can be extended in the future for whatever C++ needs may arise.<br> 
 * Note that this AST will be barely functional and most of Clang's functions will not be implemented, and I'm not sure about the legality of this approach, but this approach seems the wisest to me 
 * <br>
 * Nodes I intend to implement: Variable declarations and reference, functions, class declarations, #include, all basic statements (if, for, etc) and some base C/C++ functions (printf/cout, etc)
 * <br>
 * My idea is to implement a C++ file emiter, that emits a file that contains a Verilator testbench that interacts directly with the HDL module (no janky middle HDL testbench like now) and it generates the validation data for the validation of the module (the current implementation will always be very slow and require extensive resources, doing it all in C++ is way better)
 * 
 * @author João Conceição
 *
 */
public class CppAST {

    private CppNode rootNode;
    
    public CppAST() {
        this.rootNode = new CppNullStatement();
    }
    
    public CppNode getRootNode() {
        return this.rootNode;
    }
    
    
 

}
