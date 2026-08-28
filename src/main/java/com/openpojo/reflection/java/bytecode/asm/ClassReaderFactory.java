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

package com.openpojo.reflection.java.bytecode.asm;

import org.objectweb.asm.ClassReader;

import com.openpojo.reflection.exception.ReflectionException;
import com.openpojo.reflection.java.Java;

/**
 * Creates the ASM {@code ClassReader} from the .class resource of a class.
 *
 * @author oshoukry
 */
public class ClassReaderFactory {

	private ClassReaderFactory() {
		throw new UnsupportedOperationException(ClassReaderFactory.class.getName() + " should not be constructed!");
	}

	/**
	 * Opens the bytecode of a class so that ASM can read it.
	 *
	 * @param clazz
	 *     The class whose bytecode is to be read.
	 * @return the reader positioned on that class.
	 */
	public static ClassReader getClassReader(Class<?> clazz) {
		try {
			return new ClassReader(clazz.getResourceAsStream(Java.PATH_DELIMITER
					+ clazz.getName().replace(Java.PACKAGE_DELIMITER, Java.PATH_DELIMITER) + Java.CLASS_EXTENSION));
		} catch (Throwable t) {
			throw ReflectionException.getInstance("Failed to create ClassReader for class [" + clazz + "]", t);
		}
	}

}
