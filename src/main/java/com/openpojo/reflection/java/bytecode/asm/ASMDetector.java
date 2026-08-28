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

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.reflection.java.load.ClassUtil;
import com.openpojo.reflection.java.packageloader.reader.JarFileReader;
import com.openpojo.reflection.java.version.Version;
import com.openpojo.reflection.java.version.VersionFactory;

import static com.openpojo.reflection.java.version.VersionFactory.getImplementationVersion;

/**
 * Works out whether ASM is on the classpath and at which version, by reading the manifest of its jar.
 *
 * @author oshoukry
 */
public class ASMDetector {
	/**
	 * The class whose presence gives away that ASM is on the classpath.
	 */
	public static final String ASM_CLASS_NAME = "org.objectweb.asm.ClassWriter";
	private static final String VERSION_MANIFEST_KEY_FALLBACK = "Bundle-Version";

	private ASMDetector() {
	}

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared instance.
	 */
	public static ASMDetector getInstance() {
		return Instance.INSTANCE;
	}

	/**
	 * Tells whether ASM is available.
	 *
	 * @return {@code true} if its telltale class is loaded.
	 */
	public boolean isASMLoaded() {
		return ClassUtil.isClassLoaded(ASM_CLASS_NAME);
	}

	/**
	 * The ASM version present on the classpath.
	 *
	 * @return the detected version.
	 */
	public Version getVersion() {
		Class<?> clazz = ClassUtil.loadClass(ASM_CLASS_NAME);
		Version implementationVersion = getImplementationVersion(clazz);
		if (implementationVersion.getVersion() == null) {
			implementationVersion = getBundleVersion(clazz);
		}
		return implementationVersion;
	}

	/**
	 * Reads the version declared in the manifest of the jar where the given class lives.
	 *
	 * @param clazz
	 *     Any class from the jar to inspect.
	 * @return the bundle version, or an empty version if it is not stated.
	 */
	public Version getBundleVersion(Class<?> clazz) {
		Version bundleVersion;

		try {
			PojoClass pojoClass = PojoClassFactory.getPojoClass(clazz);
			String sourcePath = pojoClass.getSourcePath();
			String jarFilePath = JarFileReader.getJarFileNameFromURLPath(sourcePath);
			String bundleVersionManifestEntry = JarFileReader.getInstance(jarFilePath)
					.getManifestEntry(VERSION_MANIFEST_KEY_FALLBACK);
			bundleVersion = VersionFactory.getVersion(bundleVersionManifestEntry);
		} catch (Exception ignored) {
			bundleVersion = VersionFactory.getVersion(null);
		}

		return bundleVersion;
	}

	private static class Instance {
		private static final ASMDetector INSTANCE = new ASMDetector();
	}
}
