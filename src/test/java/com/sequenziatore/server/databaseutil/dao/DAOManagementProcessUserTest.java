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
 * 	File contentente la classe DAOManagementProcessUserTest
 * 
 *	@file		DAOManagementProcessUserTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.DataCollection.EnumState;
import com.sequenziatore.server.entity.Step.EnumParallelism;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe DAOManagementProcessUser.
 *
 *	@author 	DeSQ
 */
public class DAOManagementProcessUserTest {
	
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
	 *	Test del metodo findClosedSuccessSubscription() che ritorna gli id delle iscrizioni ai processi attualmente chiusi a cui l'utente ha partecipato con successo.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testFindClosedSuccessSubscription() throws Exception {
		DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(false);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setCheckText("null");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(true);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.VERIFIED);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        testCollection.setPhoto("null");
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        HibernateUtil.getSession().saveOrUpdate(testCollection);        
	    HibernateUtil.commitTransaction();
	    
	    User userObject = new User();
	    userObject.setIdUser(testUser.getIdUser());
	    
        HibernateUtil.getSession().beginTransaction();
	    List<Integer> subscriptionIdList = daoManagementProcessUserObject.findClosedSuccessSubscription(userObject);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(subscriptionIdList.get(0), testSubscription.getIdSubscription());
	}
	
	/**
	 * 	Test del metodo deleteSubscription() che elimina l'iscrizione di un utente ad un processo.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testDeleteSubscription() throws Exception {
		DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(false);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setCheckText("null");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.NOTCOLLECTED);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        testCollection.setPhoto("null");
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        HibernateUtil.getSession().saveOrUpdate(testCollection);        
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
	    daoManagementProcessUserObject.deleteSubscription(testUser, testProcess);
	    assertEquals(0, daoManagementProcessUserObject.findActiveProcess(testUser).size());
	    HibernateUtil.commitTransaction();
	}
	
	/**
	 * 	Test del metodo deleteDataCollection() 
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testDeleteDataCollection() throws Exception {
		DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(false);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setCheckText("null");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.NOTCOLLECTED);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        testCollection.setPhoto("null");
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        HibernateUtil.getSession().saveOrUpdate(testCollection);        
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
	    daoManagementProcessUserObject.deleteDataCollection(testUser, testStep);
	    assertNull(daoManagementProcessUserObject.findDataCollection(testUser, testStep));
	    HibernateUtil.commitTransaction();
	}
	
	/**
	 *	Test del metodo findAllClosedSubscription() che ritorna gli id delle iscrizioni ai processi attualmente chiusi a cui l'utente ha partecipato.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testFindAllClosedSubscription() throws Exception {
		DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(false);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setCheckText("null");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.NOTCOLLECTED);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        testCollection.setPhoto("null");
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        HibernateUtil.getSession().saveOrUpdate(testCollection);        
	    HibernateUtil.commitTransaction();
	    
	    User userObject = new User();
	    userObject.setIdUser(testUser.getIdUser());
	    
        HibernateUtil.getSession().beginTransaction();
	    List<Integer> subscriptionIdList = daoManagementProcessUserObject.findAllClosedSubscription(userObject);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(subscriptionIdList.get(0), testSubscription.getIdSubscription());
	}
	
	/**
	 *	Test del metodo findDataCollection() che permette di trovare una data collection di un utente.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testFindDataCollection() throws Exception {
		DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setCheckText("null");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.TOVERIFY);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        testCollection.setPhoto("null");
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        HibernateUtil.getSession().saveOrUpdate(testCollection);        
	    HibernateUtil.commitTransaction();
	    
	    User userObject = new User();
	    userObject.setIdUser(testUser.getIdUser());
	    Step stepObject = new Step();
	    stepObject.setIdStep(testStep.getIdStep());
	    
	    HibernateUtil.getSession().beginTransaction();
	    DataCollection dataCollectionObject = daoManagementProcessUserObject.findDataCollection(userObject, stepObject);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(dataCollectionObject.getIdStep().getDescription(), testCollection.getIdStep().getDescription());
	}
	
	/**
	 *	Test del metodo findDataCollectionByLevel() che permette di trovare la lista delle data collection (appartententi allo stesso livello) di un utente.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testFindDataCollectionByLevel() throws Exception {
		DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione di un passo per il processo
        Step testStep1 = new Step();
        testStep1.setAdminVerify(false);
        testStep1.setDescription("test");
        testStep1.setCheckText("null");
        testStep1.setIsGeolocation(false);
        testStep1.setIsPhoto(false);
        testStep1.setIsText(false);
        testStep1.setLevel(1);
        testStep1.setParallelism(EnumParallelism.OR);
        HibernateUtil.getSession().saveOrUpdate(testStep1);
        
        //creazione di un passo per il processo
        Step testStep2 = new Step();
        testStep2.setAdminVerify(false);
        testStep2.setDescription("test");
        testStep2.setCheckText("null");
        testStep2.setIsGeolocation(false);
        testStep2.setIsPhoto(false);
        testStep2.setIsText(false);
        testStep2.setLevel(1);
        testStep2.setParallelism(EnumParallelism.OR);
        HibernateUtil.getSession().saveOrUpdate(testStep2);
        
        //creazione di una data collection
        DataCollection testCollection1 = new DataCollection();
        testCollection1.setState(EnumState.TOVERIFY);
        testCollection1.setIdStep(testStep1);
        testCollection1.setIdUser(testUser);
        testCollection1.setPhoto("null");
        testCollection1.setText("null");
        testCollection1.setLongitude("null");
        testCollection1.setLatitude("null");
        HibernateUtil.getSession().saveOrUpdate(testCollection1);
        
        //creazione di una data collection
        DataCollection testCollection2 = new DataCollection();
        testCollection2.setState(EnumState.TOVERIFY);
        testCollection2.setIdStep(testStep2);
        testCollection2.setIdUser(testUser);
        testCollection2.setPhoto("null");
        testCollection2.setText("null");
        testCollection2.setLongitude("null");
        testCollection2.setLatitude("null");
        HibernateUtil.getSession().saveOrUpdate(testCollection2); 
	    HibernateUtil.commitTransaction();
	    	    
	    HibernateUtil.getSession().beginTransaction();
	    List<DataCollection> dataCollectionList = daoManagementProcessUserObject.findDataCollectionByLevel(testCollection1);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(dataCollectionList.get(0).getIdCollection(), testCollection2.getIdCollection());
	    assertEquals(dataCollectionList.get(0).getIdUser().getName(), testCollection1.getIdUser().getName());
	}
	
	/**
	 *	Test del metodo findMinStep() che ritorna il livello minimo del passo a cui l'utente è arrivato.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testFindMinStep() throws Exception {
		DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setCheckText("null");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.TOVERIFY);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        testCollection.setPhoto("null");
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        HibernateUtil.getSession().saveOrUpdate(testCollection);        
	    HibernateUtil.commitTransaction();
	    
        HibernateUtil.getSession().beginTransaction();
	    Integer minLevel = daoManagementProcessUserObject.findMinStep(testUser, testProcess);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(minLevel, testStep.getLevel());
    	
	}
	
	/**
	 *	Test del metodo findProcessById() che permette di trovare un processo a partire dal suo id.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testFindProcessById() throws Exception {
		DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
        
        //creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
	    HibernateUtil.commitTransaction();
	    
	    Process processObject = new Process();
	    processObject.setIdProcess(testProcess.getIdProcess());
	    
        HibernateUtil.getSession().beginTransaction();
        Process processReturnedObject = daoManagementProcessUserObject.findProcessById(processObject);
	    HibernateUtil.commitTransaction();

	    assertEquals(testProcess.getDescription(), processReturnedObject.getDescription());
	}
	
	/**
	 *	Test del metodo findSubscription() che permette di trovare l'iscrizione di un utente ad un processo.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testFindSubscription() throws Exception {
		DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
        
        //creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
	    HibernateUtil.commitTransaction();
	    
	    User userObject = new User();
	    userObject.setIdUser(testUser.getIdUser());

	    Process processObject = new Process();
	    processObject.setIdProcess(testProcess.getIdProcess());
	    
        HibernateUtil.getSession().beginTransaction();
        Subscription subscriptionObject = daoManagementProcessUserObject.findSubscription(userObject, processObject);
	    HibernateUtil.commitTransaction();
	
	    assertEquals(testSubscription.getIdUser().getIdUser(), subscriptionObject.getIdUser().getIdUser());
	}
	
	/**
	 *	Test del metodo insertSubscription() che permette ad un utente di iscriversi ad un nuovo processo.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testInsertSubscription() throws Exception {
    	DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
        
        //creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
	    HibernateUtil.commitTransaction();
        
        //creazione dell'oggetto Subscription
        Subscription newSubscriprtion = new Subscription();
        newSubscriprtion.setIdUser(testUser);
        newSubscriprtion.setIdProcess(testProcess);
        newSubscriprtion.setComplete(false);
        
        HibernateUtil.getSession().beginTransaction();
        daoManagementProcessUserObject.insertSubscription(newSubscriprtion);
	    HibernateUtil.commitTransaction();
	    
	    assertFalse(newSubscriprtion.getComplete());
    }
    
	/**
	 *	Test del metodo findAvailableProcess() che permette di trovare tutti i processi a cui l'utente non è ancora iscritto.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testFindAvailableProcess() throws Exception {
        DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
        
        //creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList1 = new ArrayList<Step>();
        testStepList1.add(testStep);
        testProcess.setSteps(testStepList1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);        
        HibernateUtil.commitTransaction();
	    
        HibernateUtil.getSession().beginTransaction();
        List<Process> processList = daoManagementProcessUserObject.findAvailableProcess(testUser);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(1, processList.size());
    }
    
    /**
	 *	Test del metodo findActiveProcess() che permette di trovare tutti i processi a cui l'utente è iscritto.
     * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testFindActiveProcess() throws Exception {
        DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
        
        //creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
	    HibernateUtil.commitTransaction();
	    
        HibernateUtil.getSession().beginTransaction();
        List<Subscription> subscriptionList = daoManagementProcessUserObject.findActiveProcess(testUser);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(1, subscriptionList.size());
    }
	
    /**
	 *	Test del metodo findStep() che permette di recuperare le data collection da completare e i dati dei corrispondenti passi.
	 *	Vengono testati entrambi i metodi: findStep(User, Process, Integer) e findStep(Step).
     * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testFindStep() throws Exception {
    	DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setCheckText("null");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.TOVERIFY);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        testCollection.setPhoto("null");
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        HibernateUtil.getSession().saveOrUpdate(testCollection);        
	    HibernateUtil.commitTransaction();
	    
        HibernateUtil.getSession().beginTransaction();
	    Integer minLevel = daoManagementProcessUserObject.findMinStep(testUser, testProcess);
	    HibernateUtil.commitTransaction();
        
	    //test metodo: public List<DataCollection> findStep(User user, Process process, Integer minLevel)
	    HibernateUtil.getSession().beginTransaction();
	    List<DataCollection> dataCollectionList = daoManagementProcessUserObject.findStep(testUser, testProcess, minLevel);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(1, dataCollectionList.size());
	    assertEquals(testStep.getIdStep(), dataCollectionList.get(0).getIdStep().getIdStep());
	    
	    //test metodo: public Step findStep(Step step)
	    HibernateUtil.getSession().beginTransaction();
	    Step returnedStep = daoManagementProcessUserObject.findStep(testStep);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(testStep.getIdStep(), returnedStep.getIdStep());
    }
    
    /**
	 *	Test del metodo insertDataCollection() che permette di completare un passo caricando una raccolta dati.
     * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testInsertDataCollection() throws Exception {
    	DAOManagementProcessUser daoManagementProcessUserObject = new DAOManagementProcessUser();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
    	
    	//creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
        HibernateUtil.getSession().saveOrUpdate(testUser);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setCheckText("null");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);     
	    HibernateUtil.commitTransaction();
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.TOVERIFY);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        testCollection.setPhoto("null");
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
    	
        HibernateUtil.getSession().beginTransaction();
        daoManagementProcessUserObject.insertDataCollection(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    assertNotNull(testCollection);
    }

}
