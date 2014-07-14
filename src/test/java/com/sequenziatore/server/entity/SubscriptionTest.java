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
 * 	File contentente la classe SubscriptionTest
 * 
 *	@file		SubscriptionTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.entity;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe Subscription.
 *
 *	@author 	DeSQ
 */
public class SubscriptionTest {

	/**
	 * Test del costruttore Subscription(JSONObject).
	 */
	@Test
	public void testSubscription(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("IdSubscription", 1);
		jsonObject.put("Complete", false);
		jsonObject.put("IdUser", 1);
		jsonObject.put("IdProcess", 1);
		
		Subscription subscriptionObject = new Subscription(jsonObject);
		assertTrue(jsonObject.get("IdSubscription").equals(subscriptionObject.getIdSubscription()));
	}
	
	/**
	 * Test del metodo toJSONObject().
	 */
	@Test
	public void testToJSONObject(){
		User userObject = new User();
		userObject.setIdUser(1);
		
		Process processObject = new Process();
		processObject.setIdProcess(1);
		
		Subscription subscriptionObject = new Subscription();
		subscriptionObject.setIdSubscription(1);
		subscriptionObject.setComplete(false);
		subscriptionObject.setIdUser(userObject);
		subscriptionObject.setIdProcess(processObject);
		
		JSONObject jsonObject = subscriptionObject.toJSONObject();
		
		assertEquals(jsonObject.get("Complete"), subscriptionObject.getComplete());
	}
	
	/**
	 *	Test dei metodi getIdSubscription() e setIdSubscription().
	 */
	@Test
	public void testGetAndSetIdSubscription(){
		int startValue = 10;
		Subscription subscriptionObject = new Subscription();
		subscriptionObject.setIdSubscription(startValue);
 		int endValue = subscriptionObject.getIdSubscription();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getIdUser() e setIdUser().
	 */
	@Test
	public void testGetAndSetIdUser(){
		User startValue = new User();
		Subscription subscriptionObject = new Subscription();
		subscriptionObject.setIdUser(startValue);
		User endValue = subscriptionObject.getIdUser();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getIdProcess() e setIdProcess().
	 */
	@Test
	public void testGetAndSetIdProcess(){
		Process startValue = new Process();
		Subscription subscriptionObject = new Subscription();
		subscriptionObject.setIdProcess(startValue);
 		Process endValue = subscriptionObject.getIdProcess();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getComplete() e setComplete().
	 */
	@Test
	public void testGetAndSetComplete(){
		boolean startValue = true;
		Subscription subscriptionObject = new Subscription();
		subscriptionObject.setComplete(startValue);
 		boolean endValue = subscriptionObject.getComplete();
 		assertEquals(startValue, endValue);
 	}

}
