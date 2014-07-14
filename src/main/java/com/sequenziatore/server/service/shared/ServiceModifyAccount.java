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
 * 	File contentente la classe ServiceModifyAccount
 * 
 *	@file		ServiceModifyAccount.java
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
 *	La classe ServiceModifyAccount offre il servizio di modifica dell'account di un utente.
 *
 *	@author 	DeSQ
 */
public class ServiceModifyAccount implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette la modifica dell'account di un utente grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene le informazioni che serviranno per aggiornare l'account
	 * @return l'esito dell'operazione di modifica
	 */
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		iDAOFactory = new DAOFactory();
		User user=(User)entity.get(0);
		User userSearchUsername = null;
		User userSearchEmail=null;
		User userPassword = null;
		User userPasswordControll = null;
	    
	    try {
	    	HibernateUtil.getSession().beginTransaction();
			userSearchUsername = iDAOFactory.createDAOUserData().findUserByUsername(user);
			userSearchEmail = iDAOFactory.createDAOUserData().findUserByEmail(user);
		    userPasswordControll = iDAOFactory.createDAOUserData().findUserById(user);
		    HibernateUtil.commitTransaction();
		} catch (Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
	    
	    // se userSearchUsername e userSearchEmail non sono null e non sono chi fa la richiesta
	    if((userSearchUsername != null && userSearchUsername.getIdUser().compareTo(user.getIdUser()) != 0) && (userSearchEmail != null && userSearchEmail.getIdUser().compareTo(user.getIdUser()) != 0))
	    	 return objectJson.put("Confirmation", "userAndMailNotAvailable");
	    else if(userSearchUsername != null && userSearchUsername.getIdUser().compareTo(user.getIdUser()) != 0)
	    	return objectJson.put("Confirmation", "usernameNotAvailable");
	    else if(userSearchEmail != null && userSearchEmail.getIdUser().compareTo(user.getIdUser()) != 0)
	    	return objectJson.put("Confirmation", "emailNotAvailable");
	    if(user.getPassword() == null){
	    	user.setPassword(userPasswordControll.getPassword());
	    	try {
	    	HibernateUtil.getSession().beginTransaction();
		    iDAOFactory.createDAOUserData().insertUser(user);
		    HibernateUtil.commitTransaction();
		    } catch (Exception e) {
				return objectJson.put("Confirmation", "connectionError");
			}
		    objectJson.put("Confirmation", "registrationOK");
	    } else{
	    	userPassword=(User)entity.get(1);
	    	if(userPasswordControll.getPassword().equals(userPassword.getPassword())){
	    		try {
	    		HibernateUtil.getSession().beginTransaction();
		    	iDAOFactory.createDAOUserData().insertUser(user);
		    	HibernateUtil.commitTransaction();
	    		} catch (Exception e) {
	    			return objectJson.put("Confirmation", "connectionError");
	    		}
		    	objectJson.put("Confirmation", "registrationOK");
	    	} else{
	    		objectJson.put("Confirmation", "wrongPassword");
	    	}
	    }
	    return objectJson;
	}
	 
}
 
