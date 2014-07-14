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
 * 	File contentente la classe StepTest
 * 
 *	@file		StepTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.entity;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import com.sequenziatore.server.entity.Step.EnumParallelism;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe Step.
 *
 *	@author 	DeSQ
 */
public class StepTest {

	/**
	 * Test del costruttore Step(JSONObject).
	 */
	@Test
	public void testStep(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("IdStep", 1);
		jsonObject.put("Level", 1);
		jsonObject.put("Description", "testDescription");
		jsonObject.put("IsPhoto", true);
		jsonObject.put("IsText", true);
		jsonObject.put("IsGeolocation", true);
		jsonObject.put("CheckText", "testText");
		jsonObject.put("CheckLatitude", "testLatitude");
		jsonObject.put("CheckLongitude", "testLongitude");
		jsonObject.put("AdminVerify", false);
		jsonObject.put("Parallelism", "OR");
		jsonObject.put("IdProcess", 1);
		
		Step stepObject = new Step(jsonObject);
		assertEquals(jsonObject.get("Parallelism"), stepObject.getParallelism().toString());
		
		jsonObject.remove("Parallelism");
		jsonObject.put("Parallelism", "AND");
		
		stepObject = new Step(jsonObject);
		assertEquals(jsonObject.get("Parallelism"), stepObject.getParallelism().toString());
		
		jsonObject.remove("Parallelism");
		jsonObject.put("Parallelism", "NOT");
		
		stepObject = new Step(jsonObject);
		assertEquals(jsonObject.get("Parallelism"), stepObject.getParallelism().toString());
	}
	
	/**
	 * Test del metodo toJSONObject().
	 */
	@Test
	public void testToJSONObject(){
		Process processObject = new Process();
		processObject.setIdProcess(1);
		
		Step stepObject = new Step();
		stepObject.setIdStep(1);
		stepObject.setLevel(1);
		stepObject.setDescription("testDescription");
		stepObject.setIsPhoto(true);
		stepObject.setIsText(true);
		stepObject.setIsGeolocation(true);
		stepObject.setCheckText("testText");
		stepObject.setCheckLatitude("testLatitude");
		stepObject.setCheckLongitude("testLongitude");
		stepObject.setAdminVerify(false);
		stepObject.setParallelism(EnumParallelism.OR);
		stepObject.setIdProcess(processObject);
		
		JSONObject jsonObject = stepObject.toJSONObject();
		
		assertEquals(stepObject.getDescription(), jsonObject.get("Description"));
	}
	
	/**
	 *	Test dei metodi getIdStep() e setIdStep().
	 */
	@Test
	public void testGetAndSetIdStep(){
		int startValue = 10;
		Step stepObject = new Step();
 		stepObject.setIdStep(startValue);
 		int endValue = stepObject.getIdStep();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getLevel() e setLevel().
	 */
	@Test
	public void testGetAndSetLevel(){
		int startValue = 10;
		Step stepObject = new Step();
 		stepObject.setLevel(startValue);
 		int endValue = stepObject.getLevel();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getDescription() e setDescription().
	 */
	@Test
	public void testGetAndSetDescription(){
		String startValue = "test";
		Step stepObject = new Step();
 		stepObject.setDescription(startValue);
 		String endValue = stepObject.getDescription();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getIsPhoto() e setIsPhoto().
	 */
	@Test
	public void testGetAndSetPhoto(){
		boolean startValue = false;
		Step stepObject = new Step();
 		stepObject.setIsPhoto(startValue);
 		boolean endValue = stepObject.getIsPhoto();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getIsText() e setIsText().
	 */
	@Test
	public void testGetAndSetText(){
		boolean startValue = false;
		Step stepObject = new Step();
 		stepObject.setIsText(startValue);
 		boolean endValue = stepObject.getIsText();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getIsGeolocation() e setIsGeolocation().
	 */
	@Test
	public void testGetAndSetGeolocation(){
		boolean startValue = false;
		Step stepObject = new Step();
 		stepObject.setIsGeolocation(startValue);
 		boolean endValue = stepObject.getIsGeolocation();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getCheckText() e setCheckText().
	 */
	@Test
	public void testGetAndSetCheckText(){
		String startValue = "test";
		Step stepObject = new Step();
 		stepObject.setCheckText(startValue);
 		String endValue = stepObject.getCheckText();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getCheckLatitude() e setCheckLatitude().
	 */
	@Test
	public void testGetAndSetCheckLatitude(){
		String startValue = "test";
		Step stepObject = new Step();
 		stepObject.setCheckLatitude(startValue);
 		String endValue = stepObject.getCheckLatitude();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getCheckLongitude() e setCheckLongitude().
	 */
	@Test
	public void testGetAndSetCheckLongitude(){
		String startValue = "test";
		Step stepObject = new Step();
 		stepObject.setCheckLongitude(startValue);
 		String endValue = stepObject.getCheckLongitude();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getAdminVerify() e setAdminVerify().
	 */
	@Test
	public void testGetAndSetAdminVerify(){
		boolean startValue = false;
		Step stepObject = new Step();
 		stepObject.setAdminVerify(startValue);
 		boolean endValue = stepObject.getAdminVerify();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getParallelism() e setParallelism().
	 */
	@Test
	public void testGetAndSetParallelism(){
		EnumParallelism startValue = EnumParallelism.AND;
		Step stepObject = new Step();
 		stepObject.setParallelism(startValue);
 		EnumParallelism endValue = stepObject.getParallelism();
 		assertEquals(startValue, endValue);
 		
 		startValue = EnumParallelism.NOT;
 		stepObject.setParallelism(startValue);
 		endValue = stepObject.getParallelism();
 		assertEquals(startValue, endValue);
 		
 		startValue = EnumParallelism.OR;
 		stepObject.setParallelism(startValue);
 		endValue = stepObject.getParallelism();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getIdProcess() e setIdProcess().
	 */
	@Test
	public void testGetAndSetIdProcess(){
		Process startValue = new Process();
		Step stepObject = new Step();
 		stepObject.setIdProcess(startValue);
 		Process endValue = stepObject.getIdProcess();
 		assertEquals(startValue, endValue);
 	}
	
}
