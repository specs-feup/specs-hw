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

package pt.up.fe.specs.crispy.experimental;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;

public interface HardwareGetterInterface<T> {

    /*
     * *****************************************************************************
     * get statements list
     */
    public default List<T> getStatements() {
        return getBody().getChildren(HardwareStatement.class);
    }

    /*
     * get statement by index
     */
    public default T getStatement(int idx) {
        return getStatements().get(idx);
    }

    /*
     * get statement via predicate
     */
    public default List<T> getStatements(Predicate<T> predicate) {
        return getStatements().stream().filter(predicate).collect(Collectors.toList());
    }
}
