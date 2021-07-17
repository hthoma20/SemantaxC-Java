package com.semantax.module;

import com.semantax.main.Runner;
import dagger.Component;

import javax.inject.Singleton;

public class Main {

    @Singleton
    @Component(modules = {
            VisitorModule.class
    })
    public interface SemantaxComponent {
        Runner getRunner();
    }

    public static void main(String[] args) {
        SemantaxComponent component = DaggerMain_SemantaxComponent.builder().build();
        Runner runner = component.getRunner();
        runner.run(args);
    }


}
