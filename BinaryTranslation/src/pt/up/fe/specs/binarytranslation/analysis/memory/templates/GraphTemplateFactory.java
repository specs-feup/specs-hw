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

package pt.up.fe.specs.binarytranslation.analysis.memory.templates;

public class GraphTemplateFactory {
    public static AGraphTemplate getTemplate(GraphTemplateType type) {
        switch(type) {
        case TYPE_1:
            return new GraphTemplateType1(type);
        case TYPE_2:
            return new GraphTemplateType2(type);
        case TYPE_3:
            return new GraphTemplateType3(type);
        case TYPE_4:
            return new GraphTemplateType4(type);
        case TYPE_5:
            return new GraphTemplateType5(type);
        case TYPE_6:
            return new GraphTemplateType6(type);
        case TYPE_7:
            return new GraphTemplateType7(type);
        case TYPE_8:
            return new GraphTemplateType8(type);
        case TYPE_9:
            return new GraphTemplateType9(type);
        case TYPE_10:
            return new GraphTemplateType10(type);
        case TYPE_11:
            return new GraphTemplateType11(type);
        case TYPE_0:
            return new GraphTemplateType0(type);
        default:
            return null;
        }
    }
}
