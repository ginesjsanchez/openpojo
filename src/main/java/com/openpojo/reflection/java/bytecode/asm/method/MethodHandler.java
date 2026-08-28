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

package com.openpojo.reflection.java.bytecode.asm.method;

import org.objectweb.asm.MethodVisitor;

/**
 * Generates the body of an abstract method in the subclass created with ASM.
 *
 * @author oshoukry
 */
public interface MethodHandler {

	/**
	 * Writes the method body into the subclass being generated.
	 *
	 * @param methodVisitor
	 *     The ASM visitor to emit the code on.
	 * @param abstractClassName
	 *     Internal name of the abstract class being extended.
	 * @param generatedClassName
	 *     Internal name of the subclass being generated.
	 * @param access
	 *     The method modifiers, as ASM encodes them.
	 * @param name
	 *     The method name.
	 * @param desc
	 *     The descriptor of the method signature.
	 * @param signature
	 *     The generic signature of the method, or {@code null} if it has none.
	 * @param exceptions
	 *     The declared exceptions, or {@code null} if there are none.
	 */
	void generateMethod(MethodVisitor methodVisitor,
			String abstractClassName,
			String generatedClassName,
			int access,
			String name,
			String desc,
			String signature,
			String[] exceptions);
}
