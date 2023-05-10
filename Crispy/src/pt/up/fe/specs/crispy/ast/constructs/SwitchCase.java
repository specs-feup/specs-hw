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

package pt.up.fe.specs.crispy.ast.constructs;

import java.util.List;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.statement.CaseItem;
import pt.up.fe.specs.crispy.ast.statement.HardwareStatement;

public class SwitchCase extends HardwareStatement {

    protected SwitchCase(HardwareNodeType type) {
        super(type);
    }

    /*
     * for copying
     */
    private SwitchCase() {
        this(HardwareNodeType.SwitchCase);
    }

    public SwitchCase(VariableOperator sel) {
        super(HardwareNodeType.CaseItem);
        this.addChild(sel);

    }

    public VariableOperator getCaseSelect() {
        return this.getChild(VariableOperator.class, 0);
    }

    public SwitchCase addCaseItem(CaseItem item) {
        this.addChild(item);
        return this;
    }

    public List<CaseItem> getCasesItems() {
        return this.getChildrenOf(CaseItem.class);
    }

    // TODO: get by case item sel value
    public CaseItem getCaseItem(int idx) {
        return getCasesItems().get(idx);
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();
        builder.append("case(" + this.getCaseSelect().getAsString() + ")\n");
        for (var item : this.getCasesItems())
            builder.append(item.getAsString() + "\n");
        builder.append("default: \n");
        builder.append("endcase\n");
        return builder.toString();
    }

    @Override
    protected SwitchCase copyPrivate() {
        return new SwitchCase();
    }

    @Override
    public SwitchCase copy() {
        return (SwitchCase) super.copy();
    }

    /*
    public List<HardwareModule> cases; // criar lista de casos
    
    public InputPort sel;
    public OutputPort out;
    
    public SwitchCase(int n, int dataWidth) {
        super(SwitchCase.class.getSimpleName());
    
        cases = new ArrayList<HardwareModule>(n); // n e o numero de casos
        sel = addInputPort("sel", cases.size());
    
        // Wires?
    }
    
    public void addCase(int i, HardwareModule mod) { // i e o elemento que vou aceder, index
        if (i < 0 || i >= cases.size()) // validados de dados de entrada
            return;
    
        cases.set(i, mod);
    }
    
    @Override
    public void emit() {
        // emit this
        super.emit();
    
        for (int i = 0; i < cases.size(); ++i) {
            System.out.println("case " + i + ": ");
            cases.get(i).emit();
        }
    }*/
}
