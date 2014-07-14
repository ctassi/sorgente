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
 * 	File contentente la classe ServiceStepValidationTest
 * 
 *	@file		ServiceStepValidationTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.service.admin;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.entity.DataCollection.EnumState;
import com.sequenziatore.server.entity.Step.EnumParallelism;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe ServiceStepValidation.
 *
 *	@author 	DeSQ
 */
public class ServiceStepValidationTest {

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
	 *	Test del metodo serviceOperation() che permette la validazione di una raccolta dati con passi in AND.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testServiceOperation_AND() throws Exception {
    	ServiceStepValidation serviceStepValidationObject = new ServiceStepValidation();
    	
    	//creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("test");
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
        testProcess.setTotalLevel(1);
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));;
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep1 = new Step();
        testStep1.setAdminVerify(false);
        testStep1.setDescription("test");
        testStep1.setIsGeolocation(false);
        testStep1.setIsPhoto(false);
        testStep1.setIsText(false);
        testStep1.setLevel(1);
        testStep1.setIdProcess(testProcess);
        testStep1.setParallelism(EnumParallelism.AND);
        HibernateUtil.getSession().saveOrUpdate(testStep1);
        
        //creazione di un passo per il processo
        Step testStep2 = new Step();
        testStep2.setAdminVerify(false);
        testStep2.setDescription("test");
        testStep2.setIsGeolocation(false);
        testStep2.setIsPhoto(false);
        testStep2.setIsText(false);
        testStep2.setLevel(1);
        testStep2.setIdProcess(testProcess);
        testStep2.setParallelism(EnumParallelism.AND);
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
        testCollection1.setText("test");
        testCollection1.setState(EnumState.TOVERIFY);
        testCollection1.setIdStep(testStep1);
        testCollection1.setIdUser(testUser);
        testCollection1.setWrongText(true);
        testCollection1.setWrongPhoto(true);
        testCollection1.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection1);
        
        //creazione di una data collection
        DataCollection testCollection2 = new DataCollection();
        testCollection2.setText("test");
        testCollection2.setState(EnumState.FAILED);
        testCollection2.setIdStep(testStep2);
        testCollection2.setIdUser(testUser);
        testCollection2.setWrongText(true);
        testCollection2.setWrongPhoto(true);
        testCollection2.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection2);
        HibernateUtil.commitTransaction();
        
        //creazione della lista di entità
	    List<IEntity> iEntityList = new ArrayList<IEntity>();
	    iEntityList.add(testCollection1);
	    
	    JSONObject jsonReturnedObject = serviceStepValidationObject.serviceOperation(iEntityList);
	    
	    assertEquals("successValidation", jsonReturnedObject.get("Confirmation"));
	    
	    HibernateUtil.getSession().beginTransaction();
	    testCollection1.setState(EnumState.TOVERIFY);
	    HibernateUtil.getSession().saveOrUpdate(testCollection1);
	    
	    testCollection2.setState(EnumState.TOVERIFY);
	    HibernateUtil.getSession().saveOrUpdate(testCollection2);
	    
	    testSubscription.setComplete(false);
	    HibernateUtil.getSession().saveOrUpdate(testSubscription);
	    HibernateUtil.commitTransaction();
	    
	    jsonReturnedObject = serviceStepValidationObject.serviceOperation(iEntityList);
        
	    assertEquals("successValidation", jsonReturnedObject.get("Confirmation"));
	    
	    HibernateUtil.getSession().beginTransaction();
	    testCollection1.setState(EnumState.TOVERIFY);
	    HibernateUtil.getSession().saveOrUpdate(testCollection1);
	    
	    testCollection2.setState(EnumState.VERIFIED);
	    HibernateUtil.getSession().saveOrUpdate(testCollection2);
	    
	    testSubscription.setComplete(false);
	    HibernateUtil.getSession().saveOrUpdate(testSubscription);
	    HibernateUtil.commitTransaction();
	    
	    jsonReturnedObject = serviceStepValidationObject.serviceOperation(iEntityList);
        
	    assertEquals("successValidation", jsonReturnedObject.get("Confirmation"));
	    
	    HibernateUtil.getSession().beginTransaction();
	    testCollection1.setState(EnumState.TOVERIFY);
	    testCollection1.setWrongText(false);
        testCollection1.setWrongPhoto(true);
        testCollection1.setWrongGeolocation(true);
	    HibernateUtil.getSession().saveOrUpdate(testCollection1);
	    
	    testCollection2.setState(EnumState.VERIFIED);
	    testCollection2.setWrongText(false);
        testCollection2.setWrongPhoto(true);
        testCollection2.setWrongGeolocation(true);
	    HibernateUtil.getSession().saveOrUpdate(testCollection2);
	    
	    testSubscription.setComplete(false);
	    HibernateUtil.getSession().saveOrUpdate(testSubscription);
	    HibernateUtil.commitTransaction();
	    
	    jsonReturnedObject = serviceStepValidationObject.serviceOperation(iEntityList);
        
	    assertEquals("successValidation", jsonReturnedObject.get("Confirmation"));
    }
    
    /**
	 *	Test del metodo serviceOperation() che permette la validazione di una raccolta dati con passi in OR.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testserviceOperation_OR() throws Exception {
    	ServiceStepValidation serviceStepValidationObject = new ServiceStepValidation();
    	
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
        Step testStep1 = new Step();
        testStep1.setAdminVerify(true);
        testStep1.setDescription("test");
        testStep1.setCheckText("null");
        testStep1.setIsGeolocation(false);
        testStep1.setIsPhoto(false);
        testStep1.setIsText(false);
        testStep1.setLevel(1);
        testStep1.setParallelism(EnumParallelism.OR);
        testStep1.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep1);
        
        //creazione di un passo per il processo
        Step testStep2 = new Step();
        testStep2.setAdminVerify(true);
        testStep2.setDescription("test");
        testStep2.setCheckText("null");
        testStep2.setIsGeolocation(false);
        testStep2.setIsPhoto(false);
        testStep2.setIsText(false);
        testStep2.setLevel(1);
        testStep2.setParallelism(EnumParallelism.OR);
        testStep2.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep2);
        
        //inserimento dei passi nel processo
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
        testCollection1.setState(EnumState.TOVERIFY);
        testCollection1.setIdStep(testStep1);
        testCollection1.setIdUser(testUser);
        testCollection1.setPhoto("null");
        testCollection1.setText("null");
        testCollection1.setLongitude("null");
        testCollection1.setLatitude("null");
	    testCollection1.setWrongText(true);
        testCollection1.setWrongPhoto(true);
        testCollection1.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection1);
        
        //creazione di una data collection
        DataCollection testCollection2 = new DataCollection();
        testCollection2.setState(EnumState.TOVERIFY);
        testCollection2.setIdStep(testStep1);
        testCollection2.setIdUser(testUser);
        testCollection2.setPhoto("null");
        testCollection2.setText("null");
        testCollection2.setLongitude("null");
        testCollection2.setLatitude("null");
        testCollection2.setWrongText(true);
        testCollection2.setWrongPhoto(true);
        testCollection2.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection2);
	    HibernateUtil.commitTransaction();
        
	    //creazione della lista di entità
	    List<IEntity> iEntityList = new ArrayList<IEntity>();
	    iEntityList.add(testCollection1);
        
        JSONObject jsonReturnedObject = serviceStepValidationObject.serviceOperation(iEntityList);
        
	    assertEquals("successValidation", jsonReturnedObject.get("Confirmation"));
	    
	    HibernateUtil.getSession().beginTransaction();
	    testCollection1.setState(EnumState.TOVERIFY);
	    testCollection1.setWrongText(false);
        testCollection1.setWrongPhoto(true);
        testCollection1.setWrongGeolocation(true);
	    HibernateUtil.getSession().saveOrUpdate(testCollection1);
	    
	    testCollection2.setState(EnumState.VERIFIED);
	    testCollection2.setWrongText(false);
        testCollection2.setWrongPhoto(true);
        testCollection2.setWrongGeolocation(true);
	    HibernateUtil.getSession().saveOrUpdate(testCollection2);
	    
	    testSubscription.setComplete(false);
	    HibernateUtil.getSession().saveOrUpdate(testSubscription);
	    HibernateUtil.commitTransaction();
	    
	    jsonReturnedObject = serviceStepValidationObject.serviceOperation(iEntityList);
        
	    assertEquals("successValidation", jsonReturnedObject.get("Confirmation"));
    }
    
    /**
	 *	Test del metodo serviceOperation() che permette la validazione di una raccolta dati con passi in NOT.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testServiceOperation_NOT() throws Exception {
    	ServiceStepValidation serviceStepValidationObject = new ServiceStepValidation();
    	
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
        testStep.setAdminVerify(true);
        testStep.setDescription("test");
        testStep.setCheckText("null");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setParallelism(EnumParallelism.NOT);
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
	    testCollection.setWrongText(true);
        testCollection.setWrongPhoto(true);
        testCollection.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection);        
	    HibernateUtil.commitTransaction();
        
	    //creazione della lista di entità
	    List<IEntity> iEntityList = new ArrayList<IEntity>();
	    iEntityList.add(testCollection);
        
	    JSONObject jsonReturnedObject = serviceStepValidationObject.serviceOperation(iEntityList);
        
	    assertEquals("successValidation", jsonReturnedObject.get("Confirmation"));
	    
	    HibernateUtil.getSession().beginTransaction();
	    testCollection.setState(EnumState.TOVERIFY);
	    testCollection.setWrongText(false);
        testCollection.setWrongPhoto(true);
        testCollection.setWrongGeolocation(true);
	    HibernateUtil.getSession().saveOrUpdate(testCollection);
	    
	    testSubscription.setComplete(false);
	    HibernateUtil.getSession().saveOrUpdate(testSubscription);
	    HibernateUtil.commitTransaction();
	    
	    jsonReturnedObject = serviceStepValidationObject.serviceOperation(iEntityList);
        
	    assertEquals("successValidation", jsonReturnedObject.get("Confirmation"));
    }

}
