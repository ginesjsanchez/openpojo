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
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openpojo.cache.CacheStorage;
import com.openpojo.cache.CacheStorageFactory;
import com.openpojo.reflection.exception.ReflectionException;

/**
 * Entry point to subclass generation with ASM. It caches the subclasses already generated so a new class is not
 * created on every request.
 *
 * @author oshoukry
 */
public class ASMService {
	private SimpleClassLoader simpleClassLoader = new SimpleClassLoader();
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private CacheStorage<Class<?>> alreadyGeneratedClasses = CacheStorageFactory.getPersistentCacheStorage();

	private ASMService() {
	}

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared instance.
	 */
	public static ASMService getInstance() {
		return Instance.INSTANCE;
	}

	/**
	 * Generates a subclass of the given class with ASM, reusing the one already generated if there is one.
	 *
	 * @param <T>
	 *     The type of the base class.
	 * @param clazz
	 *     The class to extend.
	 * @return the generated subclass.
	 */
	public <T> Class<? extends T> createSubclassFor(Class<T> clazz) {
		SubClassDefinition subClassDefinition = new DefaultSubClassDefinition(clazz);
		return createSubclassFor(clazz, subClassDefinition);
	}

	/**
	 * Generates a subclass following a specific definition.
	 *
	 * @param <T>
	 *     The type of the base class.
	 * @param clazz
	 *     The class to extend.
	 * @param subClassDefinition
	 *     The definition of the subclass to generate.
	 * @return the generated subclass.
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<? extends T> createSubclassFor(Class<T> clazz, SubClassDefinition subClassDefinition) {
		Class<? extends T> generatedClass = (Class<? extends T>) alreadyGeneratedClasses
				.get(subClassDefinition.getGeneratedClassName());

		if (generatedClass != null) {
			logger.info("Reusing already generated sub-class for class [{}]", clazz.getName());
		} else {
			try {
				generatedClass = (Class<? extends T>) simpleClassLoader.loadThisClass(
						getSubClassByteCode(subClassDefinition), subClassDefinition.getGeneratedClassName());
				alreadyGeneratedClasses.add(subClassDefinition.getGeneratedClassName(), generatedClass);
			} catch (Throwable throwable) {
				throw ReflectionException.getInstance("Failed to create subclass for class: " + clazz, throwable);
			}
		}
		return generatedClass;
	}

	private byte[] getSubClassByteCode(SubClassDefinition subClassDefinition) {

		ClassReader classReader = subClassDefinition.getClassReader();
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		SubClassCreator subClassCreator = new SubClassCreator(cw, subClassDefinition.getGeneratedClassNameAsJDKPath());
		classReader.accept(subClassCreator, 0);

		cw.visitEnd();
		return cw.toByteArray();
	}

	private static class Instance {
		private static final ASMService INSTANCE = new ASMService();
	}
}
