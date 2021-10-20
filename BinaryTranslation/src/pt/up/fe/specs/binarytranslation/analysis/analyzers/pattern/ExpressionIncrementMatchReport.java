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

import java.util.List;

public class ExpressionIncrementMatchReport extends APatternReport {

    private List<String> memID;
    private List<String> registers;
    private List<String> matches;

    public ExpressionIncrementMatchReport(List<String> bbID, List<String> memID, List<String> registers,
            List<String> matches) {
        this.setBasicBlockIDs(bbID);
        this.memID = memID;
        this.registers = registers;
        this.matches = matches;
    }

    @Override
    public int getLastID() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void resetLastID() {
        // TODO Auto-generated method stub

    }

    @Override
    public void incrementLastID() {
        // TODO Auto-generated method stub

    }

    @Override
    public String toCsv() {
        var sb = new StringBuilder();
        for (var i = 0; i < memID.size(); i++) {
            sb.append(name).append(",");
            sb.append(getBasicBlockIDs().get(i)).append(",");
            sb.append(memID.get(i)).append(",");
            sb.append(registers.get(i)).append(",");
            sb.append(matches.get(i)).append("\n");
        }
        return sb.toString();
    }

}
