package com.semantax.phase.codegen;

import com.semantax.ast.type.ArrayType;
import com.semantax.ast.type.BoolType;
import com.semantax.ast.type.IntType;
import com.semantax.ast.type.RecordType;
import com.semantax.ast.type.StringType;
import com.semantax.ast.visitor.BaseAstVisitor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Stores names for types that need to be generated
 */
public class GeneratedTypeRegistry extends BaseAstVisitor<String> {

    private int recordIndex = 0;
    @Getter
    private final Map<RecordEntry, String> recordNames = new HashMap<>();

    /**
     * Get the name that has been registered for the given type.
     * If no name exists, register one and return it
     * @param recordType the type to get a name for
     * @return the name of the type that should be used in generated code
     */
    public String visit(RecordType recordType) {
        RecordEntry entry = new RecordEntry(recordType);
        return recordNames.computeIfAbsent(entry, e ->
                e.getMembers().length == 0 ? "record_empty" : "record_" + recordIndex++);
    }

    public String visit(IntType intType) {
        return "Int";
    }

    public String visit(BoolType boolType) {
        return "Bool";
    }

    public String visit(ArrayType arrayType) {
        return "Array";
    }

    public String visit(StringType stringType) {
        return "String";
    }


    /**
     * Represents an abstraction of a RecordType. All record types that match in
     * name a structure can use the same struct in generated code. This data
     * object represents a distinct structure
     */
    @EqualsAndHashCode
    public class RecordEntry {

        @Value
        public class RecordMember {
            private String fieldName;
            private String typeName;
        }

        @Getter
        private final RecordMember[] members;

        public RecordEntry(RecordType representedType) {
            this.members = representedType.getNameTypePairs().stream()
                    .map(nameTypePair -> {
                        String typeName = nameTypePair.getType().accept(GeneratedTypeRegistry.this);
                        return new RecordMember(nameTypePair.getName(), typeName);
                    })
                    .toArray(RecordMember[]::new);
        }

        public void forEach(BiConsumer<String, String> consumer) {
            for (RecordMember member : members) {
                consumer.accept(member.fieldName, member.typeName);
            }
        }
    }

}
