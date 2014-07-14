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
 * 	File contentente la classe DAOUserDataTest
 * 
 *	@file		DAOUserDataTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.dao;

import static org.junit.Assert.*;

import org.hibernate.Query;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.User;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe DAOUserData.
 *
 *	@author 	DeSQ
 */
public class DAOUserDataTest {
	
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
	 *	Test del metodo testFindUserByUsername() che permette di trovare un utente dal suo username.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindUserByUsername() throws Exception {
		DAOUserData daoUserDataObject = new DAOUserData();
		        
		//creazione ambiente per l'esecuzione della query
		HibernateUtil.getSession().beginTransaction();

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
        HibernateUtil.getSession().saveOrUpdate(testUser);
        HibernateUtil.commitTransaction();
        
        User userObject = new User();
        userObject.setUsername("testUsername");
        userObject.setEmail("test@test.com");
        
        //invocazione metodo da testare
        HibernateUtil.getSession().beginTransaction();
        User userData = daoUserDataObject.findUserByUsername(userObject);
	    HibernateUtil.commitTransaction();
        
	    assertEquals(userData.getName(), testUser.getName());
    }
	
	/**
	 *	Test del metodo insertUser() che permette di registrare un nuovo utente.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testInsertUser() throws Exception {
        DAOUserData daoUserDataObject = new DAOUserData();
        
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
        
        //invocazione metodo da testare
        HibernateUtil.getSession().beginTransaction();
        daoUserDataObject.insertUser(testUser);
	    HibernateUtil.commitTransaction();
        
	    assertNotNull(testUser);
    }
    
	/**
	 *	Test del metodo findUser() che permette di controllare le informazioni di login di un utente.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindUser() throws Exception {
        DAOUserData daoUserDataObject = new DAOUserData();
        
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.hashAndSetPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        HibernateUtil.commitTransaction();
        
        User userObject = new User();
        userObject.setUsername(testUser.getUsername());
        userObject.setPassword(testUser.getPassword());
        
        //invocazione metodo da testare
        HibernateUtil.getSession().beginTransaction();
        User returnedUser = daoUserDataObject.findUser(userObject);
	    HibernateUtil.commitTransaction();
        
	    assertNotNull(returnedUser);
	    assertEquals(returnedUser.getName(), testUser.getName());
    }
    
    /**
	 *	Test del metodo findUserById() che permette di cercare un utente a partire dal suo id.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindUserById() throws Exception {
        DAOUserData daoUserDataObject = new DAOUserData();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        HibernateUtil.commitTransaction();
        
        //invocazione metodo da testare
        HibernateUtil.getSession().beginTransaction();
        User userObject = daoUserDataObject.findUserById(testUser);
	    HibernateUtil.commitTransaction();
        
	    assertNotNull(userObject);
    }

}
