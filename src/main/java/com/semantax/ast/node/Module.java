package com.semantax.ast.node;

import com.semantax.ast.node.list.ModuleList;
import com.semantax.ast.node.list.PatternDefinitionList;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.node.list.WordList;
import com.semantax.ast.visitor.AstVisitor;

import com.semantax.exception.UnexpectedTokenException;
import com.semantax.parser.generated.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.semantax.parser.generated.SemantaxParserConstants.MODULE_MODIFIER;


@Builder(builderClassName = "Builder")
@Getter
public class Module extends AstNode {

    private final Modifier modifier;
    @NonNull
    private final String name;

    @lombok.Builder.Default
    private final WordList modulesUsed = new WordList();
    @lombok.Builder.Default
    private final ModuleList subModules = new ModuleList();
    @lombok.Builder.Default
    private final StatementList statements = new StatementList();
    @lombok.Builder.Default
    private final PatternDefinitionList patterns = new PatternDefinitionList();

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
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

    /**
     * Replace the given statement in the list with the statements in the given list.
     * For example, if the current statements are [A, B, C] and
     * `replace(B, [D, E])` is called, then this Module's updated statements will be
     * [A, D, E, C]
     *
     * @param statement the statement to replace
     * @param replacements the statements to substitute in
     * @throws com.semantax.exception.CompilerException if the given statement is not
     *          present in the current statements
     */
    public void replace(Statement statement, StatementList replacements) {
        statements.replace(statement, replacements);
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
