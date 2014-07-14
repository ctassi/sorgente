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
 * 	File contentente la classe DAOManagementProcessAdminTest
 * 
 *	@file		DAOManagementProcessAdminTest.java
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
import com.sequenziatore.server.entity.DataCollection.EnumState;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.entity.Step.EnumParallelism;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe DAOManagementProcessAdmin.
 *
 *	@author 	DeSQ
 */
public class DAOManagementProcessAdminTest {

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
	 *	Test del metodo findMinStep() che permette di cercare nel database il passo minimo  di un processo non completato da un utente.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindMinStep() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
        testProcess.setTotalLevel(2);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep1 = new Step();
        testStep1.setAdminVerify(false);
        testStep1.setDescription("test");
        testStep1.setIsGeolocation(false);
        testStep1.setIsPhoto(false);
        testStep1.setIsText(false);
        testStep1.setLevel(1);
        testStep1.setParallelism(EnumParallelism.NOT);
        testStep1.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep1);
        
        //creazione di un passo per il processo
        Step testStep2 = new Step();
        testStep2.setAdminVerify(false);
        testStep2.setDescription("test");
        testStep2.setIsGeolocation(false);
        testStep2.setIsPhoto(false);
        testStep2.setIsText(false);
        testStep2.setLevel(2);
        testStep2.setParallelism(EnumParallelism.NOT);
        testStep2.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep2);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep1);
        testStepList.add(testStep2);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
    	//iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        
        //creazione di una data collection
        DataCollection testCollection1 = new DataCollection();
        testCollection1.setState(EnumState.VERIFIED);
        testCollection1.setIdStep(testStep1);
        testCollection1.setIdUser(testUser);
        HibernateUtil.getSession().saveOrUpdate(testCollection1);
	    
	    //creazione di una data collection
        DataCollection testCollection2 = new DataCollection();
        testCollection2.setState(EnumState.NOTCOLLECTED);
        testCollection2.setIdStep(testStep2);
        testCollection2.setIdUser(testUser);
        HibernateUtil.getSession().saveOrUpdate(testCollection2);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        Integer minLevel = daoManagementProcessAdminObject.findMinStep(testUser, testProcess);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(testStep2.getLevel(), minLevel);
    }
    
	/**
	 *	Test del metodo findUserStepComplete() che permette di cercare nel database le data collection degli gli utenti che hanno completato un passo.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindUserStepComplete() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        List<Integer> dataCollectionIdList = daoManagementProcessAdminObject.findUserStepComplete(testStep);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(1, dataCollectionIdList.size());
	    assertEquals(testCollection.getIdCollection(), dataCollectionIdList.get(0));
    }
    
    /**
	 *	Test del metodo findUserSubscriptionComplete() che permette di cercare nel database gli utenti che hanno completato un processo.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindUserSubscriptionComplete() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        List<Integer> userIdList = daoManagementProcessAdminObject.findUserSubscriptionComplete(testProcess);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(1, userIdList.size());
	    assertEquals(testUser.getIdUser(), userIdList.get(0));
    }
    
    /**
	 *	Test del metodo findClosedProcess() che permette di cercare nel database i processi terminati e amministrati dall'amministratore che fa la richiesta.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindClosedProcess() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
        
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
        HibernateUtil.commitTransaction();
        
        HibernateUtil.getSession().beginTransaction();
        List<Process> processList = daoManagementProcessAdminObject.findClosedProcess(testAdmin);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(1, processList.size());
	    assertEquals(testProcess.getDescription(), processList.get(0).getDescription());
    }
    
    /**
	 *		Test del metodo findProcess() che permette di trovare i dati di uno specifico processo partendo dal suo id.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindProcess() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAvailable(false);
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
        processObject = daoManagementProcessAdminObject.findProcess(testProcess);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(testProcess.getDescription(), processObject.getDescription());
    }
    
    /**
	 *	Test del metodo findDataCollectionIdStep() che ritorna le data collection da verificare relative ad un determinato passo.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindDataCollectionIdStep() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        List<DataCollection> dataCollectionList = daoManagementProcessAdminObject.findDataCollectionIdStep(testAdmin, testProcess, testStep.getLevel(), testStep.getIdStep());
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(testCollection.getIdCollection(), dataCollectionList.get(0).getIdCollection());
    }
    
	/**
	 *	Test del metodo findAllUser() che permette di ricevere la lista di tutti gli utenti.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindAllUser() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        List<Integer> userIdList = daoManagementProcessAdminObject.findAllUser();
	    HibernateUtil.commitTransaction();
    
	    assertEquals(1, userIdList.size());
	    assertEquals(testUser.getIdUser(), userIdList.get(0));
    }
	
	/**
	 *	Test del metodo findDataCollectionsLevel() ritorna i livelli dei passi che hanno data collection da verificare.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindDataCollectionsLevel() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        List<Integer> levelList = daoManagementProcessAdminObject.findDataCollectionsLevel(testAdmin, testProcess);
	    HibernateUtil.commitTransaction();
    
	    assertEquals(testStep.getLevel(), levelList.get(0));
    }
	
	/**
	 *	Test del metodo findDataCollectionsProcess() ritorna la lista dei processi che hanno data collection da verificare.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindDataCollectionsProcess() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        List<Process> processList = daoManagementProcessAdminObject.findDataCollectionsProcess(testAdmin);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(testProcess.getIdProcess(), processList.get(0).getIdProcess());
    }
    
	/**
	 *	Test del metodo findDataCollectionsStep() che ritorna la lista dei passi aventi data collection ancora da verificare da un amministratore.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindDataCollectionsStep() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        List<Step> stepList = daoManagementProcessAdminObject.findDataCollectionsStep(testAdmin, testProcess, testStep.getLevel());
	    HibernateUtil.commitTransaction();
    
	    assertEquals(testStep.getIdStep(), stepList.get(0).getIdStep());
    }
    
	/**
	 *	Test del metodo findIdDataCollectionToVerify() ritorna la lista degli id delle data collection da verificare.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindIdDataCollectionToVerify() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        List<Integer> dataCollectionIdList = daoManagementProcessAdminObject.findIdDataCollectionToVerify(testAdmin);
	    HibernateUtil.commitTransaction();
	    
	    assertEquals(testCollection.getIdCollection(), dataCollectionIdList.get(0));
    }
    
	/**
	 *	Test del metodo insertProcess() che permette di inserire nel database un nuovo processo.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testInsertProcess() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
	    
	    HibernateUtil.getSession().beginTransaction();
        daoManagementProcessAdminObject.insertProcess(testProcess);
	    HibernateUtil.commitTransaction();
    }

    /**
	 *	Test del metodo insertSubscription() che permette di inserire nel database una nuova iscrizione ad un processo.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testInsertSubscription() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
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
        
    	//iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        daoManagementProcessAdminObject.insertSubscription(testSubscription);
	    HibernateUtil.commitTransaction();
    }
    
    /**
	 *	Test del metodo insertDatacollection() che permette di inserire nel database una nuova data collection.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testInsertDatacollection() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();

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
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.NOTCOLLECTED);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        daoManagementProcessAdminObject.insertDataCollection(testCollection);
	    HibernateUtil.commitTransaction();
    }
    
    /**
	 *		Test del metodo findDataCollectionByLevel() che permette di trovare una specifica raccolta dati ???
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindDataCollectionByLevel() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
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
        
        //creazione di una data collection
        DataCollection testCollection1 = new DataCollection();
        testCollection1.setState(EnumState.NOTCOLLECTED);
        testCollection1.setIdStep(testStep);
        testCollection1.setIdUser(testUser);
        HibernateUtil.getSession().saveOrUpdate(testCollection1);
	    
	    //creazione di una data collection
        DataCollection testCollection2 = new DataCollection();
        testCollection2.setState(EnumState.NOTCOLLECTED);
        testCollection2.setIdStep(testStep);
        testCollection2.setIdUser(testUser);
        HibernateUtil.getSession().saveOrUpdate(testCollection2);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        List<DataCollection> dataCollectionList = daoManagementProcessAdminObject.findDataCollectionByLevel(testCollection1);
	    HibernateUtil.commitTransaction();
        
        assertEquals(testCollection2.getIdCollection(), dataCollectionList.get(0).getIdCollection());
    }
    
	/**
	 *	Test del metodo findSubscription() che permette di verificare se un utente sta partecipano ad un processo creato dall'amministratore che fa la richiesta.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testFindSubscription() throws Exception {
		IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("testUser");
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
    	
    	//iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
	    HibernateUtil.commitTransaction();
	    
	    HibernateUtil.getSession().beginTransaction();
        Subscription returnedSubscription = daoManagementProcessAdminObject.findSubscription(testUser, testProcess);
	    HibernateUtil.commitTransaction();
        
        assertEquals(testSubscription.getIdSubscription(), returnedSubscription.getIdSubscription());
	}
    
	/**
	 *	Test del metodo findUserSubscription() che permette di trovare tutti gli utenti che partecipano ad un processo creato dall'amministratore che fa la richiesta.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testFindUserSubscription() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
        List<User> userList = daoManagementProcessAdminObject.findUserSubscription(testProcess);
	    HibernateUtil.commitTransaction();
        
        assertEquals(1, userList.size());
    }
    
    /**
	 *	Test del metodo findActiveProcess() che permette di trovare tutti i processi attivi amministrati da un determinato amministratore.
     * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testFindActiveProcess() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
    	
    	//creazione processo1
        Process testProcess1 = new Process();
        testProcess1.setAdmin(testAdmin);
        testProcess1.setAvailable(true);
        testProcess1.setDescription("Processo di test1");
        testProcess1.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess1.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess1.setName("test1");
        testProcess1.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess1.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess1);
        
        //creazione di un passo per il processo1
        Step testStep1 = new Step();
        testStep1.setAdminVerify(false);
        testStep1.setDescription("test1");
        testStep1.setIsGeolocation(false);
        testStep1.setIsPhoto(false);
        testStep1.setIsText(false);
        testStep1.setLevel(1);
        testStep1.setParallelism(EnumParallelism.AND);
        testStep1.setIdProcess(testProcess1);
        HibernateUtil.getSession().saveOrUpdate(testStep1);
        
        //inserimento del passo nel processo1
        List<Step> testStepList1 = new ArrayList<Step>();
        testStepList1.add(testStep1);
        testProcess1.setSteps(testStepList1);
        HibernateUtil.getSession().saveOrUpdate(testProcess1);
        
        //creazione processo2
        Process testProcess2 = new Process();
        testProcess2.setAdmin(testAdmin);
        testProcess2.setAvailable(true);
        testProcess2.setDescription("Processo di test2");
        testProcess2.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess2.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess2.setName("test2");
        testProcess2.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        testProcess2.setTotalLevel(1);
        HibernateUtil.getSession().saveOrUpdate(testProcess2);
        
        //creazione di un passo per il processo2
        Step testStep2 = new Step();
        testStep2.setAdminVerify(false);
        testStep2.setDescription("test2");
        testStep2.setIsGeolocation(false);
        testStep2.setIsPhoto(false);
        testStep2.setIsText(false);
        testStep2.setLevel(1);
        testStep2.setParallelism(EnumParallelism.AND);
        testStep2.setIdProcess(testProcess1);
        HibernateUtil.getSession().saveOrUpdate(testStep2);
        
        //inserimento del passo nel processo2
        List<Step> testStepList2 = new ArrayList<Step>();
        testStepList2.add(testStep2);
        testProcess1.setSteps(testStepList2);
        HibernateUtil.getSession().saveOrUpdate(testProcess1);        
        HibernateUtil.commitTransaction();
        
	    HibernateUtil.getSession().beginTransaction();
        List<Process> processList = daoManagementProcessAdminObject.findActiveProcess(testAdmin);
	    HibernateUtil.commitTransaction();
	    
        assertEquals(2, processList.size());
    }
    
    /**
	 *	Test del metodo findDataCollection() che permette di trovare una specifica raccolta dati.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testFindDataCollection() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
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
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.AND);
        HibernateUtil.getSession().saveOrUpdate(testStep);
    	
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setState(EnumState.NOTCOLLECTED);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        HibernateUtil.getSession().saveOrUpdate(testCollection);
        HibernateUtil.commitTransaction();
        
	    HibernateUtil.getSession().beginTransaction();
        DataCollection dataCollectionObject = daoManagementProcessAdminObject.findDataCollection(testCollection);
	    HibernateUtil.commitTransaction();
	    
        assertEquals(testUser.getIdUser(), dataCollectionObject.getIdUser().getIdUser());
    }
    
    /**
	 *	Test del metodo findAllProcesses() che permette di trovare tutti i processi dell'amministratore che fa la richiesta.
     * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testFindAllProcesses() throws Exception {
    	IDAOManagementProcessAdmin daoManagementProcessAdminObject = new DAOManagementProcessAdmin();
    	
    	//creazione ambiente per l'esecuzione della query
    	HibernateUtil.getSession().beginTransaction();
    	
    	//creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("testAdmin");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
        
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
        
	    HibernateUtil.getSession().beginTransaction();
        List<Process> processListObject = daoManagementProcessAdminObject.findAllProcesses(testAdmin);
	    HibernateUtil.commitTransaction();
	    
        assertEquals(1, processListObject.size());
    }
    

}
