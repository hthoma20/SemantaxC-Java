package com.semantax.module;

import com.semantax.ast.visitor.AstPrintingVisitor;
import dagger.Module;
import dagger.Provides;

@Module
public class VisitorModule {

    @Provides
    public AstPrintingVisitor astPrintingVisitor() {
        return AstPrintingVisitor.builder().build();
    }
}
