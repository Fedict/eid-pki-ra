/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */

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

