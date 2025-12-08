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

package com.openpojo.reflection.java.packageloader.reader;

import java.net.URI;
import java.net.URL;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoMethod;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.utils.samplejar.SampleJar;

/**
 * @author oshoukry
 */
public class JarFileReaderTest {

	@Test
	public void onlyPrivateConstructors() {
		PojoClass pojoClass = PojoClassFactory.getPojoClass(JarFileReader.class);
		for (PojoMethod method : pojoClass.getPojoConstructors()) {
			Assertions.assertTrue(method.isPrivate());
		}
	}

	@Test
	public void canCreate() {
		JarFileReader jarFileReader = JarFileReader.getInstance((String) null);
		Assertions.assertNotNull(jarFileReader);
	}

	@Test
	public void canCreateUsingInvalidJarFileUrl() {
		JarFileReader jarfileReader = JarFileReader.getInstance((URL) null);
		Assertions.assertNotNull(jarfileReader);
		Assertions.assertFalse(jarfileReader.isValid());
	}

	@Test
	public void canReadJarUsingURLOrFile() {
		JarFileReader urlJarFileReader = JarFileReader.getInstance(SampleJar.getJarURL());
		JarFileReader filepathJarFileReader = JarFileReader.getInstance(SampleJar.getJarFilePath());

		Assertions.assertTrue(urlJarFileReader.isValid());
		Assertions.assertTrue(filepathJarFileReader.isValid());

		Set<String> urlClassNames = urlJarFileReader.getClassNames();
		Set<String> filepathClassNames = filepathJarFileReader.getClassNames();
		Assertions.assertEquals(urlClassNames.size(), filepathClassNames.size());

		for (String urlEntry : urlClassNames) {
			Assertions.assertTrue(filepathClassNames.contains(urlEntry));
		}
	}

	@Test
	public void shouldReturnFalseWhenInvalidFile() {
		JarFileReader jarFileReader = JarFileReader.getInstance((String) null);
		Assertions.assertFalse(jarFileReader.isValid());
	}

	@Test
	public void shouldReturnFalseIfFileIsNotJarFile() throws Exception {
		PojoClass pojoClass = PojoClassFactory.getPojoClass(this.getClass());
		String sourcePath = (new URI(pojoClass.getSourcePath())).toURL().getPath();
		JarFileReader jarFileReader = JarFileReader.getInstance(sourcePath);
		Assertions.assertFalse(jarFileReader.isValid());
	}

	@Test
	public void isValidReturnsTrueWhenFileIsJar() throws Exception {
		JarFileReader jarFileReader = JarFileReader.getInstance(SampleJar.getJarFilePath());
		Assertions.assertTrue(jarFileReader.isValid());
	}

	@SuppressWarnings("unused")
	@Test
	public void canReadEntries() {
		String jarFile = System.getProperty("java.home") + "/lib/jrt-fs.jar";
		Assertions.assertNotNull(jarFile);
		JarFileReader jarFileReader = JarFileReader.getInstance(jarFile);
		Assertions.assertNotNull(jarFileReader);
		Assertions.assertTrue(jarFileReader.isValid());
		Set<String> classNames = jarFileReader.getClassNames();
		//// Assertions.//assertThat(classNames.size(), Matchers.greaterThan(10000)); //
		//// actual number is 20,651
	}

//	private String getJarFile(String jarFileName) {
//		logger.info("*** {}", System.getProperty("java.home"));
//		String classPath = System.getProperty("java.class.path");
//		classPath += Java.CLASSPATH_DELIMITER + System.getProperty("java.library.path");
//		classPath += Java.CLASSPATH_DELIMITER + System.getProperty("java.ext.dirs");
//		classPath += Java.CLASSPATH_DELIMITER + System.getProperty("sun.boot.class.path");
//		String[] classPathParts = classPath.split(Java.CLASSPATH_DELIMITER);
//		for (String entry : classPathParts) {
//			if (entry.endsWith(Java.PATH_DELIMITER + jarFileName))
//				return entry;
//		}
//		return null;
//	}
}