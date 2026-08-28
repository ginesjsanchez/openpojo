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

/**
 * Thrown when an operation needing ASM is requested and ASM is not on the classpath.
 *
 * @author oshoukry
 */
public class ASMNotLoadedException extends RuntimeException {
	private static final long serialVersionUID = -3171903904220994482L;

	private ASMNotLoadedException() {
		this("ASM v5.0+ library required, please see http://asm.ow2.org/");
	}

	private ASMNotLoadedException(String message) {
		super(message);
	}

	/**
	 * Returns the single instance of this class; it is the one that gets registered and the one reused on every
	 * request.
	 *
	 * @return the shared instance.
	 */
	public static ASMNotLoadedException getInstance() {
		return new ASMNotLoadedException();
	}

	/**
	 * Creates the exception with a message of its own instead of the default one.
	 *
	 * @param message
	 *     The detail message to include.
	 * @return the resulting exception.
	 */
	public static ASMNotLoadedException getInstance(String message) {
		return new ASMNotLoadedException(message);
	}
}
