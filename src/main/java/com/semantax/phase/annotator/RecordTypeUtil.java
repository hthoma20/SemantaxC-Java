package com.semantax.phase.annotator;

import com.semantax.ast.node.literal.NameParsableExpressionPair;
import com.semantax.ast.node.literal.RecordLit;
import com.semantax.ast.node.literal.type.NameTypeLitPair;
import com.semantax.ast.node.literal.type.RecordTypeLit;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class RecordTypeUtil {

    @Inject
    public RecordTypeUtil() { }

    private Set<String> getDuplicateNames(Stream<String> names) {
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();

        names.forEach(name -> {
            if (seen.contains(name)) {
                duplicates.add(name);
            }
            else {
                seen.add(name);
            }
        });

        return duplicates;
    }
    
    public Set<String> getDuplicateNames(RecordLit recordLit) {
        return getDuplicateNames(recordLit.getNameParsableExpressionPairs().stream()
                .map(NameParsableExpressionPair::getName));
    }
    
    public Set<String> getDuplicateNames(RecordTypeLit recordTypeLit) {
        return getDuplicateNames(recordTypeLit.getNameTypeLitPairs().stream()
                .map(NameTypeLitPair::getName));
    }
    
}
