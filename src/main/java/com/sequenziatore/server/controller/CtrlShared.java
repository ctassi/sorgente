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
 * 	File contentente la classe CtrlShared
 * 
 *	@file		CtrlShared.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.service.interfaceservice.IService;
import com.sequenziatore.server.service.shared.*;

/**
 *	La classe CtrlShared svolge il ruolo di controller per le operazioni disponibili sia ai normali utenti che agli amministratori.
 *
 *	@author 	DeSQ
 */
@Controller
@RequestMapping("/shared")
public class CtrlShared {
 
	/** Rappresenta il tipo di parser utilizzato in base all'operazione da svolgere. */
	private IParser iParser;
	
	/** Rappresenta il tipo di service utilizzata in base all'operazione da svolgere. */ 
	private IService iService;
	
	/**
	 * Permette ad un utente di autenticarsi.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/login", method = RequestMethod.POST)
	public void ctrlLogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		List<IEntity> entities=new ArrayList<IEntity>();
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		iService=new ServiceLogin();
		User user = new User(json);
		entities.add(user);
		JSONObject jsonResponse = iService.serviceOperation(entities);
		try {
			out = response.getWriter();
			if(jsonResponse.get("Confirmation").equals("successAuthentication")){
				request.getSession().setAttribute(jsonResponse.get("IdUser").toString(),true);
				if(jsonResponse.getBoolean("IsAdmin")){
					request.getSession().setAttribute(jsonResponse.get("IdUser").toString()+"Admin",true);
				}
				else{
					request.getSession().setAttribute(jsonResponse.get("IdUser").toString()+"Admin",false);
				}	
			}
		    response.setContentType("text/html");
		    out.write(jsonResponse.toString());
			out.close();
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di visualizzare i dati del proprio account.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/viewaccount", method = RequestMethod.POST)
	public void ctrlViewAccount(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		List<IEntity> entities=new ArrayList<IEntity>();
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		User user = new User(json);
		entities.add(user);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				iService=new ServiceViewAccount();
				JSONObject jsonResponse = iService.serviceOperation(entities);
				out = response.getWriter();
			    response.setContentType("text/html");
			    out.write(jsonResponse.toString());
				out.close();
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di modificare i dati del proprio account.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/modifyaccount", method = RequestMethod.POST)
	public void ctrlModifyAccount(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		iParser=new ParseJSON();
		List<IEntity> entities=new ArrayList<IEntity>();
		JSONObject json=iParser.parse(request);
		User user = new User(json);
		entities.add(user);
		User userPassword= new User();
		if(json.has("Password2")){
			userPassword.hashAndSetPassword((String)json.get("Password2"));
			entities.add(userPassword);
		}
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				iService=new ServiceModifyAccount();
				JSONObject jsonResponse = iService.serviceOperation(entities);
				out = response.getWriter();
				response.setContentType("text/html");
				out.write(jsonResponse.toString());
				out.close();
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di resettare la propria password nel caso l'avesse dimenticata.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/recoverypassword", method = RequestMethod.POST)
	public void ctrlRecoveryPassword(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		iParser=new ParseJSON();
		List<IEntity> entities=new ArrayList<IEntity>();
		JSONObject json=iParser.parse(request);
		User user = new User(json);
		entities.add(user);
		iService=new ServiceRecoveryPassword();
		JSONObject jsonResponse = iService.serviceOperation(entities);
		try {
			out = response.getWriter();
		    response.setContentType("text/html");
		    out.write(jsonResponse.toString());
			out.close();
		}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di terminare la sessione.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/logout", method = RequestMethod.POST)
	public void ctrlLogout(HttpServletRequest request, HttpServletResponse response) {
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		String userToLogout = json.get("IdUser").toString();
		try {
			request.getSession().removeAttribute(userToLogout);
			request.getSession().removeAttribute(userToLogout+"Admin");
		}
		catch (NullPointerException e) {}
	}
	
	/**
	 * Permette ad un nuovo utente di registrarsi.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/userregistration", method = RequestMethod.POST)
	public void ctrlUserRegistration(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		iParser=new ParseJSON();
		List<IEntity> entities=new ArrayList<IEntity>();
		JSONObject json=iParser.parse(request);
		User user = new User(json);
		entities.add(user);
		iService=new ServiceUserRegistration();
		JSONObject jsonResponse = iService.serviceOperation(entities);
		try {
			out = response.getWriter();
			response.setContentType("text/html");
			out.write(jsonResponse.toString());
			out.close();
		}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di registrarsi alla websocket.
	 * 
	 * @param message messaggio di notifica contenente i dati da mostrare
	 * @return messaggio di notifica contenente i dati da mostrare
	 */
	@MessageMapping(value="/pushservice")
	@SendTo(value="/notificationlistener")
	private Notification loginWebsocket(Notification message){
		return message;
	}
	
	/**
	 * Permette ad un utente o admin di autenticarsi tramite Facebook.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/facebooklogin", method = RequestMethod.POST)
	public void ctrlUserFacebookLogin(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		List<IEntity> entities=new ArrayList<IEntity>();
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		iService=new ServiceFacebookLogin();
		User user = new User(json);
		entities.add(user);
		JSONObject jsonResponse = iService.serviceOperation(entities);		
		try {
			out = response.getWriter();
			if(jsonResponse.get("Confirmation").equals("successAuthentication")){
				request.getSession().setAttribute(jsonResponse.get("IdUser").toString(),true);
				if(jsonResponse.getBoolean("IsAdmin")){
					request.getSession().setAttribute(jsonResponse.get("IdUser").toString()+"Admin",true);
				}
				else{
					request.getSession().setAttribute(jsonResponse.get("IdUser").toString()+"Admin",false);
				}	
			}
		    response.setContentType("text/html");
		    out.write(jsonResponse.toString());
			out.close();
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
}
 
