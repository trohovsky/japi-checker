/*
 * Copyright 2011 William Bernardet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.japi.checker.tests;

public class PublicScopeFieldTestCases {
	public static String testPublicChangeFromStatic;
	protected static String testProtectedChangeFromStatic;
	private static String testPrivateChangeFromStatic;
	// Final
	public final String publicFinalToNoFinal = "";
	protected final String protectedFinalToNoFinal = "";
	private final String privateFinalToNoFinal = "";
	// change of type
	public String testChangeOfTypePublic;
	// Change of scope
	public String testChangeOfScopeFromPublicToProtected;
	public String testChangeOfScopeFromPublicToPrivate;
	// Static
	public String testPublicChangeToStatic;
	public String publicNotfinalToFinal = "";
	// transient
	public transient String publicTransientToNoTransient = "";
	public String publicNotTransientToTransient = "";
	protected String testChangeOfTypeProtected;
	protected String testChangeOfScopeFromProtectedToPublic;
	protected String testChangeOfScopeFromProtectedToPrivate;
	protected String testProtectedChangeToStatic;
	protected String protectedNotfinalToFinal = "";
	protected transient String protectedTransientToNoTransient = "";
	protected String protectedNotTransientToTransient = "";
	private String testChangeOfTypePrivate;
	private String testChangeOfScopeFromPrivateToProtected;
	private String testChangeOfScopeFromPrivateToPublic;
	private String testPrivateChangeToStatic;
	private String privateNotfinalToFinal = "";
	private transient String privateTransientToNoTransient = "";
	private String privateNotTransientToTransient = "";

}
