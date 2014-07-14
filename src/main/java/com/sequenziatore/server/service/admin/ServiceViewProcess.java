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
 * 	File contentente la classe ServiceViewProcess
 * 
 *	@file		ServiceViewProcess.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.admin;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceViewProcess offre il servizio di visualizzazione dei processi terminati e amministrati da uno specifico amministratore.
 *
 *	@author 	DeSQ
 */
public class ServiceViewProcess implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette la ricerca di uno specifico processo grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id dell'amministratore che fa la richiesta
	 * @return la lista dei processi terminati con tutte le informazioni necessarie
	 */
	
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=null;
		JSONArray array= new JSONArray();
		iDAOFactory = new DAOFactory();
		Process process=(Process)entity.get(0);
		Process processResult=null;
		
		try {
		    HibernateUtil.getSession().beginTransaction();
		    processResult = iDAOFactory.createDAOManagementProcessAdmin().findProcess(process);
		    HibernateUtil.commitTransaction();
		} catch (Exception e) {
			return new JSONObject().put("Confirmation", "connectionError");
		}
	    objectJson=processResult.toJSONObject();
	    for(int i=0;i<processResult.getSteps().size();i++){
	    	JSONObject objectJsonStep=processResult.getSteps().get(i).toJSONObject();
		    array.put(objectJsonStep);
		}
	    objectJson.put("Confirmation", "successGetProcess");
		objectJson.put("StepList", array);
	    return objectJson;
	}
	 
}
 
