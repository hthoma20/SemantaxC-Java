package com.semantax.phase;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Module;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.Program;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.TraversalVisitor;
import com.semantax.exception.CompilerException;
import com.semantax.logger.ErrorLogger;
import com.semantax.phase.annotator.TypeAnnotator;
import com.semantax.phase.parser.PhraseParser;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * This phase is responsible for modifying the given Ast in the following ways:
 *
 * Each Phrase will be parsed
 * Each Expression will be annotated with its type
 *
 */
public class ParsePhase extends TraversalVisitor<Void>
        implements Phase<Program, Program> {

    private final ErrorLogger errorLogger;
    private final PhraseParser phraseParser;
    private final TypeAnnotator typeAnnotator;

    private Module currentModule;

    private final Stack<SymbolTable> symbolTables = new Stack<>();
    private final Stack<List<PatternDefinition>> patterns = new Stack<>();

    @Inject
    public ParsePhase(
            ErrorLogger errorLogger,
            PhraseParser phraseParser,
            TypeAnnotator typeAnnotator) {
        this.errorLogger = errorLogger;
        this.phraseParser = phraseParser;
        this.typeAnnotator = typeAnnotator;
    }

    @Override
    public Optional<Program> process(Program program) {

        Optional<Module> mainModule = mainModule(program);
        if (!mainModule.isPresent()) {
            errorLogger.error(program.getFilePos(), "The given program has no main module");
            return Optional.empty();
        }

        mainModule.get().accept(this);

        return Optional.of(program);
    }

    @Override
    public Void visit(Module module) {
        this.currentModule = module;

        pushNewSymbolTable();
        pushNewPatternList();

        // visit sub-nodes
        super.visit(module);

        symbolTables.pop();
        patterns.pop();

        return null;
    }

    @Override
    public Void visit(PatternDefinition patternDefinition) {
        patterns.peek().add(patternDefinition);
        return null;
    }

    @Override
    public Void visit(ParsableExpression parsableExpression) {

        super.visit(parsableExpression);
        typeAnnotator.visit(parsableExpression);

        Phrase phrase = parsableExpression.getPhrase();

        Optional<AstNode> optionalParsedPhrase =
                phraseParser.parse(phrase, currentPatterns(), symbolTables.peek());

        if (!optionalParsedPhrase.isPresent()) {
            errorLogger.error(parsableExpression.getFilePos(), "Couldn't parse phrase");
            return null;
        }

        AstNode parsedPhrase = optionalParsedPhrase.get();

        if (parsedPhrase instanceof Expression) {
            parsableExpression.parseTo((Expression) parsedPhrase);
        }
        else {
            throw new CompilerException("Unexpected parsed phrase");
        }

        return null;
    }

//    @Override
//    public Void visit(Expression expression) {
//        super.visit(expression);
//        typeAnnotator.annotate(expression);
//        return null;
//    }

    /////////////// Util methods //////////////////////////

    private void pushNewSymbolTable() {
        Optional<SymbolTable> parent = symbolTables.isEmpty() ?
                Optional.empty() : Optional.of(symbolTables.peek());

        symbolTables.push(SymbolTable.builder()
                .parent(parent)
                .build());
    }

    private void pushNewPatternList() {
        patterns.push(new ArrayList<>());
    }

    private List<PatternDefinition> currentPatterns() {
        return patterns.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private Optional<Module> mainModule(Program program) {
        List<Module> modules = program.getModules()
                .stream()
                .filter(module -> module.getModifier() == Module.Modifier.MAIN)
                .collect(Collectors.toList());

        if (modules.size() > 1) {
            errorLogger.error(modules.get(0).getFilePos(), "Found %d main modules, exactly 1 required", modules.size());
            return Optional.empty();
        }

        if (modules.size() == 0 ) {
            errorLogger.error(FilePos.none(), "No main module provided");
            return Optional.empty();
        }

        return Optional.of(modules.get(0));
    }
}
