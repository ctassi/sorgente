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
 * 	File contentente la classe ServiceViewAccount
 * 
 *	@file		ServiceViewAccount.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.shared;

import java.util.List;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.User;

import org.json.JSONObject;

import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceViewAccount offre il servizio di visualizzazione dei dati dell'account.
 *
 *	@author 	DeSQ
 */
public class ServiceViewAccount implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette ad un utente di visualizzare i dati del proprio account grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id dell'utente che fa la richiesta
	 * @return i dati dell'account dell'utente
	 */
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		User user = null;
		User userFind=(User)entity.get(0);
		JSONObject objectJson=new JSONObject();
		iDAOFactory = new DAOFactory();
		try {
			HibernateUtil.getSession().beginTransaction();
			user = iDAOFactory.createDAOUserData().findUserById(userFind);
			HibernateUtil.commitTransaction();	    
		} catch (Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
		if(user != null){
			objectJson=user.toJSONObject();
			objectJson.put("Confirmation", "receivedInfo");
		}else{
			objectJson.put("Confirmation", "notReceivedInfo");
		}
		return objectJson;
	}
	 
}
 
