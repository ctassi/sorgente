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
 * 	File contentente la classe ServiceStatisticsAdmin
 * 
 *	@file		ServiceStatisticsAdmin.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */
package com.sequenziatore.server.service.admin;

import java.util.List;

import org.json.JSONObject;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceStatisticsAdmin offre il servizio di visualizzazione delle statistiche di un determinato admin.
 *
 *	@author 	DeSQ
 */
public class ServiceStatisticsAdmin implements IService {
	
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette la ricerca delle statistiche grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id dell'amministratore che fa la richiesta
	 * @return le statistiche
	 */
	
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		iDAOFactory = new DAOFactory();
		User user=(User)entity.get(0);
		List<Process> activeProcess;
		List<Process> allProcess;
		List<Integer> stepToVerify;
		List<Integer> allUser;
		
		try {
		    HibernateUtil.getSession().beginTransaction();
		    activeProcess = iDAOFactory.createDAOManagementProcessAdmin().findActiveProcess(user);
		    allProcess = iDAOFactory.createDAOManagementProcessAdmin().findAllProcesses(user);
		    stepToVerify = iDAOFactory.createDAOManagementProcessAdmin().findIdDataCollectionToVerify(user);
		    allUser = iDAOFactory.createDAOManagementProcessAdmin().findAllUser();
		    HibernateUtil.commitTransaction();
		} catch(Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
		objectJson.put("Confirmation", "receivedStatistics");
	    objectJson.put("ActiveProcess", activeProcess.size());
	    objectJson.put("AllProcess", allProcess.size());
	    objectJson.put("CloseProcess", allProcess.size()-activeProcess.size());
	    objectJson.put("ToVerify", stepToVerify.size());
	    objectJson.put("AllUser", allUser.size());
	    return objectJson;
	}

}
