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
 * 	File contentente la classe CtrlUserTest
 * 
 *	@file		CtrlUserTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.entity.DataCollection.EnumState;
import com.sequenziatore.server.entity.Step.EnumParallelism;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe CtrlUser.
 *
 *	@author 	DeSQ
 */
public class CtrlUserTest {
	
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
	 *	Test del metodo ctrlUnsubscribeProcess() che permette ad un utente di disiscriversi da un processo.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testCtrlUnsubscribeProcess() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlUser ctrlUserObject = new CtrlUser();

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

        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        HibernateUtil.commitTransaction();
	    
	    String stringObject = "{IdUser: "+testUser.getIdUser()+", Admin: "+testUser.getIsAdmin()+", IdProcess: "+testProcess.getIdProcess()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			when(request.getContentType()).thenReturn("application/json");
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testUser.getIdUser().toString()+"Admin")).thenReturn(testUser.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlUserObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlUserObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlUserObject.ctrlUnsubscribeProcess(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
	}
	
	/**
	 *	Test del metodo ctrlViewProcessNotActive() che permette ad un utente di visualizzare i processi che non sono più attivi.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlViewProcessNotActive() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlUser ctrlUserObject = new CtrlUser();

        //creazione ambiente per l'esecuzione della query
	    HibernateUtil.getSession().beginTransaction();
	    
	    //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
	    HibernateUtil.commitTransaction();
	    
        String stringObject = "{IdUser: "+testUser.getIdUser()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testUser.getIdUser().toString()+"Admin")).thenReturn(testUser.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlUserObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlUserObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlUserObject.ctrlViewProcessNotActive(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
	
	/**
	 *	Test del metodo ctrlStatisticsUser) che permette di ricevere le statistiche dei processi di un utente.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testCtrlStatisticsUser() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlUser ctrlUserObject = new CtrlUser();

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
        testStep.setIsGeolocation(true);
        testStep.setIsPhoto(true);
        testStep.setIsText(true);
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
        testCollection.setPhoto(new String("null"));
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        testCollection.setWrongText(true);
        testCollection.setWrongPhoto(true);
        testCollection.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    String dataCollectionInfo = new String("WrongText: true, WrongGeolocation: true, WrongPhoto: true");
	    String stringObject = "{IdUser: "+testUser.getIdUser()+", Admin: "+testUser.getIsAdmin()+", IdStep: "+testCollection.getIdStep().getIdStep()+", "+dataCollectionInfo+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			when(request.getContentType()).thenReturn("application/json");
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testUser.getIdUser().toString()+"Admin")).thenReturn(testUser.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlUserObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlUserObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlUserObject.ctrlStatisticsUser(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
	}
	
	/**
	 *	Test del metodo ctrlSaveDataStep() che permette di salvare i dati raccolti per completare un passo.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testCtrlSaveDataStep() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlUser ctrlUserObject = new CtrlUser();

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
        testStep.setIsGeolocation(true);
        testStep.setIsPhoto(true);
        testStep.setIsText(true);
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
        testCollection.setPhoto(new String("null"));
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        testCollection.setWrongText(true);
        testCollection.setWrongPhoto(true);
        testCollection.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    String dataCollectionInfo = new String("WrongText: true, WrongGeolocation: true, WrongPhoto: true");
	    String stringObject = "{IdUser: "+testUser.getIdUser()+", Admin: "+testUser.getIsAdmin()+", IdStep: "+testCollection.getIdStep().getIdStep()+", "+dataCollectionInfo+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			when(request.getContentType()).thenReturn("application/json");
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testUser.getIdUser().toString()+"Admin")).thenReturn(testUser.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlUserObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlUserObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlUserObject.ctrlSaveDataStep(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    
	/**
	 *	Test del metodo ctrlSubscribeToProcess() che permette ad un utente di iscriversi ad un processo.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testCtrlSubscribeToProcess() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlUser ctrlUserObject = new CtrlUser();

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
        
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
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
        testStep.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        
        //inserimento del passo nel processo
        List<Step> testStepList = new ArrayList<Step>();
        testStepList.add(testStep);
        testProcess.setSteps(testStepList);
        HibernateUtil.getSession().saveOrUpdate(testProcess);
	    HibernateUtil.commitTransaction();

	    String stringObject = "{IdUser: "+testUser.getIdUser()+", Admin: "+testUser.getIsAdmin()+", IdProcess: "+testProcess.getIdProcess()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testUser.getIdUser().toString()+"Admin")).thenReturn(testUser.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlUserObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlUserObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlUserObject.ctrlSubscribeToProcess(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
	
    /**
	 *	Test del metodo ctrlViewStep() che permette la visualizzazione di un passo da svolgere.
     * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testCtrlViewStep() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlUser ctrlUserObject = new CtrlUser();

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
        
	    String stringObject = "{IdUser: "+testUser.getIdUser()+", Admin: "+testUser.getIsAdmin()+", IdProcess: "+testProcess.getIdProcess()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testUser.getIdUser().toString()+"Admin")).thenReturn(testUser.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlUserObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlUserObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlUserObject.ctrlViewStep(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
	
	/**
	 *	Test del metodo ctrlAvailableProcess() che ritorna la lista dei processi a cui l'utente può iscriversi.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlAvailableProcess() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlUser ctrlUserObject = new CtrlUser();

        //creazione ambiente per l'esecuzione della query
	    HibernateUtil.getSession().beginTransaction();
	    
	    //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
	    HibernateUtil.commitTransaction();
	    
        String stringObject = "{IdUser: "+testUser.getIdUser()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testUser.getIdUser().toString()+"Admin")).thenReturn(testUser.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlUserObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlUserObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlUserObject.ctrlAvailableProcess(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }

    /**
	 *	Test del metodo ctrlViewActiveProcesses() che ritorna la lista dei processi attivi a cui l'utente è iscritto.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlViewActiveProcesses() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlUser ctrlUserObject = new CtrlUser();

        //creazione ambiente per l'esecuzione della query
	    HibernateUtil.getSession().beginTransaction();
	    
	    //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
	    HibernateUtil.commitTransaction();
	    
        String stringObject = "{IdUser: "+testUser.getIdUser()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testUser.getIdUser().toString()+"Admin")).thenReturn(testUser.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlUserObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlUserObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlUserObject.ctrlViewActiveProcesses(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
}
