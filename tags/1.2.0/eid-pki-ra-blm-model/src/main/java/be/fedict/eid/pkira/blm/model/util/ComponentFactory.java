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

