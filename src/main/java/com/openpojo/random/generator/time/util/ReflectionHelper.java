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

package com.openpojo.random.generator.time.util;

import java.lang.reflect.Method;

import com.openpojo.random.exception.RandomGeneratorException;

/**
 * Reflection helpers for the date and time generators, which work with types that may not be present at runtime.
 *
 * @author oshoukry
 */
public class ReflectionHelper {

	/**
	 * Invokes a method obtained by reflection.
	 *
	 * @param method
	 *     The method to invoke.
	 * @param instance
	 *     The instance to invoke it on, or {@code null} if it is static.
	 * @param params
	 *     The call arguments.
	 * @return whatever the method returns.
	 */
	public static Object invokeMethod(Method method, Object instance, Object... params) {
		try {
			return method.invoke(instance, params);
		} catch (Exception e) {
			throw RandomGeneratorException.getInstance(e.getMessage(), e);
		}
	}

	/**
	 * Looks a method up by name and parameter types, without propagating the failure if it is not there.
	 *
	 * @param onClass
	 *     The class to search in.
	 * @param methodName
	 *     The method name.
	 * @param types
	 *     The types of its parameters.
	 * @return the method, or {@code null} if it is not there.
	 */
	public static Method getMethod(Class<?> onClass, String methodName, Class<?>... types) {
		try {
			return onClass.getMethod(methodName, types);
		} catch (Exception e) {
			throw RandomGeneratorException.getInstance(e.getMessage(), e);
		}
	}

	private ReflectionHelper() {
		throw new UnsupportedOperationException(ReflectionHelper.class.getName() + " should not be constructed!");
	}
}
