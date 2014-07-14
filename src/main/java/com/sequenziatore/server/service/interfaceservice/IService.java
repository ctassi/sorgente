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
 * 	File contentente l'interfaccia IService
 * 
 *	@file		IService.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.interfaceservice;

import java.util.List;

import org.json.JSONObject;

import com.sequenziatore.server.entity.IEntity;

/**
 *	L'interfaccia IService viene utilizzata dalle classi CtrlAdmin, CtrlUser e CtrlShared per interagire con le varie service.
 *
 *	@author 	DeSQ
 */
public interface IService {
 
	/**
	 * Permette diverse modalità di interazione con il database a seconda del tipo di service utilizzata.
	 * 
	 * @param entity contiene i dati su cui effettuare la ricerca nel database
	 * @return il risultato della ricerca
	 */
	public JSONObject serviceOperation(List<IEntity> entity);
}
 
