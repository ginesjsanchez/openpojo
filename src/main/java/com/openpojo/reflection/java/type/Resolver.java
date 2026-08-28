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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import com.openpojo.reflection.java.type.impl.GenericArrayTypeResolver;
import com.openpojo.reflection.java.type.impl.NoResolveTypeResolver;
import com.openpojo.reflection.java.type.impl.ParameterizedTypeResolver;
import com.openpojo.reflection.java.type.impl.TypeVariableResolver;
import com.openpojo.reflection.java.type.impl.WildcardTypeResolver;

/**
 * Entry point to type resolution: given any {@code Type} from the Java reflection API, it dispatches to the {@code
 * TypeResolver} that knows how to handle it and returns the effective type or its parameters.
 *
 * @author oshoukry
 */
public class Resolver {
	private static final WildcardTypeResolver WILDCARD_TYPE_RESOLVER = new WildcardTypeResolver();
	private static final ParameterizedTypeResolver PARAMETERIZED_TYPE_RESOLVER = new ParameterizedTypeResolver();
	private static final TypeVariableResolver TYPE_VARIABLE_RESOLVER = new TypeVariableResolver();
	private static final GenericArrayTypeResolver GENERIC_ARRAY_TYPE_RESOLVER = new GenericArrayTypeResolver();
	private static final NoResolveTypeResolver NO_RESOLVE_TYPE_RESOLVER = new NoResolveTypeResolver();

	/**
	 * Resolves a type to its effective form, undoing wildcards and type variables.
	 *
	 * @param type
	 *     The type to resolve.
	 * @return the resolved type.
	 */
	public static Type resolve(Type type) {
		return getTypeResolver(type).resolveType(type);
	}

	/**
	 * The type enclosing the given one.
	 *
	 * @param type
	 *     The type to inspect.
	 * @return the enclosing type.
	 */
	public static Type getEnclosingType(Type type) {
		return getTypeResolver(type).getEnclosingType(type);
	}

	/**
	 * The type arguments of the given type.
	 *
	 * @param type
	 *     The type to inspect.
	 * @return its parameters, empty if it has none.
	 */
	public static Type[] getParameterTypes(Type type) {
		return getTypeResolver(type).getParameterTypes(type);
	}

	/**
	 * Dispatch is by instanceof, so the match between the concrete Type and the {@code TypeResolver<T>} that handles
	 * it cannot be checked by the compiler. This is the only place where that happens, which is why the unchecked
	 * conversion is concentrated here instead of being repeated in every public method.
	 */
	@SuppressWarnings("unchecked")
	private static TypeResolver<Type> getTypeResolver(Type type) {
		final TypeResolver<?> typeResolver;
		if (type instanceof WildcardType)
			typeResolver = WILDCARD_TYPE_RESOLVER;
		else if (type instanceof ParameterizedType)
			typeResolver = PARAMETERIZED_TYPE_RESOLVER;
		else if (type instanceof TypeVariable)
			typeResolver = TYPE_VARIABLE_RESOLVER;
		else if (type instanceof GenericArrayType)
			typeResolver = GENERIC_ARRAY_TYPE_RESOLVER;
		else
			typeResolver = NO_RESOLVE_TYPE_RESOLVER;
		return (TypeResolver<Type>) typeResolver;
	}

	private Resolver() {
		throw new UnsupportedOperationException(Resolver.class.getName() + " should not be constructed!");
	}
}
