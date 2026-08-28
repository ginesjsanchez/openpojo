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

package com.openpojo.reflection.service;

import java.util.List;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.registry.Service;

/**
 * A Service to lookup java classes.
 *
 * @author oshoukry
 */
public interface PojoClassLookupService extends Service {

	/**
	 * Searches a package for the classes deriving from the given type.
	 *
	 * @param packageName
	 *     The package to search in.
	 * @param type
	 *     The type they must derive from.
	 * @param pojoClassFilter
	 *     An extra filter, or {@code null}.
	 * @return the classes meeting both conditions.
	 */
	List<PojoClass> enumerateClassesByExtendingType(final String packageName, final Class<?> type,
			final PojoClassFilter pojoClassFilter);

	/**
	 * Wraps a Java class into the openpojo abstraction.
	 *
	 * @param clazz
	 *     The class to wrap.
	 * @return its {@code PojoClass}.
	 */
	PojoClass getPojoClass(final Class<?> clazz);

	/**
	 * The classes of a package, without descending into its sub-packages.
	 *
	 * @param packageName
	 *     The package to read.
	 * @return the classes found.
	 */
	List<PojoClass> getPojoClasses(final String packageName);

	/**
	 * The classes of a package that pass the filter, without descending into its sub-packages.
	 *
	 * @param packageName
	 *     The package to read.
	 * @param pojoClassFilter
	 *     The filter to apply, or {@code null}.
	 * @return the classes that pass the filter.
	 */
	List<PojoClass> getPojoClasses(final String packageName, final PojoClassFilter pojoClassFilter);

	/**
	 * The classes of a package and all its sub-packages that pass the filter.
	 *
	 * @param packageName
	 *     The starting package.
	 * @param pojoClassFilter
	 *     The filter to apply, or {@code null}.
	 * @return the classes that pass the filter.
	 */
	List<PojoClass> getPojoClassesRecursively(final String packageName, final PojoClassFilter pojoClassFilter);

}
