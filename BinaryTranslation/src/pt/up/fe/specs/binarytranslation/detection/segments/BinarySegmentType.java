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
 
package pt.up.fe.specs.binarytranslation.detection.segments;

public enum BinarySegmentType {

    STATIC_FREQUENT_SEQUENCE,
    STATIC_BASIC_BLOCK,
    TRACE_FREQUENT_SEQUENCE,
    TRACE_BASIC_BLOCK,
    MEGA_BLOCK;

    public Boolean isStatic() {
        return (this == STATIC_FREQUENT_SEQUENCE) || (this == STATIC_BASIC_BLOCK);
    }

    public Boolean isTrace() {
        return (this == TRACE_FREQUENT_SEQUENCE) || (this == MEGA_BLOCK) || (this == TRACE_BASIC_BLOCK);
    }

    public Boolean isAcyclical() {
        return (this == STATIC_FREQUENT_SEQUENCE) || (this == TRACE_FREQUENT_SEQUENCE);
    }

    public Boolean isCyclical() {
        return (this == STATIC_BASIC_BLOCK) || (this == MEGA_BLOCK) || (this == TRACE_BASIC_BLOCK);
    }
}
