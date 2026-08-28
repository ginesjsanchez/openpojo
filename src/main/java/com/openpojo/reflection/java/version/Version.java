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

package com.openpojo.reflection.java.version;

/**
 * A version broken down into major, minor and patch, comparable with another.
 *
 * @author oshoukry
 */
public interface Version extends Comparable<Version> {
	/**
	 * The version exactly as it was received.
	 *
	 * @return the original string.
	 */
	String getVersion();

	/**
	 * The version number at the given position: 0 major, 1 minor, 2 patch.
	 *
	 * @param idx
	 *     The position to read.
	 * @return that number, or {@code null} if there is none.
	 */
	Integer getPart(int idx);
}
