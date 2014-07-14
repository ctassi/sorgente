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
 * 	File contentente la classe ServiceViewAdminProcessNotActive
 * 
 *	@file		ServiceViewAdminProcessNotActive.java
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
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceViewAdminProcessNotActive offre il servizio di visualizzazione di tutti i processi non attivi di un amministratore.
 *
 *	@author 	DeSQ
 */
public class ServiceViewAdminProcessNotActive implements IService {
	
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette la ricerca di tutti i processi non attivi di un amministratore grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id dell'amministratore che fa la richiesta
	 * @return la lista dei processi non attivi con tutte le informazioni necessarie
	 */
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		JSONArray array= new JSONArray();
		iDAOFactory = new DAOFactory();
		User user=(User)entity.get(0);
		List<Process> processes;
		try {
		    HibernateUtil.getSession().beginTransaction();
		    processes = iDAOFactory.createDAOManagementProcessAdmin().findClosedProcess(user);
		    HibernateUtil.commitTransaction();
		} catch (Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
	    if(processes.size() == 0){
	    	objectJson.put("Confirmation", "noneProcess");
	    }else{
	    	for(int i=0;i<processes.size();i++){
	    		JSONObject objectJsonProcess=processes.get(i).toJSONObject();
		    	array.put(objectJsonProcess);
		    }
		    objectJson.put("ListProcess", array);
		    objectJson.put("Confirmation", "process");
	    }
	    return objectJson;
	}

}
