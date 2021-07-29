package com.semantax.ast.node;

import com.semantax.ast.node.list.ModuleList;
import com.semantax.ast.node.list.PatternDefinitionList;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.list.WordList;
import com.semantax.ast.visitor.ASTVisitor;

import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.SemantaxParserConstants;
import com.semantax.parser.generated.Token;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.semantax.parser.generated.SemantaxParserConstants.MODULE_MODIFIER;


@Builder(builderClassName = "Builder")
@Getter
public class Module extends AstNode {

    private final Modifier modifier;
    private final String name;
    private final WordList modulesUsed;
    private final ModuleList subModules;
    private final StatementList statements;
    private final PatternDefinitionList patterns;

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Iterate the modules, statements, and patterns contained by this
     * module in the order that they are defined
     * @return a list over the sub-nodes of this module
     */
    public List<AstNode> subNodes() {

        List<AstNode> subNodes = new ArrayList<>();
        subModules.forEach(subNodes::add);
        statements.forEach(subNodes::add);
        patterns.forEach(subNodes::add);

        subNodes.sort(Comparator.comparing(AstNode::getFilePos));

        return subNodes;
    }

    public enum Modifier {
        MAIN, PUBLIC;

        public static Modifier of(Token t) {
            if (!(t.kind == MODULE_MODIFIER)) {
                throw new UnexpectedTokenException(t);
            }

            switch (t.image) {
                case "main":
                    return MAIN;
                case "public":
                    return PUBLIC;
                default:
                    throw new UnexpectedTokenException(t);
            }
        }
    }
}
