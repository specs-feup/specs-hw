/**
 *  Copyright 2020 SPeCS.
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

package pt.up.fe.specs.interpretivesimulator;

public class ArithmeticLogicUnit {

    //TODO: Generate flags from specifications
    private boolean flag1;
    private boolean flag2;
    private boolean flag3;
    
    private int output;
    
    public ArithmeticLogicUnit() {
        super();
    }
    //TODO: Might change String operation to Enum OperatorType
    public void processInstruction(int operator_a, int operator_b, String operation) {
        System.out.println(operator_a + operation + operator_b);
        setOutput(0);
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
    
    
}
