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

package pt.up.fe.specs.binarytranslation.graphs.edge;

import pt.up.fe.specs.binarytranslation.instruction.Operand;

public abstract class AGraphEdge implements GraphEdge {

    protected Operand op; // the originating operand
    protected GraphEdgeType etype;
    protected GraphEdgeDirection edirection;
    protected String value;

    public AGraphEdge(Operand op, GraphEdgeDirection edirection, String value) {
        this.op = op;
        this.edirection = edirection;
        this.value = value;
    }

    public GraphEdgeDirection getDirection() {
        return this.edirection;
    }

    public GraphEdgeType getType() {
        return this.etype;
    }

    public String getRepresentation() {
        return this.value;
    }

    public Boolean isImmediate() {
        return this.etype == GraphEdgeType.immediate;
    }

    public Boolean isLivein() {
        return this.etype == GraphEdgeType.livein;
    }

    public Boolean isLiveout() {
        return this.etype == GraphEdgeType.liveout;
    }

    public Boolean isInternal() {
        return this.etype == GraphEdgeType.noderesult;
    }

    /*
     * An edge is modified during the resolving of the graph, when a BinarySegmentGraph is instantiated
     */
    public void setEdgeAs(GraphEdgeType etype, String value) {
        this.etype = etype;
        this.value = value;
    }

    /*
     * Wrapper for Operand method getWidth to reduce verbosity downstream
     */
    public int getWidth() {
        return this.op.getProperties().getWidth();
    }

    /*
     * A graph edge is equal if the representation (getRepresentation) is equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphEdge))
            return false;
        if (obj == this)
            return true;
        return this.getRepresentation().equals(((GraphEdge) obj).getRepresentation());
    }

    /*
     * TODO: Somewhat flimsy.. but it will do for now
     */
    @Override
    public int hashCode() {
        return this.getRepresentation().hashCode() + this.getType().hashCode();
    }
}
