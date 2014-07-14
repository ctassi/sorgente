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
 * 	File contentente la classe ProcessTest
 * 
 *	@file		ProcessTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe Process.
 *
 *	@author 	DeSQ
 */
public class ProcessTest {

	/**
	 * Test del costruttore Process(JSONObject).
	 */
	@Test
	public void testProcess(){
		JSONObject date = new JSONObject();
		date.put("Year", 2014);
		date.put("Month", 1);
		date.put("Day", 1);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("IdProcess", 1);		
		jsonObject.put("Name", "testName");
		jsonObject.put("Description", "testDescription");
		jsonObject.put("TotalLevel", 1);
		jsonObject.put("PublicationDate", date);
		jsonObject.put("ClosingDate", date);
		jsonObject.put("EndSubscriptionDate", date);
		jsonObject.put("Available", true);
		jsonObject.put("IdUser", 1);
		
		Process processObject = new Process(jsonObject);
		
		assertEquals(jsonObject.get("Name"), processObject.getName());
	}
	
	/**
	 *	Test del metodo testToJSONObject().
	 */
	@Test
	public void testToJSONObject(){
		GregorianCalendar date = new GregorianCalendar();
		
		Process processObject = new Process();
		processObject.setIdProcess(1);
		processObject.setName("test");
		processObject.setDescription("testDescription");
		processObject.setTotalLevel(2);
		processObject.setPublicationDate(date);
		processObject.setClosingDate(date);
		processObject.setEndSubscriptionDate(date);
		processObject.setAvailable(true);
		
		JSONObject jsonObject = processObject.toJSONObject();
		
		assertEquals(processObject.getDescription(), jsonObject.get("Description"));
	}
	
	/**
	 *	Test dei metodi getIdProcess() e setIdProcess().
	 */
	@Test
	public void testGetAndSetIdProcess(){
		int startValue = 10;
		Process processObject = new Process();
 		processObject.setIdProcess(startValue);
 		int endValue = processObject.getIdProcess();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getName() e setName().
	 */
	@Test
	public void testGetAndSetName(){
		String startValue = "test";
		Process processObject = new Process();
		processObject.setName(startValue);
 		String endValue = processObject.getName();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getDescription() e setDescription().
	 */
	@Test
	public void testGetAndSetDescription(){
		String startValue = "test";
		Process processObject = new Process();
		processObject.setDescription(startValue);
 		String endValue = processObject.getDescription();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getTotalLevel() e setTotalLevel().
	 */
	@Test
	public void testGetAndsetTotalLevel(){
		int startValue = 10;
		Process processObject = new Process();
		processObject.setTotalLevel(startValue);
 		int endValue = processObject.getTotalLevel();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getPublicationDate() e setPublicationDate().
	 */
	@Test
	public void testGetAndSetPublicationDate(){
		Calendar startValue = new GregorianCalendar();
		Process processObject = new Process();
		processObject.setPublicationDate(startValue);
 		Calendar endValue = processObject.getPublicationDate();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getClosingDate() e setClosingDate().
	 */
	@Test
	public void testGetAndSetClosingDate(){
		Calendar startValue = new GregorianCalendar();
		Process processObject = new Process();
		processObject.setClosingDate(startValue);
 		Calendar endValue = processObject.getClosingDate();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getAvailable() e setAvailable().
	 */
	@Test
	public void testGetAndSetAvailable(){
		boolean startValue = false;
		Process processObject = new Process();
		processObject.setAvailable(startValue);
 		boolean endValue = processObject.getAvailable();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getEndSubscriptionDate() e setEndSubscriptionDate().
	 */
	@Test
	public void testGetAndSetEndSubscriptionDate(){
		Calendar startValue = new GregorianCalendar();
		Process processObject = new Process();
		processObject.setEndSubscriptionDate(startValue);
 		Calendar endValue = processObject.getEndSubscriptionDate();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getAdmin() e setAdmin().
	 */
	@Test
	public void testGetAndSetAdmin(){
		User startValue = new User();
		Process processObject = new Process();
		processObject.setAdmin(startValue);
 		User endValue = processObject.getAdmin();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getSteps() e setSteps().
	 */
	@Test
	public void testGetAndSetSteps(){
		List<Step> startValue = new ArrayList<Step>();
		Process processObject = new Process();
		processObject.setSteps(startValue);
 		List<Step> endValue = processObject.getSteps();
 		assertEquals(startValue, endValue);
 	}

}
