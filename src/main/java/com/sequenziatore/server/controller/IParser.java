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
 * 	File contentente l'interfaccia IParser
 * 
 *	@file		IParser.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 *	L'intefaccia IParser viene utilizzata dai controller per ricavare un JSONObject da una richiesta http.
 *
 *	@author 	DeSQ
 */
public interface IParser {
 
	/**
	 * Estrae le informazioni da una richiesta http e restituisce un JSONObject che le contiene.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @return il JSONObject con le informazioni della richiesta http
	 */
	public JSONObject parse(HttpServletRequest request);
}
 
