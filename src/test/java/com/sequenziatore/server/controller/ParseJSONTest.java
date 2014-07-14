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
 * 	File contentente la classe ParseJSONTest
 * 
 *	@file		ParseJSONTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.junit.Test;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe ParseJSON.
 *
 *	@author 	DeSQ
 */
public class ParseJSONTest {

	/**
	 *	Test del metodo parse() che estrae informazioni da una richiesta HTTP e ritorna un JSONObject.
	 */
	@Test
	public void testParse(){
		{
	        HttpServletRequest request = mock(HttpServletRequest.class);       
	        IParser iParserObject = new ParseJSON();
	        
	        String stringObject = "{key: 'value'}";
	    	StringReader stringReaderObject=new StringReader(stringObject);
	    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);

	        try {
				when(request.getReader()).thenReturn(bufferReaderObject);
			} catch (IOException e2) {
				e2.printStackTrace();
			}

	        JSONObject jsonObject = iParserObject.parse(request);
	        assertNotNull(jsonObject);
	        assertEquals(1, jsonObject.length());
	        assertEquals("value",jsonObject.get("key"));
	    }
 	}

}
