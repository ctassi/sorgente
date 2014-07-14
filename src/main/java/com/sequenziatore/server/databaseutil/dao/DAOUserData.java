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
 * 	File contentente la classe DAOUserData
 * 
 *	@file		DAOUserData.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.dao;

import org.hibernate.Query;

import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.User;

/**
 *	Classe DAOUserData viene utilizzata per gestire le operazioni sul database, sia in scrittura che in lettura.
 *
 *	@author 	DeSQ
 */
public class DAOUserData implements IDAOUserData {
 
	/**
	 * Premette di controllare le informazioni di login di un utente.
	 * 
	 * @param user contiene l'username e la password dell'utente
	 * @return tutte le informazioni dell'utente cercato, null altrimenti
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public User findUser(User user) throws Exception {
		User userLogin = null;
		Query query = HibernateUtil.getSession().createQuery("from User where Password = :password  and (Username = :username or Email= :username)");
		query.setParameter("username", user.getUsername());
		query.setParameter("password", user.getPassword());
		userLogin = (User) query.uniqueResult();
		return userLogin;
	}
	
	/**
	 * Permette di cercare un utente partendo dal suo id.
	 * 
	 * @param userFind contiene l'id dell'utente da cercare
	 * @return l'utente cercato se è stato trovato, null altrimenti
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public User findUserById(User userFind) throws Exception {
		User user = null;
		Query query = HibernateUtil.getSession().createQuery("from User where idUser = :idUser");
		query.setParameter("idUser", userFind.getIdUser());
		user = (User) query.uniqueResult();
		return user;
	}
	
	/**
	 * Permette la registrazione di un nuovo utente.
	 * 
	 * @param userRegistration contiene i dati dell'utente da registrare
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public void insertUser(User userRegistration) throws Exception {
		HibernateUtil.getSession().saveOrUpdate(userRegistration);
	}
	
	/**
	 * Permette di cercare un utente partendo dal suo username.
	 * 
	 * @param userRegistration contiene l'username dell'utente da cercare
	 * @return l'utente cercato se è stato trovato, null altrimenti
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public User findUserByUsername(User userRegistration) throws Exception {
		User userSearch = null;
		Query query = HibernateUtil.getSession().createQuery("from User where  Username = :username");
		query.setParameter("username", userRegistration.getUsername());
		userSearch = (User) query.uniqueResult();
		return userSearch;
	}
	
	/**
	 * Permette di cercare un utente partendo dalla sua email.
	 * 
	 * @param user contiene dalla email dell'utente da cercare
	 * @return l'utente cercato se è stato trovato, null altrimenti
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public User findUserByEmail(User user) throws Exception {
		User userSearch = null;
		Query query = HibernateUtil.getSession().createQuery("from User where Email= :email");
		query.setParameter("email", user.getEmail());
		userSearch = (User) query.uniqueResult();
		return userSearch;
	}
	 
}
 
