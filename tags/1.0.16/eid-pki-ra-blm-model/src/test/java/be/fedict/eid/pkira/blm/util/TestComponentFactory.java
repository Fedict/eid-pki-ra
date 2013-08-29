package be.fedict.eid.pkira.blm.util;

import be.fedict.eid.pkira.blm.model.util.ComponentFactory;

public class TestComponentFactory extends ComponentFactory {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<T> componentClass, String name) {
		return (T) MockContext.getInstance().getMock(name);
	}
}
