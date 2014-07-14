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
 * 	File contentente la classe ServiceFacebookLogin
 * 
 *	@file		ServiceFacebookLogin.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */
package com.sequenziatore.server.service.shared;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceFacebookLogin offre il servizio di autenticazione o registrazione attraverso i dati di facebook.
 *
 *	@author 	DeSQ
 */
public class ServiceFacebookLogin implements IService {
	
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette l'autenticazione o la registrazione all'applicazione grazie alle informazioni acquisite dal front-end attraverso facebook.
	 * 
	 * @param entity contiene  l'email e i dati dell'utente
	 * @return l'esito dell'operazione di login con tutti i dati dell'utente
	 */
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		iDAOFactory = new DAOFactory();
		User user=(User)entity.get(0);
		User userSearchEmail=null;
		try {
	    	HibernateUtil.getSession().beginTransaction();
			userSearchEmail = iDAOFactory.createDAOUserData().findUserByEmail(user);
			HibernateUtil.commitTransaction();
			if(userSearchEmail == null){
				HibernateUtil.getSession().beginTransaction();
		    	iDAOFactory.createDAOUserData().insertUser(user);
		    	userSearchEmail = iDAOFactory.createDAOUserData().findUserByEmail(user);
		    	HibernateUtil.commitTransaction();
		    	objectJson=userSearchEmail.toJSONObject();
		    	List<IEntity> userRecovery=new ArrayList<IEntity>();
		    	userRecovery.add(userSearchEmail);
		    	userRecovery.add(userSearchEmail);
		    	new ServiceRecoveryPassword().serviceOperation(userRecovery);
		    	objectJson.put("Confirmation", "successAuthentication");
		    }else {
		    	objectJson=userSearchEmail.toJSONObject();
		    	objectJson.put("Confirmation", "successAuthentication");
		    }
		} catch (Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
		return objectJson;
	}
	
}
