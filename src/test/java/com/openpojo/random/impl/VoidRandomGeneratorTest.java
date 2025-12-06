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

package com.openpojo.random.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openpojo.random.RandomGenerator;

public class VoidRandomGeneratorTest {
	RandomGenerator voidRandomGenerator;
	Class<?> voidClass = void.class;
	private static final int EXPECTED_COUNT = 1;

	@BeforeEach
	public void setUp() throws Exception {
		voidRandomGenerator = VoidRandomGenerator.getInstance();
	}

	@Test
	public void testGetInstance() {
		Assertions.assertNotNull(voidRandomGenerator);
		Assertions.assertTrue(voidRandomGenerator instanceof VoidRandomGenerator);
	}

	@Test
	public void testDoGenerate() {
		Assertions.assertNull(voidRandomGenerator.doGenerate(voidClass));
	}

	@Test
	public void testGetTypes() {
		CommonCode.testGetType(voidRandomGenerator, voidClass, EXPECTED_COUNT);

	}

}
