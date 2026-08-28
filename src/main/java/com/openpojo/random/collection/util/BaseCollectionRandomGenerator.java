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

package com.openpojo.random.collection.util;

import java.util.Collection;

import com.openpojo.random.ParameterizableRandomGenerator;
import com.openpojo.random.util.SerializableComparableObject;
import com.openpojo.reflection.Parameterizable;

/**
 * Base for the collection generators. It leaves the creation of the empty instance to the subclass and takes care of
 * populating it with random elements of the requested type.
 *
 * @author oshoukry
 */
public abstract class BaseCollectionRandomGenerator implements ParameterizableRandomGenerator {

	@Override
	public Collection<Object> doGenerate(Class<?> type) {
		return CollectionHelper.buildCollections(getBasicInstance(type), SerializableComparableObject.class);
	}

	@Override
	public Collection<Object> doGenerate(Parameterizable parameterizedType) {
		return CollectionHelper.buildCollections(doGenerate(parameterizedType.getType()),
				parameterizedType.getParameterTypes().get(0));
	}

	@Override
	public abstract Collection<Class<?>> getTypes();

	/**
	 * Creates the empty collection of the concrete type the subclass handles; populating it is this base class job.
	 *
	 * @param type
	 *     The requested type.
	 * @return the empty collection.
	 */
	protected abstract Collection<Object> getBasicInstance(Class<?> type);

	/**
	 * Constructor reachable only from subclasses.
	 */
	protected BaseCollectionRandomGenerator() {
	}
}
