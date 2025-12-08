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

package com.openpojo.reflection.java.packageloader.env;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoMethod;
import com.openpojo.reflection.impl.PojoClassFactory;

/**
 * @author oshoukry
 */
public class JavaClassPathClassLoaderTest {

	private Integer minExpectedTotalClasses;
	private JavaClassPathClassLoader javaClassPathClassLoader;
	private int minJavaUtilConcurrentAtomicCount;
	private int minJavaLangClasses;
	private int minPackageCountUnderJava;

	private static final String JAVA_VERSION = System.getProperty("java.version");

	@BeforeEach
	public void setup() {
		javaClassPathClassLoader = JavaClassPathClassLoader.getInstance();
		if (JAVA_VERSION.startsWith("1.8")) {
			minExpectedTotalClasses = 20000;
			minJavaUtilConcurrentAtomicCount = 30;
			minJavaLangClasses = 400;
			minPackageCountUnderJava = 13;
		} else {
			minExpectedTotalClasses = 16000;
			minJavaUtilConcurrentAtomicCount = 17;
			minJavaLangClasses = 230;
			minPackageCountUnderJava = 10;
		}
	}

	@Test
	public void onlyPrivateConstructors() {
		PojoClass pojoClass = PojoClassFactory.getPojoClass(JavaClassPathClassLoader.class);
		for (PojoMethod constructor : pojoClass.getPojoConstructors())
			Assertions.assertTrue(constructor.isPrivate());
	}

	@Test
	public void shouldNotThrowExceptionOnInvalidClassPathProperty() {
		JavaClassPathClassLoader instance = JavaClassPathClassLoader.getInstance("InvalidClassPathPropertyName");
		// Assertions.//assertThat(instance, notNullValue());
		// Assertions.//assertThat(instance.getClassNames().size(), is(0));
	}

	@Test
	public void canGetInstance() {
		JavaClassPathClassLoader instance = JavaClassPathClassLoader.getInstance();
		// Assertions.//assertThat(instance, notNullValue());
	}

	@Test
	public void whenPackageNameIsNullReturnEmptyClassSet() {
		Set<Type> classes = javaClassPathClassLoader.getTypesInPackage(null);
		// Assertions.//assertThat(classes, notNullValue());
		// Assertions.//assertThat(classes.size(), is(0));
	}

	@Test
	public void defaultClassPathVars() {
		String[] expectedClassPathKeys = { "java.library.path", "java.class.path", "java.ext.dirs",
				"sun.boot.class.path" };

		Set<String> classPathKeys = javaClassPathClassLoader.getClassPathKeys();

		// Assertions.//assertThat(classPathKeys, notNullValue());
		// Assertions.//assertThat(classPathKeys.size(),
		// is(expectedClassPathKeys.length));
		// Assertions.//assertThat(classPathKeys,
		// containsInAnyOrder(expectedClassPathKeys));
	}

	@Test
	public void canGetAllClassNamesInBootClassPath() {
		Set<String> classNames = javaClassPathClassLoader.getClassNames();
		// Assertions.//assertThat(classNames, notNullValue());
		// Assertions.//assertThat(classNames.size(),
		// greaterThan(minExpectedTotalClasses));
	}

	@Test
	public void canLoadAllClassesInJavaUtilConcurrentAtomic() {
		String concurrentPackageName = AtomicInteger.class.getPackage().getName();
		Set<Type> classesInPackage = javaClassPathClassLoader.getTypesInPackage(concurrentPackageName);
		// Assertions.//assertThat(classesInPackage.size(),
		// greaterThan(minJavaUtilConcurrentAtomicCount));
	}

	@Test
	public void canGetPackageNamesUnderGivenPackageName() {
		Set<String> subPackages = javaClassPathClassLoader.getSubPackagesFor("java");
		// Assertions.//assertThat(subPackages, notNullValue());
		// Assertions.//assertThat(subPackages.size(),
		// greaterThan(minPackageCountUnderJava));
	}

	@Test
	public void willReturnTrueForJavaPackageExists() {
		// Assertions.//assertThat(javaClassPathClassLoader.hasPackage("java"),
		// is(true));
		// Assertions.//assertThat(javaClassPathClassLoader.hasPackage("javax"),
		// is(true));
		// Assertions.//assertThat(javaClassPathClassLoader.hasPackage("com.sun"),
		// is(true));
		// Assertions.//assertThat(javaClassPathClassLoader.hasPackage("com.openpojo"),
		// is(false));
	}

	@Test
	public void end2end_shouldLoadAllClassesInJavaLang() {
		List<PojoClass> types = PojoClassFactory.getPojoClassesRecursively("java.lang", null);
		checkListOfPojoClassesContains(types, java.lang.Class.class);
		checkListOfPojoClassesContains(types, java.lang.CharSequence.class);
		checkListOfPojoClassesContains(types, java.lang.Runnable.class);
		checkListOfPojoClassesContains(types, java.lang.Throwable.class);
		checkListOfPojoClassesContains(types, java.lang.Double.class);
		checkListOfPojoClassesContains(types, java.lang.Float.class);
		checkListOfPojoClassesContains(types, java.lang.Object.class);
		checkListOfPojoClassesContains(types, java.lang.Error.class);

		// Assertions.//assertThat(types.size(), greaterThan(minJavaLangClasses));
	}

	private void checkListOfPojoClassesContains(List<PojoClass> types, Class<?> expectedClass) {
		Assertions.assertTrue(types.contains(PojoClassFactory.getPojoClass(expectedClass)));
	}

	@Test
	public void end2endLoadAllClassesInTheVM() {
		List<PojoClass> types = PojoClassFactory.getPojoClassesRecursively("", null);
		Assertions.assertTrue(types.contains(PojoClassFactory.getPojoClass(this.getClass())));
		final String reason = "Loaded " + types.size() + " classes instead of expected " + minExpectedTotalClasses;
		// Assertions.//assertThat(reason, types.size(),
		// greaterThan(minExpectedTotalClasses));
		checkListOfPojoClassesContains(types, this.getClass());
	}

}