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

package com.openpojo.reflection.coverage.service;

import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.adapt.PojoClassAdapter;
import com.openpojo.reflection.coverage.CoverageDetector;

/**
 * Service gathering the filter and the adapter of every detected coverage tool, so validation never sees the
 * structures they inject.
 *
 * @author oshoukry
 */
public interface PojoCoverageFilterService extends PojoClassFilter, PojoClassAdapter {
	/**
	 * Adds the filter and the adapter of a coverage tool.
	 *
	 * @param coverageDetector
	 *     The detector to register.
	 */
	void registerCoverageDetector(CoverageDetector coverageDetector);

}
