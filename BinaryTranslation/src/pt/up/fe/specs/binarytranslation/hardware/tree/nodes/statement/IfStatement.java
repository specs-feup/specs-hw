package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;

public class IfStatement extends HardwareStatement {

    public IfStatement(HardwareExpression expr) {
        super();
        this.addChild(expr);
        this.type = HardwareNodeType.IfStatement;
    }

    public void addStatement(HardwareStatement stat) {
        this.addChild(stat);
    }

    public HardwareExpression getCondition() {
        return (HardwareExpression) this.getChild(0);
    }

    public List<HardwareStatement> getStatements() {
        var list = this.getChildren();
        var nlist = new ArrayList<HardwareStatement>();
        for (var c : list)
            nlist.add((HardwareStatement) c);
        return nlist;
    }

    @Override
    public String getAsString() {
        var builder = new StringBuilder();

        builder.append("if(" + this.getCondition().getAsString() + ")");

        var stats = this.getStatements();
        if (stats.size() > 1)
            builder.append(" begin");
        builder.append("\n\t");

        for (var child : stats) {
            builder.append("\t" + child.getAsString() + "\n");
        }

        if (this.getStatements().size() > 1)
            builder.append("end");
        builder.append("\n");

        return builder.toString();
    }
}
