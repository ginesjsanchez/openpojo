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

package com.openpojo.random.collection.list;

import java.util.Arrays;
import java.util.Collection;

import javax.management.relation.RoleList;

import com.openpojo.random.RandomGenerator;
import com.openpojo.random.collection.util.CollectionHelper;
import com.openpojo.random.util.Helper;
import com.openpojo.random.util.SomeRole;

/**
 * Generates a {@code javax.management.relation.RoleList} holding random {@code Role} entries.
 *
 * @author oshoukry
 */
public class RoleListRandomGenerator implements RandomGenerator {
	private static final Class<?>[] TYPES = new Class<?>[]{RoleList.class};
	private static final RoleListRandomGenerator INSTANCE = new RoleListRandomGenerator();

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared generator.
	 */
	public static RoleListRandomGenerator getInstance() {
		return INSTANCE;
	}

	public Collection<Class<?>> getTypes() {
		return Arrays.asList(TYPES);
	}

	public Collection<Object> doGenerate(Class<?> type) {
		Helper.assertIsAssignableTo(type, getTypes());
		return CollectionHelper.buildCollections(new RoleList(), SomeRole.class);
	}

	private RoleListRandomGenerator() {
	}
}
