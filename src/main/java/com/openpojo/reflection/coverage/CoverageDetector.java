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

package com.openpojo.reflection.coverage;

import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.adapt.PojoClassAdapter;

/**
 * Detects whether a coverage tool is present at runtime and supplies the filter and the adapter needed to ignore
 * whatever that tool injects into the bytecode.
 *
 * @author oshoukry
 */
public interface CoverageDetector {

	/**
	 * Human readable name of the tool.
	 *
	 * @return the name of the coverage tool.
	 */
	String getName();

	/**
	 * The class whose presence gives this coverage tool away.
	 *
	 * @return the fully qualified name of that class.
	 */
	String getCoverageClassName();

	/**
	 * Tells whether the tool is in use.
	 *
	 * @return {@code true} if the tool is present on the classpath.
	 */
	boolean isLoaded();

	/**
	 * The class filter belonging to this tool.
	 *
	 * @return the filter that discards the classes this tool generates.
	 */
	PojoClassFilter getPojoClassFilter();

	/**
	 * The class adapter belonging to this tool.
	 *
	 * @return the adapter that hides whatever this tool injects into classes.
	 */
	PojoClassAdapter getPojoClassAdapter();

}
