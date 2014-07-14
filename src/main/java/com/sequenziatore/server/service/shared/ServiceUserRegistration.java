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
 * 	File contentente la classe ServiceUserRegistration
 * 
 *	@file		ServiceUserRegistration.java
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
 *	La classe ServiceUserRegistration offre il servizio di registrazione di un nuovo utente.
 *
 *	@author 	DeSQ
 */
public class ServiceUserRegistration implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette ad un nuovo utente di effettuare la registrazione grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene i dati necessari per la registrazione dell'utente
	 * @return l'esito dell'operazione di registrazione
	 */
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		iDAOFactory = new DAOFactory();
		User user=(User)entity.get(0);
		User userSearchUsername = null;
		User userSearchEmail=null;
	    try {
	    	HibernateUtil.getSession().beginTransaction();
			userSearchUsername = iDAOFactory.createDAOUserData().findUserByUsername(user);
			userSearchEmail = iDAOFactory.createDAOUserData().findUserByEmail(user);
		    HibernateUtil.commitTransaction();
		} catch (Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
	    if(userSearchUsername != null && userSearchEmail != null)
	    	 return objectJson.put("Confirmation", "userAndMailNotAvailable");
	    else if(userSearchUsername != null)
	    	return objectJson.put("Confirmation", "usernameNotAvailable");
	    else if(userSearchEmail != null)
	    	return objectJson.put("Confirmation", "emailNotAvailable");
	    try {
	    	HibernateUtil.getSession().beginTransaction();
	    	iDAOFactory.createDAOUserData().insertUser(user);
	    	HibernateUtil.commitTransaction();
	    } catch (Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
	    objectJson.put("Confirmation", "registrationOK");
		return objectJson;
	}
	 
}
 
