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

import java.util.ArrayList;
import java.util.List;

public class ArithmeticPatternReport extends APatternReport {
    private List<String> types = new ArrayList<>();
    private static int lastID = 1;

    @Override
    public int getLastID() {
        return lastID;
    }

    @Override
    public void resetLastID() {
        lastID = 1;
    }

    @Override
    public void incrementLastID() {
        lastID++;
    }

    @Override
    public String toCsv() {
        var sb = new StringBuilder();

        for (int i = 0; i < getBasicBlockIDs().size(); i++) {
            var blockTypes = types.get(i);
            var bbid = getBasicBlockIDs().get(i);
            sb.append(this.name + "," + bbid + "," + blockTypes + "\n");
        }
        return sb.toString();
    }
    
    public void addEntry(String blockTypes) {
        types.add(blockTypes);
        getBasicBlockIDs().add("BB" + lastID);
        incrementLastID();
    }
}
