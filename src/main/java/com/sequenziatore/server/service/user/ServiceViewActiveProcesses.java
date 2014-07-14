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
 * 	File contentente la classe ServiceViewActiveProcesses
 * 
 *	@file		ServiceViewActiveProcesses.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.user;

import java.util.Calendar;
import java.util.List;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceViewActiveProcesses offre il servizio di visualizzazione dei processi attivi a cui l'utente sta partecipando.
 *
 *	@author 	DeSQ
 */
public class ServiceViewActiveProcesses implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette ad un utente di visualizzare i processi attivi a cui sta partecipando grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id dell'utente che fa la richiesta
	 * @return tutte le informazioni dei processi attivi a cui l'utente sta partecipando
	 */
	
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		JSONArray array= new JSONArray();
		User user=(User)entity.get(0);
		iDAOFactory = new DAOFactory();
		List<Subscription> subscriptions;
		
		try {
		    HibernateUtil.getSession().beginTransaction();
		    subscriptions = iDAOFactory.createDAOManagementProcessUser().findActiveProcess(user);
		    HibernateUtil.commitTransaction();
		} catch(Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
		
	    if(subscriptions.size() == 0){
	    	objectJson.put("Confirmation", "noneSubscriptions");
	    }else{
	    	for(int i=0;i<subscriptions.size();i++){
	    		JSONObject objectJsonSubscription=subscriptions.get(i).toJSONObject();
	    		Calendar date;
	    		objectJsonSubscription.put("Name",subscriptions.get(i).getIdProcess().getName());
	    		objectJsonSubscription.put("Description",subscriptions.get(i).getIdProcess().getDescription());
	    		objectJsonSubscription.put("TotalSteps",subscriptions.get(i).getIdProcess().getTotalLevel());
	    		date=subscriptions.get(i).getIdProcess().getPublicationDate();
	    		int publicationDateM =  date.get(Calendar.MONTH)+1;
	    		objectJsonSubscription.put("PublicationDate",date.get(Calendar.DAY_OF_MONTH)+ "-" + publicationDateM + "-" + date.get(Calendar.YEAR));
	    		date=subscriptions.get(i).getIdProcess().getClosingDate();
	    		int closingDateM = date.get(Calendar.MONTH)+1;
	    		objectJsonSubscription.put("ClosingDate",date.get(Calendar.DAY_OF_MONTH)+ "-" + closingDateM + "-" + date.get(Calendar.YEAR));
	    		date=subscriptions.get(i).getIdProcess().getEndSubscriptionDate();
	    		int endSubscriptionDateM = date.get(Calendar.MONTH)+1;
	    		objectJsonSubscription.put("EndSubscriptionDate",date.get(Calendar.DAY_OF_MONTH)+ "-" + endSubscriptionDateM + "-" + date.get(Calendar.YEAR));
		    	array.put(i,objectJsonSubscription);
		    }
		    objectJson.put("ListSubscriptions", array);
		    objectJson.put("Confirmation", "subscriptions");
	    }
	    return objectJson;
	}
	 
}
 
