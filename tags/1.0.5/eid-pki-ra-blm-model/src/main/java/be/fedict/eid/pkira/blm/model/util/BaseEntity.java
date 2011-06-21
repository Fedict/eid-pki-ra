package be.fedict.eid.pkira.blm.model.util;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

	protected <T> T getComponent(Class<T> componentClass, String name) {
		return getComponentFactory().getComponent(componentClass, name);
	}

	private ComponentFactory getComponentFactory() {
		return ComponentFactory.getCurrentComponentFactory();
	}
}