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

package com.openpojo.reflection.coverage.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openpojo.reflection.filters.FilterCloverClasses;

/**
 * @author oshoukry
 */
public class Clover3Test {

	@Test
	public void shouldHaveNoAdapters() {
		Assertions.assertNull(Clover3.getInstance().getPojoClassAdapter());
	}

	@Test
	public void shouldHaveFilterCloverClasses() {
		Assertions.assertEquals(FilterCloverClasses.class, Clover3.getInstance().getPojoClassFilter().getClass());
	}

	@Test
	public void nameIsClover3() {
		Assertions.assertEquals("Clover 3", Clover3.getInstance().getName());
	}

	@Test
	public void coverageClassNameIs__com_cenqua_cloverTestNameSniffer() {
		Assertions.assertEquals("com_cenqua_clover.TestNameSniffer", Clover3.getInstance().getCoverageClassName());
	}
}
