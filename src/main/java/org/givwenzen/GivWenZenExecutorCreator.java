package org.givwenzen;

import com.google.common.collect.TreeMultimap;
import org.givwenzen.annotations.InstantiationStrategy;

public class GivWenZenExecutorCreator {
    private String packageName;
    private Object[] state;
    private InstantiationStrategy[] instantiationStrategies = new InstantiationStrategy[0];
    private IDomainStepFinder domainStepFinder;
    private IDomainStepFactory domainStepFactory;
    private Object[] stepState;

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

    private IDomainStepFactory createDomainStepFactory() {
        if (domainStepFactory != null) {
            return domainStepFactory;
        }
        return new DomainStepFactory(instantiationStrategies);
    }

    private IDomainStepFinder createDomainStepFinder() {
        if (domainStepFinder != null) return domainStepFinder;
        if (packageName != null) return new DomainStepFinder(packageName);
        return new DomainStepFinder();
    }

    private Object[] getStepState() {
        return state;
    }

    public GivWenZenExecutorCreator domainStepFinder(IDomainStepFinder domainStepFinder) {
        this.domainStepFinder = domainStepFinder;
        return this;
    }

    public GivWenZenExecutorCreator domainStepFactory(IDomainStepFactory domainStepFactory) {
        this.domainStepFactory = domainStepFactory;
        return this;
    }
}
