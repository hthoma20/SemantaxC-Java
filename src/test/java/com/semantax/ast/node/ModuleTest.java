package com.semantax.ast.node;

import com.semantax.ast.node.list.ModuleList;
import com.semantax.ast.node.list.PatternDefinitionList;
import com.semantax.ast.node.list.StatementList;
import com.semantax.ast.util.FilePos;
import junit.framework.TestCase;

import java.util.List;

public class ModuleTest extends TestCase {

    public void test_subNodes() {
        Statement first = Statement.builder().buildWith(new FilePos(1,1));
        Module second = Module.builder().name("testModule").build();
        second.setFilePos(new FilePos(2, 1));
        Statement third = Statement.builder().buildWith(new FilePos(3,1));
        PatternDefinition fourth = PatternDefinition.builder().buildWith(new FilePos(4, 1));

        StatementList statements = new StatementList();
        statements.add(first);
        statements.add(third);

        ModuleList modules = new ModuleList();
        modules.add(second);

        PatternDefinitionList patterns = new PatternDefinitionList();
        patterns.add(fourth);

        Module module = Module.builder()
                    .statements(statements)
                    .subModules(modules)
                    .patterns(patterns)
                    .name("testModule")
                    .build();

        List<AstNode> subNodes = module.subNodes();

        assertEquals(4, subNodes.size());
        assertEquals(first, subNodes.get(0));
        assertEquals(second, subNodes.get(1));
        assertEquals(third, subNodes.get(2));
        assertEquals(fourth, subNodes.get(3));
    }
}
