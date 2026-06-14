package mediator;

// Base class — every participant holds a reference to the mediator only
public abstract class Colleague {

    protected LogisticsMediator mediator;

    public Colleague(LogisticsMediator mediator) {
        this.mediator = mediator;
    }

    public abstract String getName();
}
