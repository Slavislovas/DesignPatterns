package dod.mediator;

import dod.mediator.IMediator;
import dod.mediator.IMediatorComponent;

import java.util.*;

public class ItemMediator implements IMediator {

    private final List<IMediatorComponent> _components = new ArrayList<>();

    @Override
    public void addComponent(IMediatorComponent component) {
        _components.add(component);
    }

    @Override
    public void notify(IMediatorComponent sender) {
        for (IMediatorComponent component : _components) {
            if(!component.getType().equals(sender.getType())) {
                component.act();
            }
        }
    }
}
