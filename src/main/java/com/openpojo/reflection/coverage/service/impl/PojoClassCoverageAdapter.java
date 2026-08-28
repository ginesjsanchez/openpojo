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

package com.openpojo.reflection.coverage.service.impl;

import java.util.HashSet;
import java.util.Set;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.adapt.PojoClassAdapter;

/**
 * Composite adapter: chains the adapters of every detected coverage tool, applying them one after another.
 *
 * @author oshoukry
 */
public class PojoClassCoverageAdapter implements PojoClassAdapter {
	private Set<PojoClassAdapter> adapters = new HashSet<>();

	/**
	 * Adds an adapter to the chain; nulls are ignored.
	 *
	 * @param pojoClassAdapter
	 *     The adapter to chain.
	 */
	public void add(PojoClassAdapter pojoClassAdapter) {
		if (pojoClassAdapter != null) {
			adapters.add(pojoClassAdapter);
		}
	}

	public PojoClass adapt(PojoClass pojoClass) {
		PojoClass adaptedPojoClass = pojoClass;
		for (PojoClassAdapter adapter : adapters)
			adaptedPojoClass = adapter.adapt(adaptedPojoClass);
		return adaptedPojoClass;
	}

	/**
	 * Creates an instance ready to use.
	 */
	public PojoClassCoverageAdapter() {
	}
}
