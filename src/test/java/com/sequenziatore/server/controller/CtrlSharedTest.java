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
 * 	File contentente la classe CtrlSharedTest
 * 
 *	@file		CtrlSharedTest.java
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.User;

/**
 *	Classe contenente tutti i test di unità dei metodi della classe CtrlShared.
 *
 *	@author 	DeSQ
 */
public class CtrlSharedTest {

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
	 *	Test del metodo ctrlUserFacebookLogin() che permette ad un utente di loggarsi tramite Facebook.
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Test
	public void testCtrlUserFacebookLogin() throws Exception{
		HttpServletRequest request = mock(HttpServletRequest.class);       
		HttpServletResponse response = mock(HttpServletResponse.class);    
		HttpSession session= mock(HttpSession.class);
		CtrlShared ctrlSharedObject = new CtrlShared();
	     
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
		
	    String stringObject = "{Username: 'testUsername', Password: 'testPassword', "
        		+ "Name: 'testName', Surname: 'testSurname', Email: 'testEmail', "
        		+ "City: 'testCity', District: 'Di', IsAdmin: false}";
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
			iParserObject = ctrlSharedObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlSharedObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlSharedObject.ctrlUserFacebookLogin(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
	}
	
	/**
	 *	Test del metodo ctrlModifyAccount() che permette all'utente di modificare i dati del proprio account.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlModifyAccount() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlShared ctrlSharedObject = new CtrlShared();

        //creazione ambiente per l'esecuzione della query
	    HibernateUtil.getSession().beginTransaction();
	    
        //creazione utente
        User testUser = new User();
        testUser.setEmail("desq@mailinator.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
	    HibernateUtil.commitTransaction();
        
	    String stringObject = "{Username: "+testUser.getUsername()+", Email: "+testUser.getEmail()+", Password: "+testUser.getPassword()+", IdUser: "+testUser.getIdUser()+", IsAdmin: "+testUser.getIsAdmin()+", Name: 'test', Surname: 'test', Password2: 'test'}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
		ctrlSharedObject.ctrlModifyAccount(request, response);
    }
    
    /**
	 *	Test del metodo ctrlRecoveryPassword() che permette all'utente di recuperare la password nel caso l'avesse dimenticata.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlRecoveryPassword() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlShared ctrlSharedObject = new CtrlShared();

        //creazione ambiente per l'esecuzione della query
	    HibernateUtil.getSession().beginTransaction();
	    
        //creazione utente
        User testUser = new User();
        testUser.setEmail("desq@mailinator.com");
        testUser.setIsAdmin(false);
        testUser.setPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
	    HibernateUtil.commitTransaction();
        
	    String stringObject = "{Username: "+testUser.getUsername()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
		ctrlSharedObject.ctrlRecoveryPassword(request, response);
    }
    
    /**
	 *	Test del metodo ctrlLogout() che permette ad un utente di terminare la sessione.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlLogout() throws Exception {
    	HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlShared ctrlSharedObject = new CtrlShared();

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
        
	    String stringObject = "{Username: "+testUser.getUsername()+", Password: "+testUser.getPassword()+", IdUser: "+testUser.getIdUser()+", IsAdmin: "+testUser.getIsAdmin()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
		ctrlSharedObject.ctrlLogout(request, response);
    }
    
	/**
	 *	Test del metodo ctrlLogin() che permette ad un utente di autenticarsi.
	 * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlLogin() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        CtrlShared ctrlSharedObject = new CtrlShared();

        //creazione ambiente per l'esecuzione della query
	    HibernateUtil.getSession().beginTransaction();
	    
        //creazione utente
        User testUser = new User();
        testUser.setEmail("asd@asd.com");
        testUser.setIsAdmin(false);
        testUser.hashAndSetPassword("test");
        testUser.setUsername("test");
        HibernateUtil.getSession().saveOrUpdate(testUser);
	    HibernateUtil.commitTransaction();
	    
        String stringObject = "{Username: "+testUser.getUsername()+", Password: 'test'}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlSharedObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlSharedObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		//utente non amministratore e dati corretti
		ctrlSharedObject.ctrlLogin(request, response);
		
		HibernateUtil.getSession().beginTransaction();
	    testUser.setIsAdmin(true);
	    HibernateUtil.getSession().saveOrUpdate(testUser);
	    HibernateUtil.commitTransaction();
	    
	    
	    stringObject = "{Username: "+testUser.getUsername()+", Password: 'test'}";
    	stringReaderObject=new StringReader(stringObject);
    	bufferReaderObject = new BufferedReader(stringReaderObject);
    	try {
			when(request.getReader()).thenReturn(bufferReaderObject);
		}
    	catch (IOException e) {}
    	
		//utente amministratore e dati corretti
	    ctrlSharedObject.ctrlLogin(request, response);
	    
		stringObject = "{Username: 'username', Password: 'password'}";
    	stringReaderObject=new StringReader(stringObject);
    	bufferReaderObject = new BufferedReader(stringReaderObject);
    	try {
			when(request.getReader()).thenReturn(bufferReaderObject);
		}
    	catch (IOException e) {}
    	
    	//utente amministratore e dati errati
    	ctrlSharedObject.ctrlLogin(request, response);
		
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    
    /**
	 *	Test del metodo ctrlViewAccount() che permette all'utente di visualizzare i dati del proprio account.
     * @throws Exception segnala un problema di connessione al database
	 */
    @Test
    public void testCtrlViewAccount() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session= mock(HttpSession.class);
        
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
        
        CtrlShared ctrlSharedObject = new CtrlShared();
        
        String stringObject = "{IdUser: "+testUser.getIdUser()+"}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			when(request.getSession()).thenReturn(session);
			
			when(session.getAttribute(testUser.getIdUser().toString())).thenReturn(true);
			
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlSharedObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlSharedObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlSharedObject.ctrlViewAccount(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
    

    /**
	 *	Test del metodo ctrlUserRegistration() che permette ad un nuovo utente di registrarsi.
	 */
    @Test
    public void testCtrlUserRegistration() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        CtrlShared ctrlSharedObject = new CtrlShared();
        
        String stringObject = "{Username: 'testUsername', Password: 'testPassword', "
        		+ "Name: 'testName', Surname: 'testSurname', Email: 'testEmail', "
        		+ "City: 'testCity', District: 'Di', IsAdmin: true}";
    	StringReader stringReaderObject=new StringReader(stringObject);
    	BufferedReader bufferReaderObject = new BufferedReader(stringReaderObject);
        
    	Writer writerMock = mock(Writer.class);
    	PrintWriter writer = null;
        try {
			when(request.getReader()).thenReturn(bufferReaderObject);
			writer = new PrintWriter(writerMock);
			when(response.getWriter()).thenReturn(writer);
		}
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        
        Field iParserObject = null;
		Field iServiceObject = null;
		
		try {
			iParserObject = ctrlSharedObject.getClass().getDeclaredField("iParser");
			iParserObject.setAccessible(true);
			iServiceObject = ctrlSharedObject.getClass().getDeclaredField("iService");
			iServiceObject.setAccessible(true);
		}
		catch (NoSuchFieldException e) {}
		catch (SecurityException e) {}
		
		ctrlSharedObject.ctrlUserRegistration(request, response);
		assertNotNull(iParserObject);
		assertNotNull(iServiceObject);
    }
}
