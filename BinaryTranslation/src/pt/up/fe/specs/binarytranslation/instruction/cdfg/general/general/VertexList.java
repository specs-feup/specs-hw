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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 
 * @author João Conceição
 *
 * @param <V> Class of the vertices in the list
 */
public class VertexList<V> extends ArrayList<V>{

    public void set(Collection<V> vertices) {
        this.clear();
        this.add(vertices);
    }
    
    public V get(V vertex) {
        return this.stream().filter(v -> vertex.equals(v)).findFirst().orElse(null);
    }
    
    public List<V> get(Predicate<V> predicate){
        return this.stream().filter(predicate).collect(Collectors.toList());
    }
    
    public boolean replace(V vertex) {   
        return (this.remove(this.get(vertex))) ? super.add(vertex) : false;
    }
    
    @Override
    public boolean add(V vertex) {
        return (this.contains(vertex)) ? this.replace(vertex) : super.add(vertex);
    }
    
    public boolean add(Collection<V> vertices) {
        vertices.forEach(v -> this.add(v));
        return true;
    }
}
