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
 * 	File contentente la classe HibernateUtilTest
 * 
 *	@file		HibernateUtilTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.util;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe HibernateUtil.
 *
 *	@author 	DeSQ
 */
public class HibernateUtilTest {

	/**
	 *	Test del metodo getSessionFactory() che ritorna la SessionFactory.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testGetSessionFactory() throws Exception {
    	SessionFactory sessionFactoryObject = HibernateUtil.getSessionFactory();
    	assertNotNull(sessionFactoryObject);
    }
    
    /**
	 *	Test del metodo getSession() che ritorna la sessione.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testGetSession() throws Exception {
    	Session sessionObject = HibernateUtil.getSession();
    	assertNotNull(sessionObject);
    }
    
    /**
	 *	Test del metodo commitTransaction() che salva i dati della sessione attualmente attiva.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCommitTransaction() throws Exception {
    	Session sessionObject = HibernateUtil.getSession();
    	sessionObject.beginTransaction();

    	assertTrue(sessionObject.isConnected());
    	
    	HibernateUtil.commitTransaction();
    	
    	assertFalse(sessionObject.isConnected());
    }
    
    /**
	 *	Test del metodo closeSession() che chiude la sessione attualmente attiva.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCloseSession() throws Exception {
    	Session sessionObject = HibernateUtil.getSession();
    	sessionObject.beginTransaction();
    	
    	assertTrue(sessionObject.isConnected());
    	
    	HibernateUtil.closeSession();
    	
    	assertFalse(sessionObject.isConnected());
    }

}
