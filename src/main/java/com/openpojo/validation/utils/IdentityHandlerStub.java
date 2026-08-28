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

package com.openpojo.validation.utils;

import java.util.ArrayList;
import java.util.List;

import com.openpojo.business.identity.IdentityHandler;

/**
 * Stub {@code IdentityHandler} for the business identity tests, recording the calls it receives.
 *
 * @author oshoukry
 */
public class IdentityHandlerStub implements IdentityHandler {
	private Boolean areEqualReturn;
	private Integer hashCodeReturn;

	private String toStringReturn;

	private Object instance1;

	private Object instance2;
	private List<Object> instances = new ArrayList<>();
	/**
	 * Creates the stub and registers it for the given instances.
	 *
	 * @param instances
	 *     The instances whose identity this stub should handle.
	 */
	public IdentityHandlerStub(Object... instances) {
		if (instances != null)
			for (Object instance : instances)
				if (instance != null)
					this.instances.add(instance);
	}

	public boolean handlerFor(final Object object) {
		for (Object instance : instances)
			if (object == instance)
				return true;
		return false;
	}

	public void validate(final Object object) {
	}

	/**
	 * Sets what {@code areEqual} will return.
	 *
	 * @param areEqualReturn
	 *     The value to return.
	 */
	public void setAreEqualReturn(final Boolean areEqualReturn) {
		this.areEqualReturn = areEqualReturn;
	}

	/**
	 * The value configured for the comparison.
	 *
	 * @return what {@code areEqual} will return.
	 */
	public Boolean getAreEqualReturn() {
		return areEqualReturn;
	}

	public boolean areEqual(final Object first, final Object second) {
		return areEqualReturn;
	}

	/**
	 * Sets what {@code generateHashCode} will return.
	 *
	 * @param hashCodeReturn
	 *     The value to return.
	 */
	public void setHashCodeReturn(final Integer hashCodeReturn) {
		this.hashCodeReturn = hashCodeReturn;
	}

	/**
	 * The value configured for the hash code.
	 *
	 * @return what {@code generateHashCode} will return.
	 */
	public Integer getHashCodeReturn() {
		return hashCodeReturn;
	}

	public int generateHashCode(final Object object) {
		return hashCodeReturn;
	}

	/**
	 * Sets what {@code toString} will return.
	 *
	 * @param toStringReturn
	 *     The value to return.
	 */
	public void setToStringReturn(String toStringReturn) {
		this.toStringReturn = toStringReturn;
	}

	/**
	 * The value configured for the text representation.
	 *
	 * @return what {@code toString} will return.
	 */
	public String getToStringReturn() {
		return toStringReturn;
	}

	public String toString(final Object object) {
		return toStringReturn;
	}
}
