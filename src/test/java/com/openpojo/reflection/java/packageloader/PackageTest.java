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

package com.openpojo.reflection.java.packageloader;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.openpojo.random.RandomFactory;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.BusinessKeyMustExistRule;
import com.openpojo.validation.test.impl.BusinessIdentityTester;

/**
 * @author oshoukry
 */
public class PackageTest {

	@Test
	public final void testIsValid() {
		Package javaPackage = new Package(RandomFactory.getRandomValue(String.class));
		Affirm.affirmFalse("Invalid package evaluated to as valid?!", javaPackage.isValid());

		javaPackage = new Package(this.getClass().getPackage().getName());
		Affirm.affirmTrue("Valid package evaluated to as invalid?!", javaPackage.isValid());
	}

	/**
	 * Coverage that used to live in JavaClassPathClassLoaderTest. Now that the manual
	 * java.class.path scan is gone, ClassLoader.getResources is the only discovery
	 * mechanism, so it is worth checking end to end that it finds classes and
	 * sub-packages inside a jar.
	 */
	@Test
	public final void shouldFindClassesInsideAJar() {
		List<PojoClass> types = PojoClassFactory.getPojoClassesRecursively("org.apache.commons.lang3", null);

		Affirm.affirmTrue("No class found in a package that lives inside a jar", !types.isEmpty());
		Affirm.affirmTrue("org.apache.commons.lang3.StringUtils not found inside its jar",
				types.contains(PojoClassFactory.getPojoClass(org.apache.commons.lang3.StringUtils.class)));
	}

	@Test
	public final void shouldFindSubPackagesInsideAJar() {
		Package javaPackage = new Package("org.apache.commons.lang3");

		Affirm.affirmTrue("A package living inside a jar should be valid", javaPackage.isValid());
		Affirm.affirmTrue("No sub-package found inside a jar",
				!javaPackage.getSubPackages().isEmpty());
	}

	@Test
	public final void shouldFindClassesLoadedInTheVM() {
		List<PojoClass> types = PojoClassFactory.getPojoClassesRecursively("", null);

		Affirm.affirmTrue("Scanning the root package did not even find this very test class",
				types.contains(PojoClassFactory.getPojoClass(this.getClass())));
	}

	@Test
	public final void packageShouldDispatchEqualsAndHashCodeToBusinessIdentity() {
		final Validator pojoValidator = ValidatorBuilder.create()
				.with(new BusinessIdentityTester())
				.with(new BusinessKeyMustExistRule())
				.build();

		pojoValidator.validate(PojoClassFactory.getPojoClass(Package.class));
	}

}
