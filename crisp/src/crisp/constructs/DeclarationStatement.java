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

package crisp.constructs;

import crisp.base.Element;

public class DeclarationStatement extends AStatement {

    /*
     * Element can be a definition of data type,
     * declaration of variable, or declaration 
     * of module, or statement, etc
     */
    private Element element;

    public DeclarationStatement(Element el) {
        super(StatementType.declaration);
        this.element = el;
    }

    @Override
    public String emit() {
        // TODO Auto-generated method stub
        return null;
    }
}
