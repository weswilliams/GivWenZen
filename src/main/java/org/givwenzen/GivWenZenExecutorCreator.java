package org.givwenzen;

import org.givwenzen.annotations.InstantiationStrategy;

public class GivWenZenExecutorCreator {
    private String packageName;
    private Object[] state;
    private InstantiationStrategy[] instantiationStrategies = new InstantiationStrategy[0];

    public static GivWenZenExecutorCreator instance() {
        return new GivWenZenExecutorCreator();
    }

    public GivWenZenExecutorCreator stepClassBasePackage(String packageName) {
        this.packageName = packageName;
        return this;
    }

//
//   public GivWenZenExecutorCreator customParserFinder(ICustomParserFinder finder) {
//      this.finder = finder;
//      return this;
//   }

    public GivWenZenExecutorCreator customStepState(Object... state) {
        this.state = state;
        return this;
    }

    public GivWenZenExecutorCreator customInstantiationStrategies(InstantiationStrategy... instantiationStrategy) {
        this.instantiationStrategies = instantiationStrategy;
        return this;
    }

    public GivWenZenExecutor create() {
        return new GivWenZenExecutor(createDomainStepFinder(), createDomainStepFactory(), getStepState());
    }

    private DomainStepFactory createDomainStepFactory() {
        return new DomainStepFactory(instantiationStrategies);
    }

    private DomainStepFinder createDomainStepFinder() {
        if (packageName != null) return new DomainStepFinder(packageName);
        return new DomainStepFinder();
    }

    private Object[] getStepState() {
        return state;
    }
}
