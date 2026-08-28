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

package com.openpojo.random.util;

import java.util.Collections;
import java.util.List;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.relation.RoleStatus;
import javax.management.relation.RoleUnresolved;

import com.openpojo.random.RandomFactory;
import com.openpojo.random.exception.RandomGeneratorException;

/**
 * Filler instance of {@code javax.management.relation.RoleUnresolved} with a random name, value and status.
 *
 * @author oshoukry
 */
public class SomeRoleUnresolved extends RoleUnresolved {
	private static final long serialVersionUID = 6829782498683326442L;

	/**
	 * Creates an instance with a random name, value and status.
	 */
	public SomeRoleUnresolved() {
		super(anyString(), anyRoleValue(), anyProblemRoleStatus());
	}

	/**
	 * Generates random text for the role name.
	 *
	 * @return the generated text.
	 */
	protected static String anyString() {
		return RandomFactory.getRandomValue(String.class);
	}

	@SuppressWarnings("ConstantConditions")
	private static List<ObjectName> anyRoleValue() {
		try {
			return Collections.singletonList(new ObjectName("*:type=" + anyString() + ",name=" + anyString()));
		} catch (MalformedObjectNameException e) {
			throw RandomGeneratorException.getInstance("Failed to create Role", e);
		}
	}

	private static int anyProblemRoleStatus() {
		return RoleStatus.NO_ROLE_WITH_NAME;
	}
}
