package com.semantax.phase;

import com.semantax.ast.node.AstNode;
import com.semantax.ast.node.Expression;
import com.semantax.ast.node.Module;
import com.semantax.ast.node.ParsableExpression;
import com.semantax.ast.node.PhraseElement;
import com.semantax.ast.node.VariableReference;
import com.semantax.ast.node.Word;
import com.semantax.ast.node.literal.FunctionLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.literal.type.TypeLit;
import com.semantax.ast.node.pattern.PatternDefinition;
import com.semantax.ast.node.Phrase;
import com.semantax.ast.node.Program;
import com.semantax.ast.node.progcall.DeclProgCall;
import com.semantax.ast.type.Type;
import com.semantax.ast.util.FilePos;
import com.semantax.ast.visitor.TraversalVisitor;
import com.semantax.error.ErrorType;
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
    private boolean failedParse = false;

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
            return Optional.empty();
        }

        mainModule.get().accept(this);

        if (failedParse) {
            return Optional.empty();
        }
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
        super.visit(patternDefinition);
        return null;
    }

    @Override
    public Void visit(FunctionLit function) {

        function.getInput().accept(typeAnnotator);
        function.getOutput().ifPresent(output -> output.accept(typeAnnotator));

        pushNewSymbolTable();

        SymbolTable symbolTable = symbolTables.peek();

        for (NameTypeLitPair nameTypeLitPair : function.getInput().getNameTypeLitPairs()) {
            symbolTable.add(nameTypeLitPair.getName(), nameTypeLitPair);
        }

        super.visit(function);

        symbolTables.pop();
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
            errorLogger.error(ErrorType.UNPARSABLE_PHRASE, parsableExpression.getFilePos(), "Couldn't parse phrase");
            failedParse = true;
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

    @Override
    public Void visit(DeclProgCall declProgCall) {

        if (declProgCall.getSubExpressions().size() != 2) {
            errorLogger.error(ErrorType.ILLEGAL_DECL, declProgCall.getFilePos(),
                    "%s expects exactly two arguments: a name and type.",declProgCall.getName());
            failedParse = true;
            return null;
        }

        Optional<String> variableName = getVariableName(declProgCall);

        declProgCall.getSubExpressions().get(1).accept(this);

        Optional<Type> variableType = getVariableType(declProgCall);

        if (!variableName.isPresent() || !variableType.isPresent()) {
            errorLogger.error(ErrorType.ILLEGAL_DECL, declProgCall.getFilePos(),
                    "%s expects a name and type.", declProgCall.getName());
            failedParse = true;
            return null;
        }

        declProgCall.setDeclName(variableName.get());
        declProgCall.setDeclType(variableType.get());

        symbolTables.peek().add(variableName.get(), declProgCall);
        return null;
    }

    /////////////// Util methods //////////////////////////

    /**
     * Get the variable name in the decl if it exists
     * otherwise return Optional.empty()
     */
    private Optional<String> getVariableName(DeclProgCall declProgCall) {

        if (declProgCall.getSubExpressions().size() < 1 ||
                declProgCall.getSubExpressions().get(0).getPhrase().getPhrase().size() != 1) {
            return Optional.empty();
        }

        ParsableExpression nameExpression = declProgCall.getSubExpressions().get(0);
        PhraseElement arg = nameExpression.getPhrase().getPhrase().get(0);
        if (!(arg instanceof Word)) {
            return Optional.empty();
        }

        nameExpression.parseTo(VariableReference.builder()
            .declaration(declProgCall)
            .buildWith(nameExpression.getFilePos()));

        return Optional.of(((Word) arg).getValue());
    }

    /**
     * Get the variable type in the decl if it exists
     * otherwise return Optional.empty()
     */
    private Optional<Type> getVariableType(DeclProgCall declProgCall) {
        if (declProgCall.getSubExpressions().size() < 2 ||
                !(declProgCall.getSubExpressions().get(1).getExpression() instanceof TypeLit)) {
            return Optional.empty();
        }
        Type type = ((TypeLit) declProgCall.getSubExpressions().get(1).getExpression()).getRepresentedType();
        return Optional.of(type);
    }

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
            errorLogger.error(ErrorType.MULTIPLE_MAIN_MODULES, modules.get(0).getFilePos(),
                    "Found %d main modules, exactly 1 required", modules.size());
            return Optional.empty();
        }

        if (modules.size() == 0 ) {
            errorLogger.error(ErrorType.MISSING_MAIN_MODULE, FilePos.none(), "No main module provided");
            return Optional.empty();
        }

        return Optional.of(modules.get(0));
    }
}
