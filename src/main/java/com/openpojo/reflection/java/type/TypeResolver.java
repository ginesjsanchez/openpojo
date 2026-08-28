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

package com.openpojo.reflection.java.type;

import java.lang.reflect.Type;

/**
 * Resolves a specific {@code Type} from the Java reflection API to its effective type and its parameters.
 *
 * @param <T>
 *     The kind of {@code Type} this implementation knows how to handle.
 * @author oshoukry
 */
public interface TypeResolver<T extends Type> {

	/**
	 * Resolves the type to its effective form.
	 *
	 * @param type
	 *     The type to resolve.
	 * @return the resolved type.
	 */
	Type resolveType(T type);

	/**
	 * The type enclosing the given one, for instance the raw type of a parameterized one.
	 *
	 * @param type
	 *     The type to inspect.
	 * @return the enclosing type.
	 */
	Type getEnclosingType(T type);

	/**
	 * The type arguments of the given type.
	 *
	 * @param type
	 *     The type to inspect.
	 * @return its parameters, empty if it has none.
	 */
	Type[] getParameterTypes(T type);
}
