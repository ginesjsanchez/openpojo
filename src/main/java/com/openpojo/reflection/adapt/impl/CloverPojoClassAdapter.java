/*
 * Copyright (c) 2010-2018 Osman Shoukry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openpojo.reflection.adapt.impl;

import java.util.ArrayList;
import java.util.List;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoField;
import com.openpojo.reflection.adapt.PojoClassAdapter;
import com.openpojo.reflection.impl.PojoClassImpl;

/**
 * Hides the fields Clover injects when instrumenting the bytecode, so they are not validated as if they belonged to
 * the POJO.
 *
 * @author oshoukry
 */
public class CloverPojoClassAdapter implements PojoClassAdapter {

	private static final String CLOVER_INJECTED = "__CLR";

	private CloverPojoClassAdapter() {
	}

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared instance.
	 */
	public static PojoClassAdapter getInstance() {
		return Instance.INSTANCE;
	}

	public PojoClass adapt(PojoClass pojoClass) {
		final List<PojoField> cleansedPojoFields = new ArrayList<>();
		for (final PojoField pojoField : pojoClass.getPojoFields()) {
			if (!pojoField.getName().startsWith(CLOVER_INJECTED)) {
				cleansedPojoFields.add(pojoField);
			}
		}
		return new PojoClassImpl(pojoClass.getClazz(), cleansedPojoFields, pojoClass.getPojoMethods());
	}

	private static class Instance {
		private static final CloverPojoClassAdapter INSTANCE = new CloverPojoClassAdapter();
	}
}
