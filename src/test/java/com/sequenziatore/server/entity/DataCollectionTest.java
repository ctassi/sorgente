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
 * 	File contentente la classe DataCollectionTest
 * 
 *	@file		DataCollectionTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.entity;

import com.sequenziatore.server.entity.DataCollection.EnumState;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe DataCollection.
 *
 *	@author 	DeSQ
 */
public class DataCollectionTest{
	
	/**
	 * Test del costruttore DataCollection(JSONObject).
	 */
	@Test
	public void testDataCollection(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("IdCollection", 1);
		jsonObject.put("Photo", "testPhoto");
		jsonObject.put("Text", "testText");
		jsonObject.put("Latitude", "testLatitude");
		jsonObject.put("Longitude", "testLongitude");
		jsonObject.put("State", "VERIFIED");
		jsonObject.put("WrongText", true);
		jsonObject.put("WrongPhoto", true);
		jsonObject.put("WrongGeolocation", true);
		jsonObject.put("IdUser", "1");
		jsonObject.put("IdStep", "1");
		
		DataCollection dataCollectionObject = new DataCollection(jsonObject);
		assertEquals(jsonObject.get("State"), dataCollectionObject.getState().toString());
		
		jsonObject.remove("WrongText");
		jsonObject.put("WrongText", "true");
		jsonObject.remove("WrongPhoto");
		jsonObject.put("WrongPhoto", "true");
		jsonObject.remove("WrongGeolocation");
		jsonObject.put("WrongGeolocation", "true");
		jsonObject.remove("IdStep");
		jsonObject.put("IdStep", 1);
		jsonObject.remove("IdUser");
		jsonObject.put("IdUser", 1);
		jsonObject.remove("State");
		jsonObject.put("State", "TOVERIFY");
	
		dataCollectionObject = new DataCollection(jsonObject);
		assertEquals(jsonObject.get("State"), dataCollectionObject.getState().toString());
		
		jsonObject.remove("State");
		jsonObject.put("State", "NOTCOLLECTED");
	
		dataCollectionObject = new DataCollection(jsonObject);
		assertEquals(jsonObject.get("State"), dataCollectionObject.getState().toString());

		jsonObject.remove("State");
		jsonObject.put("State", "SKIPPED");
	
		dataCollectionObject = new DataCollection(jsonObject);
		assertEquals(jsonObject.get("State"), dataCollectionObject.getState().toString());
		
		jsonObject.remove("State");
		jsonObject.put("State", "FAILED");
	
		dataCollectionObject = new DataCollection(jsonObject);
		assertEquals(jsonObject.get("State"), dataCollectionObject.getState().toString());	
	}
	
	/**
	 * Test del metodo toJSONObject().
	 */
	@Test
	public void testToJSONObject(){
		User userObject = new User();
		userObject.setUsername("test");
		
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setIdCollection(1);
		dataCollectionObject.setPhoto("testPhoto");
		dataCollectionObject.setText("testText");
		dataCollectionObject.setLatitude("testLatitude");
		dataCollectionObject.setLongitude("testLongitude");
		dataCollectionObject.setState(EnumState.TOVERIFY);
		dataCollectionObject.setWrongText(true);
		dataCollectionObject.setWrongPhoto(true);
		dataCollectionObject.setWrongGeolocation(true);
		dataCollectionObject.setIdUser(userObject);
		
		JSONObject jsonObject = dataCollectionObject.toJSONObject();
		
		assertEquals(dataCollectionObject.getText(), jsonObject.get("Text"));
	}
	
	/**
	 *	Test dei metodi getIdCollection() e setIdCollection().
	 */
	@Test
	public void testGetAndSetIdCollection(){
		int startValue = 10;
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setIdCollection(startValue);
 		int endValue = dataCollectionObject.getIdCollection();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getPhoto() e setPhoto().
	 */
	@Test
	public void testGetAndSetPhoto(){
		String startValue = "test";
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setPhoto(startValue);
 		String endValue = dataCollectionObject.getPhoto();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getText() e setText().
	 */
	@Test
	public void testGetAndSetText(){
		String startValue = "test";
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setText(startValue);
 		String endValue = dataCollectionObject.getText();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getLatitude() e setLatitude().
	 */
	@Test
	public void testGetAndSetLatitude(){
		String startValue = "test";
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setLatitude(startValue);
 		String endValue = dataCollectionObject.getLatitude();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getLongitude() e setLongitude().
	 */
	@Test
	public void testGetAndSetLongitude(){
		String startValue = "test";
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setLongitude(startValue);
 		String endValue = dataCollectionObject.getLongitude();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getState() e setState().
	 */
	@Test
	public void testGetAndSetState(){
		EnumState startValue = EnumState.VERIFIED;
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setState(startValue);
 		EnumState endValue = dataCollectionObject.getState();
 		assertEquals(startValue, endValue);
 		
 		startValue = EnumState.TOVERIFY;
 		dataCollectionObject.setState(startValue);
 		endValue = dataCollectionObject.getState();
 		assertEquals(startValue, endValue);
 	
 		startValue = EnumState.NOTCOLLECTED;
 		dataCollectionObject.setState(startValue);
 		endValue = dataCollectionObject.getState();
 		assertEquals(startValue, endValue);
 		
 		startValue = EnumState.FAILED;
 		dataCollectionObject.setState(startValue);
 		endValue = dataCollectionObject.getState();
 		assertEquals(startValue, endValue);
 		
 		startValue = EnumState.SKIPPED;
 		dataCollectionObject.setState(startValue);
 		endValue = dataCollectionObject.getState();
 		assertEquals(startValue, endValue);
	}
	
	/**
	 *	Test dei metodi getIdUser() e setIdUser().
	 */
	@Test
	public void testGetAndSetIdUser(){
		User startValue = new User();
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setIdUser(startValue);
		User endValue = dataCollectionObject.getIdUser();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getIdStep() e setIdStep().
	 */
	@Test
	public void testGetAndSetIdStep(){
		Step startValue = new Step();
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setIdStep(startValue);
 		Step endValue = dataCollectionObject.getIdStep();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getWrongPhoto() e setWrongPhoto().
	 */
	@Test
	public void testGetAndSetWrongPhoto(){
		Boolean startValue = new Boolean(true);
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setWrongPhoto(startValue);
 		Boolean endValue = dataCollectionObject.getWrongPhoto();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getWrongText() e setWrongText().
	 */
	@Test
	public void testGetAndSetWrongText(){
		Boolean startValue = new Boolean(true);
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setWrongText(startValue);
 		Boolean endValue = dataCollectionObject.getWrongText();
 		assertEquals(startValue, endValue);
 	}
	
	/**
	 *	Test dei metodi getWrongGeolocation() e setWrongGeolocation().
	 */
	@Test
	public void testGetAndSetWrongGeolocation(){
		Boolean startValue = new Boolean(true);
		DataCollection dataCollectionObject = new DataCollection();
		dataCollectionObject.setWrongGeolocation(startValue);
 		Boolean endValue = dataCollectionObject.getWrongGeolocation();
 		assertEquals(startValue, endValue);
 	}
}
