package com.semantax.module;

import com.semantax.phase.annotator.DefaultTypeAnnotator;
import com.semantax.phase.annotator.DefaultTypeAssignabilityChecker;
import com.semantax.phase.annotator.TypeAnnotator;
import com.semantax.phase.annotator.TypeAssignabilityChecker;
import com.semantax.phase.parser.DefaultPhraseParser;
import com.semantax.phase.parser.PhraseParser;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class PhaseModule {
    @Binds
    public abstract PhraseParser phraseParser(DefaultPhraseParser phraseParser);

    @Binds
    public abstract TypeAssignabilityChecker typeAssignabilityChecker
            (DefaultTypeAssignabilityChecker typeAssignabilityChecker);

    @Binds
    public abstract TypeAnnotator typeAnnotator(DefaultTypeAnnotator typeAnnotator);
}
