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
 * 	File contentente la classe ServiceListUserProcess
 * 
 *	@file		ServiceListUserProcess.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.admin;

import java.util.List;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.entity.Process;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceListUserProcess offre il servizio di visualizzazione di tutti gli utenti che partecipano ad un processo.
 *
 *	@author 	DeSQ
 */
public class ServiceListUserProcess implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette la ricerca degli utenti che partecipano ad un processo a partire dalle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id del processo su cui effettuare la ricerca
	 * @return la lista dei partecipanti al processo
	 */
	
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		Process process=(Process)entity.get(0);
		JSONArray array= new JSONArray();
		iDAOFactory = new DAOFactory();
		List<User> users;
		try {
		    HibernateUtil.getSession().beginTransaction();
		    users = iDAOFactory.createDAOManagementProcessAdmin().findUserSubscription(process);
		    HibernateUtil.commitTransaction();
		} catch(Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
	    if(users.size() == 0){
	    	objectJson.put("Confirmation", "noneUser");
	    }else{
	    	for(int i=0;i<users.size();i++){
	    		JSONObject objectJsonUsers=users.get(i).toJSONObject();
		    	array.put(objectJsonUsers);
		    }
	    	objectJson.put("Confirmation", "users");
		    objectJson.put("ListUser", array);
	    }
	    return objectJson;
	}
	 
}
 
