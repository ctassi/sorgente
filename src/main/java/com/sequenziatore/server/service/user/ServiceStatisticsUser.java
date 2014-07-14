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
 * 	File contentente la classe ServiceStatisticsUser
 * 
 *	@file		ServiceStatisticsUser.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */
package com.sequenziatore.server.service.user;

import java.util.List;

import org.json.JSONObject;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceStatisticsUser offre il servizio di visualizzazione delle statistiche di un determinato utente.
 *
 *	@author 	DeSQ
 */
public class ServiceStatisticsUser implements IService {
	
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette la ricerca delle statistiche grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id utente che fa la richiesta
	 * @return le statistiche
	 */
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		iDAOFactory = new DAOFactory();
		User user=(User)entity.get(0);
		List<Process> availableProcess;
		List<Subscription> activeProcess;
		List<Subscription> closeSuccessSubscription;
		List<Subscription> closeAllSubscription;
		try {
		    HibernateUtil.getSession().beginTransaction();
		    availableProcess = iDAOFactory.createDAOManagementProcessUser().findAvailableProcess(user);
		    activeProcess = iDAOFactory.createDAOManagementProcessUser().findActiveProcess(user);
		    closeSuccessSubscription = iDAOFactory.createDAOManagementProcessUser().findSuccessSubscription(user);
		    closeAllSubscription = iDAOFactory.createDAOManagementProcessUser().findClosedSubscriptionFailure(user);
		    HibernateUtil.commitTransaction();
		} catch(Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
		objectJson.put("Confirmation", "receivedStatistics");
	    objectJson.put("AvailableProcess", availableProcess.size());
	    objectJson.put("ActiveProcess", activeProcess.size());
	    objectJson.put("CloseSuccess", closeSuccessSubscription.size());
	    objectJson.put("CloseAll", closeAllSubscription.size()+closeSuccessSubscription.size());
	    objectJson.put("Close", closeAllSubscription.size());
	    return objectJson;
	}
}
