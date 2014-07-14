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
 * 	File contentente la classe ParseJSON
 * 
 *	@file		ParseJSON.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 *	La classe ParseJSON viene utilizzata dai controller per ricavare un JSONObject da una richiesta http contenente solamente valori alfanumerici.
 *
 *	@author 	DeSQ
 */
public class ParseJSON implements IParser {
 
	/**
	 * Estrae le informazioni da una richiesta http contenente solo valori alfanumerici e restituisce un JSONObject che le contiene.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @return il JSONObject con le informazioni della richiesta http
	 */
	public JSONObject parse(HttpServletRequest request) {
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferReader = null;
		String line = null;
		JSONObject json = null;
	    try {
			bufferReader = request.getReader();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	    try {
			while ((line = bufferReader.readLine()) != null){
				stringBuffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 		
		json=new JSONObject(stringBuffer.toString());
		if(json.has("Photo")){
			MediaManager save=new MediaManager();
			String uploadPath = request.getServletContext().getRealPath("")+ File.separator + json.get("IdUser") + File.separator +json.get("IdProcess");
			json.put("Photo", save.uploadMediaBase64((String)json.get("Photo"),uploadPath));
		}
		return json;
	}
	 
}
 
