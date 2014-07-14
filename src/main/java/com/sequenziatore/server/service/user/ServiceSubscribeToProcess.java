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
 * 	File contentente la classe ServiceSubscribeToProcess
 * 
 *	@file		ServiceSubscribeToProcess.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.user;

import java.util.List;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.DataCollection.EnumState;

import org.json.JSONObject;

import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceSubscribeToProcess offre il servizio di iscrizione ad un processo.
 *
 *	@author 	DeSQ
 */
public class ServiceSubscribeToProcess implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette ad un utente di iscriversi ad un processo grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id dell'utente e l'id del processo a cui si vuole iscrivere
	 * @return l'esito dell'operazione di iscrizione
	 */
	
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		iDAOFactory = new DAOFactory();
		User user=(User)entity.get(0);
		Process process=(Process)entity.get(1);
		JSONObject objectJson=new JSONObject();
		Subscription subscription=null;
		try {
			HibernateUtil.getSession().beginTransaction();
			subscription=iDAOFactory.createDAOManagementProcessUser().findSubscription(user,process);
			if(subscription == null){
				process=iDAOFactory.createDAOManagementProcessUser().findProcessById(process);
				subscription=new Subscription();
				subscription.setIdUser(user);
				subscription.setIdProcess(process);
				subscription.setComplete(false);
				iDAOFactory.createDAOManagementProcessUser().insertSubscription(subscription);
				for(int i=0;i<process.getSteps().size();i++) {
					DataCollection data=new DataCollection();
					data.setIdStep(process.getSteps().get(i));
					data.setIdUser(user);
					data.setState(EnumState.NOTCOLLECTED);
					iDAOFactory.createDAOManagementProcessUser().insertDataCollection(data);
				}
				objectJson.put("Confirmation", "successSubscription");
			} else objectJson.put("Confirmation", "wrongSubscription");
		    HibernateUtil.commitTransaction();
		} catch(Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
	    return objectJson;
	}
	 
}
 
