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
 * 	File contentente la classe ServiceViewProcessNotActive
 * 
 *	@file		ServiceViewProcessNotActive.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */
package com.sequenziatore.server.service.user;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceViewProcessNotActive offre il servizio di visualizzazione dei processi a cui l'utente si è iscritto e che sono terminati o che sono stati completati con successo.
 *
 *	@author 	DeSQ
 */
public class ServiceViewProcessNotActive implements IService {
	
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette ad un utente di visualizzare i processi a cui si è iscritto e che sono terminati o che sono stati completati con successo grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id dell'utente che fa la richiesta
	 * @return i processi che sono terminati o che sono stati completati con successo
	 */
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		User user=(User)entity.get(0);
		JSONArray array= new JSONArray();
		iDAOFactory = new DAOFactory();
		List<Subscription> processFailure;
		List<Subscription> processSuccess;
		
		try {
		    HibernateUtil.getSession().beginTransaction();
		    processSuccess = iDAOFactory.createDAOManagementProcessUser().findSuccessSubscription(user);
		    processFailure = iDAOFactory.createDAOManagementProcessUser().findClosedSubscriptionFailure(user);
		    HibernateUtil.commitTransaction();
		} catch(Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
	    
	    if(processFailure.size() == 0 && processSuccess.size() == 0){
	    	objectJson.put("Confirmation", "notReceivedCloseProcess");
	    }else{
	    	for(int i=0;i<processSuccess.size();i++){
	    		JSONObject objectJsonProcess = processSuccess.get(i).getIdProcess().toJSONObject();
	    		objectJsonProcess.put("Complete", processSuccess.get(i).getComplete());
		    	array.put(objectJsonProcess);
		    }
	    	for(int i=0;i<processFailure.size();i++){
	    		JSONObject objectJsonProcess = processFailure.get(i).getIdProcess().toJSONObject();
	    		objectJsonProcess.put("Complete", processFailure.get(i).getComplete());
		    	array.put(objectJsonProcess);
	    	}
	    	objectJson.put("Confirmation", "receivedCloseProcess");
		    objectJson.put("ListProcess", array);
	    }
	    return objectJson;
	}
}
