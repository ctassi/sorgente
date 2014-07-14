/*
 * Copyright 2014 Dainese Matteo, De Nadai Andrea, Girotto Giacomo, Pavanello Mirko, Romagnosi Nicolò, Sartoretto Massimiliano, Visentin Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 	File contentente la classe NotificationTest
 * 
 *	@file		NotificationTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe Notification.
 *
 *	@author 	DeSQ
 */
public class NotificationTest {

	/**
	 *  Test dei metodi getUsername() e setUsername().
	 */
	@Test
	public void testGetAndSetUsername() {
		String startValue = "test";
		Notification notificationObject = new Notification();
		notificationObject.setUsername(startValue);
 		String endValue = notificationObject.getUsername();
 		assertEquals(startValue, endValue);
	}
	
	/**
	 *  Test dei metodi getMessage() e setMessage().
	 */
	@Test
	public void testGetAndSetMessage() {
		String startValue = "test";
		Notification notificationObject = new Notification();
		notificationObject.setMessage(startValue);
 		String endValue = notificationObject.getMessage();
 		assertEquals(startValue, endValue);
	}
	
	/**
	 *  Test dei metodi getActionType() e setActionType().
	 */
	@Test
	public void testGetAndSetActionType() {
		String startValue = "test";
		Notification notificationObject = new Notification();
		notificationObject.setActionType(startValue);
 		String endValue = notificationObject.getActionType();
 		assertEquals(startValue, endValue);
	}
	
	/**
	 *  Test dei metodi getSuccess() e setSuccess().
	 */
	@Test
	public void testGetAndSetSuccess() {
		String startValue = "test";
		Notification notificationObject = new Notification();
		notificationObject.setSuccess(startValue);
 		String endValue = notificationObject.getSuccess();
 		assertEquals(startValue, endValue);
	}

}
