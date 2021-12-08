package com.semantax.phase.codegen;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RecordCodeGenerator {

    @Inject
    public RecordCodeGenerator() { }

    public void generateTypes(CodeEmitter emitter, GeneratedTypeRegistry typeRegistry) {
        // Emit struct declarations
        typeRegistry.getRecordNames().values().forEach(structName -> {
            emitter.emitLine("struct %s;", structName);
        });

        emitter.emitLine("");

        // Emit struct definitions
        // and "constructors"
        typeRegistry.getRecordNames().forEach((recordEntry, structName) -> {
            emitStruct(emitter, recordEntry, structName);
            emitConstructor(emitter, recordEntry, structName);
            emitter.emitLine("");
        });
    }

    private void emitStruct(CodeEmitter emitter, GeneratedTypeRegistry.RecordEntry record, String structName) {
        emitter.emitLine("struct %s : Collectable {", structName);
        emitter.indent();

        record.forEach((fieldName, typeName) -> {
            emitter.emitLine("%s* %s;", typeName, fieldName);
        });

        emitter.unIndent();
        emitter.emitLine("};");
    }

    private void emitConstructor(CodeEmitter emitter, GeneratedTypeRegistry.RecordEntry record, String structName) {
        String parameters = Arrays.stream(record.getMembers())
                .map(member -> String.format("%s* %s", member.getTypeName(), member.getFieldName()))
                .collect(Collectors.joining(", "));

        emitter.emitLine("%s* new_%s(%s) {", structName, structName, parameters);
        emitter.indent();

        emitter.emitLine("%s* obj = (%s*) gcalloc(sizeof(%s), %d);", structName, structName, structName,
                record.getMembers().length);

        record.forEach((fieldName, typeName) -> {
            emitter.emitLine("obj->%s = %s;", fieldName, fieldName);
        });

        emitter.emitLine("return obj;");

        emitter.unIndent();
        emitter.emitLine("}");
    }

}
