package be.fedict.eid.pkira.blm.util;

import java.util.HashMap;
import java.util.Map;

public class MockContext {
	
	private static final MockContext INSTANCE = new MockContext();
	
	private Map<String, Object> context = new HashMap<String, Object>();
	
	private MockContext() {
	}
	
	public static MockContext getInstance() {
		return INSTANCE;
	}
	
	public void reset() {
		context.clear();
	}
	
	public void registerMock(String name, Object object) {
		context.put(name, object);
	}
	
	public Object getMock(String name) {
		return context.get(name);
	}
}

