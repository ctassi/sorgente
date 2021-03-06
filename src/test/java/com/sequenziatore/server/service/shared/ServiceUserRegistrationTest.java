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
 * 	File contentente la classe ServiceUserRegistrationTest
 * 
 *	@file		ServiceUserRegistrationTest.java
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

import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.User;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe ServiceUserRegistration.
 *
 *	@author 	DeSQ
 */
public class ServiceUserRegistrationTest {

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
	 *	Test del metodo serviceOperation() che permette la registrazione di un utente al sistema.
	 */
    @Test
    public void testServiceOperation() {
    	ServiceUserRegistration serviceUserRegistrationObject = new ServiceUserRegistration();
    	
    	//creazione utente
        User testUser = new User();
        testUser.setIsAdmin(false);
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setEmail("test@test.com");
        testUser.setName("testName");
        testUser.setSurname("testSurname");
        testUser.setCity("testCity");
        testUser.setDistrict("PD");
        
    	//creazione della lista di entità
	    List<IEntity> iEntityList = new ArrayList<IEntity>();
	    iEntityList.add(testUser);
    	
    	JSONObject jsonReturnedObject = serviceUserRegistrationObject.serviceOperation(iEntityList);
    	assertEquals("registrationOK", jsonReturnedObject.get("Confirmation"));
    	
    	//invocazione con utente già registrato
    	jsonReturnedObject = serviceUserRegistrationObject.serviceOperation(iEntityList);
    	assertEquals("userAndMailNotAvailable", jsonReturnedObject.get("Confirmation"));
    
    	//invocazione con username già registrato
    	testUser.setEmail("abc@abc.com");
    	jsonReturnedObject = serviceUserRegistrationObject.serviceOperation(iEntityList);
    	assertEquals("usernameNotAvailable", jsonReturnedObject.get("Confirmation"));
    
    	//invocazione con email già registrata
    	testUser.setUsername("newUser");
    	testUser.setEmail("test@test.com");
    	jsonReturnedObject = serviceUserRegistrationObject.serviceOperation(iEntityList);
    	assertEquals("emailNotAvailable", jsonReturnedObject.get("Confirmation"));
    }

}
