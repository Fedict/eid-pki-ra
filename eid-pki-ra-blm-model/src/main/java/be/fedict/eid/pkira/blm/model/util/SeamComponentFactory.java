package be.fedict.eid.pkira.blm.model.util;

import org.jboss.seam.Component;

public class SeamComponentFactory extends ComponentFactory {

	@Override
	public <T> T getComponent(Class<T> componentClass, String name) {
		return (T) Component.getInstance(name);
	}

}
