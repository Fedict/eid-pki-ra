package be.fedict.eid.pkira.blm.model.util;

public abstract class ComponentFactory {
	
	private static final ComponentFactory DEFAULT_COMPONENT_FACTORY = new SeamComponentFactory();
	
	private static ComponentFactory currentComponentFactory;
	
	public abstract <T> T getComponent(Class<T> componentClass, String name);
	
	public static ComponentFactory getCurrentComponentFactory() {
		if(currentComponentFactory == null) {
			return DEFAULT_COMPONENT_FACTORY;
		}
		return currentComponentFactory;
	}
	
	public static void setCurrentComponentFactory(ComponentFactory currentComponentFactory) {
		ComponentFactory.currentComponentFactory = currentComponentFactory;
	}

	public static void reset() {
		ComponentFactory.currentComponentFactory = null;
	}
}

