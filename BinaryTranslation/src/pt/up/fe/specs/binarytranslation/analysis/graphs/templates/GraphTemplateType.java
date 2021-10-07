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

package pt.up.fe.specs.binarytranslation.analysis.graphs.templates;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;

public enum GraphTemplateType {
    TYPE_0(new GraphTemplateType0()),
    TYPE_1(new GraphTemplateType1()),
    TYPE_2(new GraphTemplateType2()),
    TYPE_3(new GraphTemplateType3()),
    TYPE_4(new GraphTemplateType4()),
    TYPE_5(new GraphTemplateType5()),
    TYPE_6(new GraphTemplateType6()),
    TYPE_7(new GraphTemplateType7()),
    TYPE_8(new GraphTemplateType8()),
    TYPE_9(new GraphTemplateType9()),
    TYPE_10(new GraphTemplateType10()),
    TYPE_11(new GraphTemplateType11()),
    TYPE_12(new GraphTemplateType12()),
    TYPE_13(new GraphTemplateType13()),
    TYPE_14(new GraphTemplateType14());
    
    private AGraphTemplate template;

    GraphTemplateType(AGraphTemplate template) {
        this.template = template;
    }
    
    public AGraphTemplate getTemplate() {
        return template;
    }
    
    public SimpleDirectedGraph<BtfVertex, DefaultEdge> getTemplateGraph() {
        return template.getGraph();
    }
    
    public static String getAllTemplates() {
        var templates = new StringBuilder();
        for (var type : GraphTemplateType.values())
            templates.append(type.getTemplate().toString()).append("\n");
        return templates.toString();
    }
}
