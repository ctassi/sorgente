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
 * 	File contentente la classe CtrlAdminTest
 * 
 *	@file		CtrlAdminTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
import org.json.JSONArray;
import org.json.JSONObject;
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
 *	Classe contenente tutti i test di unità dei metodi della classe CtrlAdmin.
 *
 *	@author 	DeSQ
 */
public class CtrlAdminTest {

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
	 *	Test del metodo ctrlReport() che permette ad un amministratore di ricevere un report su un processo.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlReport() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();
        
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
        HibernateUtil.commitTransaction();
		
        String processData = new String("IdProcess: "+testProcess.getIdProcess());
        
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+", "+processData+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlReport(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }

    /**
	 *	Test del metodo ctrlStatisticsAdmin() che permette ad un amministratore di visualizzare le statistiche sui propri processi.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlStatisticsAdmin() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();

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
        testCollection.setPhoto(new String("null"));
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        testCollection.setWrongText(true);
        testCollection.setWrongPhoto(true);
        testCollection.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+", IdProcess: "+testProcess.getIdProcess()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlStatisticsAdmin(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    
    /**
	 *	Test del metodo ctrlViewAdminProcessNotActive() che permette ad un amministratore di visualizzare i propri processi che non sono più attivi.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlViewAdminProcessNotActive() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();

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
        testCollection.setPhoto(new String("null"));
        testCollection.setText("null");
        testCollection.setLongitude("null");
        testCollection.setLatitude("null");
        testCollection.setWrongText(true);
        testCollection.setWrongPhoto(true);
        testCollection.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection);
	    HibernateUtil.commitTransaction();
	    
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+", IdProcess: "+testProcess.getIdProcess()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlViewAdminProcessNotActive(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    
	/**
	 *	Test del metodo ctrlViewProcess() che permette ad un amministratore di visualizzare un processo.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlViewProcess() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();
        
        HibernateUtil.getSession().beginTransaction();
        //creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setIdProcess(testProcess);
        testStep.setParallelism(EnumParallelism.AND);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        HibernateUtil.commitTransaction();
        
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+", IdProcess: "+testProcess.getIdProcess()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlViewProcess(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
	
    /**
	 *	Test del metodo ctrlModifyProcess() che permette ad un amministratore di modificare un processo.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlModifyProcess() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();
        
        HibernateUtil.getSession().beginTransaction();
        //creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setIdProcess(testProcess);
        testStep.setParallelism(EnumParallelism.AND);
        HibernateUtil.getSession().saveOrUpdate(testStep);
        HibernateUtil.commitTransaction();
        
        JSONObject date = new JSONObject();
		date.put("Year", 2020);
		date.put("Month", 12);
		date.put("Day", 31);
		
        String processData = new String("Name: 'test', Description: 'modifiedProcess', TotalLevel: 1, PublicationDate: "+date+", ClosingDate: "+date+", EndSubscriptionDate: "+date+", Available: true");
        JSONArray stepArray = new JSONArray();
        
        JSONObject stepData = new JSONObject();
		stepData.put("Level" , 1);
		stepData.put("Description", "modifiedStep");
		stepData.put("IsPhoto", true);
		stepData.put("IsText", true);
		stepData.put("IsGeolocation", true);
		stepData.put("CheckText", "");
		stepData.put("CheckLatitude", "");
		stepData.put("CheckLongitude", "");
		stepData.put("AdminVerify", false);
		stepData.put("Parallelism" , "NOT");
		stepData.put("IdProcess" , testProcess.getIdProcess());
        stepArray.put(stepData);
        
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+", "+processData+", StepList: "+stepArray+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlModifyProcess(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    
    /**
	 *	Test del metodo ctrlCreateProcess() che permette ad un amministratore di creare un nuovo processo.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlCreateProcess() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();
        Notification notificationObject = new Notification();
        
        HibernateUtil.getSession().beginTransaction();
        //creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
        HibernateUtil.commitTransaction();
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
	    
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setIdProcess(testProcess);
        testStep.setParallelism(EnumParallelism.AND);
        
        JSONObject date = new JSONObject();
		date.put("Year", 2020);
		date.put("Month", 12);
		date.put("Day", 31);
		
        String processData = new String("Name: 'test', Description: 'test', TotalLevel: 1, PublicationDate: "+date+", ClosingDate: "+date+", EndSubscriptionDate: "+date+", Available: true");
        JSONArray stepArray = new JSONArray();
        
        JSONObject stepData = new JSONObject();
		stepData.put("Level" , 1);
		stepData.put("Description", "test");
		stepData.put("IsPhoto", true);
		stepData.put("IsText", true);
		stepData.put("IsGeolocation", true);
		stepData.put("CheckText", "");
		stepData.put("CheckLatitude", "");
		stepData.put("CheckLongitude", "");
		stepData.put("AdminVerify", false);
		stepData.put("Parallelism" , "NOT");
		stepData.put("IdProcess" , testProcess.getIdProcess());
        stepArray.put(stepData);
        
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+", "+processData+", Steps: "+stepArray+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlCreateProcess(request, response, notificationObject);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    
    /**
	 *	Test del metodo ctrlStepValidation() che permette ad un amministratore di valutare i dati di un passo.
     * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testCtrlStepValidation() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();
        
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
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        HibernateUtil.getSession().saveOrUpdate(testProcess);
        
        //creazione di un passo per il processo
        Step testStep = new Step();
        testStep.setAdminVerify(false);
        testStep.setDescription("test");
        testStep.setIsGeolocation(false);
        testStep.setIsPhoto(false);
        testStep.setIsText(false);
        testStep.setLevel(1);
        testStep.setIdProcess(testProcess);
        testStep.setParallelism(EnumParallelism.AND);
        HibernateUtil.getSession().saveOrUpdate(testStep);
    	
        //iscrizione dell'utente al processo 
        Subscription testSubscription = new Subscription();
        testSubscription.setComplete(false);
        testSubscription.setIdUser(testUser);
        testSubscription.setIdProcess(testProcess);
        HibernateUtil.getSession().saveOrUpdate(testSubscription);
        
        //creazione di una data collection
        DataCollection testCollection = new DataCollection();
        testCollection.setText("test");
        testCollection.setState(EnumState.TOVERIFY);
        testCollection.setIdStep(testStep);
        testCollection.setIdUser(testUser);
        testCollection.setWrongText(true);
        testCollection.setWrongPhoto(true);
        testCollection.setWrongGeolocation(true);
        HibernateUtil.getSession().saveOrUpdate(testCollection);
        HibernateUtil.commitTransaction();
        
	    String stringObject = "{IdUser: "+testAdmin.getIdUser().toString()+", Admin: "+testAdmin.getIsAdmin()+", IdCollection: "+testCollection.getIdCollection()+", WrongText: true, WrongGeolocation: true, WrongPhoto: true"+", IdStep: "+testCollection.getIdStep().getIdStep()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlStepValidation(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    
	/**
	 *	Test del metodo ctrlListUserProcess() che permette ad un amministratore di visualizzare la lista degli utenti che partecipano ai processi da lui creati.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testCtrlListUserProcess() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();
        
        //creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);
        
        //creazione processo
        Process testProcess = new Process();
        testProcess.setAdmin(testAdmin);
        testProcess.setAvailable(true);
        testProcess.setDescription("Processo di test");
        testProcess.setEndSubscriptionDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setClosingDate(new GregorianCalendar(2020,12,31,23,59,59));
        testProcess.setTotalLevel(1);
        testProcess.setName("test");
        testProcess.setPublicationDate(new GregorianCalendar(2012,12,31,23,59,59));
        HibernateUtil.getSession().saveOrUpdate(testProcess);  
	    HibernateUtil.commitTransaction();
        
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+", IdProcess: "+testProcess.getIdProcess()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlListUserProcess(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
	
    /**
	 *	Test del metodo ctrlViewAdminProcessActive() che permette ad un amministratore di visualizzare tutti i processi attivi e da lui creati.
     * @throws Exception segnala un problema di connessione al database
	 */
	@Test
    public void testCtrlViewAdminProcessActive() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();
        
        //creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);      
	    HibernateUtil.commitTransaction();
        
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		} 
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlViewAdminProcessActive(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    
    /**
	 *	Test del metodo ctrlStepListValidation() che permette ad un amministratore di visualizzare la lista di tutti i passi ancora da validare.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlStepListValidation() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();
        
        //creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);      
	    HibernateUtil.commitTransaction();
        
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlStepListValidation(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    
    /**
	 *	Test del metodo ctrlViewAllProcesses() che permette ad un amministratore di visualizzare tutti i processi dell'applicazione.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlViewAllProcesses() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlAdmin ctrlAdminObject = new CtrlAdmin();
        
        //creazione ambiente per l'esecuzione della query
        HibernateUtil.getSession().beginTransaction();
        
        //creazione amministratore
        User testAdmin = new User();
        testAdmin.setEmail("asd@asd.com");
        testAdmin.setIsAdmin(true);
        testAdmin.setPassword("test");
        testAdmin.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testAdmin);      
	    HibernateUtil.commitTransaction();
        
	    String stringObject = "{IdUser: "+testAdmin.getIdUser()+", Admin: "+testAdmin.getIsAdmin()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testAdmin.getIdUser().toString())).thenReturn(true);
			when(session.getAttribute(testAdmin.getIdUser().toString()+"Admin")).thenReturn(testAdmin.getIsAdmin());
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlAdminObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlAdminObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlAdminObject.ctrlViewAllProcesses(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
}
