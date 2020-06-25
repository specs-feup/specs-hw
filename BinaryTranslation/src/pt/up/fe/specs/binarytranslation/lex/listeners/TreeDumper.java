package pt.up.fe.specs.binarytranslation.lex.listeners;

import org.antlr.v4.runtime.ParserRuleContext;

import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionBaseListener;
import pt.up.fe.specs.binarytranslation.lex.generated.PseudoInstructionParser.PseudoInstructionContext;

public class TreeDumper extends PseudoInstructionBaseListener {

    private static int nestLevel = 0;

    public TreeDumper() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void enterPseudoInstruction(PseudoInstructionContext ctx) {
        this.nestLevel = 0;
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {

        for (int i = 0; i < this.nestLevel; i++)
            System.out.print('\t');

        System.out.println(ctx.getClass().getSimpleName() + " " + ctx.getText());
        this.nestLevel++;
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        this.nestLevel--;
    }
}
