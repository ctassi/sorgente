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
 * 	File contentente la classe HibernateUtil
 * 
 *	@file		HibernateUtil.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *	La classe HibernateUtil gestisce gli accessi alla SessionFactory di Hibernate ed è implementata tramite il design pattern Singleton. 
 *
 *	@author 	DeSQ
 */
public class HibernateUtil {
 
	/** Permette di effettuare operazioni di lettura e scrittura sul database. */
	private static SessionFactory SESSIONFACTORY;
	
	/** Operazioni preliminari per la creazione della sessione. */
	private HibernateUtil() {}
	
	/**
	 * Ritorna l'attributo SESSIONFACTORY.
	 * 
	 * @throws Exception segnala un problema di connessione al database
	 * @return l'attributo SESSIONFACTORY
	 */
	public static SessionFactory getSessionFactory() throws Exception {
		if(SESSIONFACTORY==null)
		{
			Configuration configuration = new Configuration();
			configuration.configure();
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			SESSIONFACTORY = configuration.buildSessionFactory(serviceRegistry); 
		}
		return SESSIONFACTORY;
	}
	
	/**
	 * Permette di eseguire il commit di una transazione.
	 * 
	 * @throws Exception segnala un problema di connessione al database
	 */
	public static void commitTransaction() throws Exception{
    	HibernateUtil.getSession().getTransaction().commit();
    }
	
	/**
	 * Ritorna la sessione attualmente aperta.
	 * 
	 * @throws Exception segnala un problema di connessione al database
	 * @return la sessione attualmente aperta
	 */
	public static Session getSession() throws Exception {
    	return getSessionFactory().getCurrentSession();
    }
	
	/**
	 * Permette di chiudere la sessione.
	 * 
	 * @throws Exception segnala un problema di connessione al database
	 */
	public static void closeSession() throws Exception {
    	HibernateUtil.getSession().close();
    }
}
 
