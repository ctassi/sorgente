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
 * 	File contentente la classe UserTest
 * 
 *	@file		UserTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.entity;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;


/**
 *	Classe contenente tutti i test di unità dei metodi della classe User.
 *
 *	@author 	DeSQ
 */
public class UserTest {

	/**
	 * Test del costruttore User(JSONObject).
	 */
	@Test
	public void testUser(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("IdUser", 1);
		jsonObject.put("Username", "testUsername");
		jsonObject.put("Password", "testPassword");
		jsonObject.put("Name", "testName");
		jsonObject.put("Surname", "testSurname");
		jsonObject.put("Email", "testEmail");
		jsonObject.put("City", "testCity");
		jsonObject.put("District", "TD");
		jsonObject.put("IsAdmin", true);
		
		User userObject = new User(jsonObject);
		assertEquals(jsonObject.get("Name"), userObject.getName());
	}
	
	/**
	 * Test del metodo toJSONObject().
	 */
	@Test
	public void testToJSONObject(){
		User userObject = new User();
		userObject.setIdUser(1);
		userObject.setUsername("testUsername");
		userObject.setName("testName");
		userObject.setSurname("testSurname");
		userObject.setEmail("testEmail");
		userObject.setCity("testCity");
		userObject.setDistrict("TD");
		userObject.setIsAdmin(true);
		
		JSONObject jsonObject = userObject.toJSONObject();
		
		assertEquals(userObject.getUsername(), jsonObject.get("Username"));
	}
	
	/**
	 * Test del metodo hashAndSetPassword().
	 */
	@Test
	public void testHashAndSetPassword(){
		String startValue = "test";
		User userObject = new User();
 		userObject.hashAndSetPassword(startValue);
 		String endValue = userObject.getPassword();
 		assertNotSame(startValue, endValue);
	}
	
	/**
	 *	Test dei metodi getIdUser() e setIdUser().
	 */
	@Test
	public void testGetAndSetIdUser(){
		int startValue = 10;
		User userObject = new User();
 		userObject.setIdUser(startValue);
 		int endValue = userObject.getIdUser();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getUsername() e setUsername().
	 */
	@Test
	public void testGetAndSetUsername(){
		String startValue = "test";
		User userObject = new User();
 		userObject.setUsername(startValue);
 		String endValue = userObject.getUsername();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getPassword() e setPassword().
	 */
	@Test
	public void testGetAndSetPassword(){
		String startValue = "test";
		User userObject = new User();
 		userObject.setPassword(startValue);
 		String endValue = userObject.getPassword();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getName() e setName().
	 */
	@Test
	public void testGetAndSetName(){
		String startValue = "test";
		User userObject = new User();
 		userObject.setName(startValue);
 		String endValue = userObject.getName();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getSurname() e setSurname().
	 */
	@Test
	public void testGetAndSetSurname(){
		String startValue = "test";
		User userObject = new User();
 		userObject.setSurname(startValue);
 		String endValue = userObject.getSurname();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getEmail() e setEmail().
	 */
	@Test
	public void testGetAndSetEmail(){
		String startValue = "test";
		User userObject = new User();
 		userObject.setEmail(startValue);
 		String endValue = userObject.getEmail();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getCity() e setCity().
	 */
	@Test
	public void testGetAndSetCity(){
		String startValue = "test";
		User userObject = new User();
 		userObject.setCity(startValue);
 		String endValue = userObject.getCity();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getDistrict() e setDistrict().
	 */
	@Test
	public void testGetAndSetDistrict(){
		String startValue = "test";
		User userObject = new User();
 		userObject.setDistrict(startValue);
 		String endValue = userObject.getDistrict();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getIsAdmin() e setIsAdmin().
	 */
	@Test
	public void testGetAndSetIsAdmin(){
		boolean startValue = true;
		User userObject = new User();
 		userObject.setIsAdmin(startValue);
 		boolean endValue = userObject.getIsAdmin();
 		assertEquals(startValue, endValue);
 	}
	
}
