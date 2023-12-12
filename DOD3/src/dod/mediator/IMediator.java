package dod.mediator;

public interface IMediator {
    void addComponent(IMediatorComponent component);
    void notify(IMediatorComponent sender);
}
