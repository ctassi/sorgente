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
 * 	File contentente la classe ServiceModifyAccountTest
 * 
 *	@file		ServiceModifyAccountTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.service.shared;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.User;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe ServiceModifyAccount.
 *
 *	@author 	DeSQ
 */
public class ServiceModifyAccountTest {

	/**
	 * Pulisce il database prima di iniziare ad eseguire i test.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Before
	public void clean() throws Exception
	{
		//pulizia database
	    HibernateUtil.getSession().beginTransaction();
	    
	    //elimina le data collection
	    Query deleteDataCollection = HibernateUtil.getSession().createQuery("delete DataCollection");
	    deleteDataCollection.executeUpdate();
	    
	    //elimina le iscrizioni ai processi
	    Query unsubscribeToProcess = HibernateUtil.getSession().createQuery("delete Subscription");
	    unsubscribeToProcess.executeUpdate();
	    
	    //elimina i passi di tutti i processi
	    Query deleteTestStep = HibernateUtil.getSession().createQuery("delete Step");
	    deleteTestStep.executeUpdate();
	    
	    //elimina i processi
	    Query deleteTestProcess = HibernateUtil.getSession().createQuery("delete Process");
	    deleteTestProcess.executeUpdate();
	    
	    //elimina gli utenti registrati
	    Query deleteTestUser = HibernateUtil.getSession().createQuery("delete User");
	    deleteTestUser.executeUpdate();
	    
	    HibernateUtil.commitTransaction();
	}
	
	/**
	 *	Pulisce il database quando sono terminati tutti i test.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@AfterClass
	public static void finalClean() throws Exception
	{
		//pulizia database
	    HibernateUtil.getSession().beginTransaction();
	    
	    //elimina le data collection
	    Query deleteDataCollection = HibernateUtil.getSession().createQuery("delete DataCollection");
	    deleteDataCollection.executeUpdate();
	    
	    //elimina le iscrizioni ai processi
	    Query unsubscribeToProcess = HibernateUtil.getSession().createQuery("delete Subscription");
	    unsubscribeToProcess.executeUpdate();
	    
	    //elimina i passi di tutti i processi
	    Query deleteTestStep = HibernateUtil.getSession().createQuery("delete Step");
	    deleteTestStep.executeUpdate();
	    
	    //elimina i processi
	    Query deleteTestProcess = HibernateUtil.getSession().createQuery("delete Process");
	    deleteTestProcess.executeUpdate();
	    
	    //elimina gli utenti registrati
	    Query deleteTestUser = HibernateUtil.getSession().createQuery("delete User");
	    deleteTestUser.executeUpdate();
	    
	    HibernateUtil.commitTransaction();
	}
	
	/**
	 *	Test del metodo serviceOperation() che permette la modifica dei dati di un utente.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testServiceOperation() throws Exception {
		ServiceModifyAccount serviceModifyAccountObject = new ServiceModifyAccount();
		
		//creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setName("test");
        testUser.setSurname("test");
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        HibernateUtil.commitTransaction();
        
        //dati dell'utente modificati
        User userObject = new User();
        userObject.setIdUser(testUser.getIdUser());
        userObject.setEmail("asd@asd.com");
        userObject.setIsAdmin(false);
        userObject.setPassword("modifiedPassword");
        userObject.setUsername("test");
        userObject.setSurname("testSurname");
        userObject.setName("testName");
        
        //password vecchia dell'utente
        User userPassword = new User();
        userPassword.setPassword("test");
        
        //creazione della lista di entità
	    List<IEntity> iEntityList = new ArrayList<IEntity>();
	    iEntityList.add(userObject);
	    iEntityList.add(userPassword);
	    
	    //dati utente corretti
	    JSONObject jsonObject = serviceModifyAccountObject.serviceOperation(iEntityList);
	    assertEquals(jsonObject.get("Confirmation"), "registrationOK");
	    HibernateUtil.getSession().beginTransaction();
	    User userData = new DAOFactory().createDAOUserData().findUserByUsername(userObject);
	    HibernateUtil.commitTransaction();
        assertEquals(userData.getName(), userObject.getName());
	    
	    //dati utente corretti ma password non corretta
	    userPassword.setPassword("wrongPassword");
	    jsonObject = serviceModifyAccountObject.serviceOperation(iEntityList);
	    assertEquals(jsonObject.get("Confirmation"), "wrongPassword");

	    //dati utente corretti e password non inserita
	    userObject.setPassword(null);
	    jsonObject = serviceModifyAccountObject.serviceOperation(iEntityList);
	    assertEquals(jsonObject.get("Confirmation"), "registrationOK");
	    
	    //dati utente corretti ma IdUser diverso
	    userObject.setIdUser(testUser.getIdUser()+10);
	    jsonObject = serviceModifyAccountObject.serviceOperation(iEntityList);
	    assertEquals(jsonObject.get("Confirmation"), "userAndMailNotAvailable");
	    
	    //Username ed IdUser errati
	    userObject.setUsername("testUsername");
	    jsonObject = serviceModifyAccountObject.serviceOperation(iEntityList);
	    assertEquals(jsonObject.get("Confirmation"), "emailNotAvailable");
	}
	
}
