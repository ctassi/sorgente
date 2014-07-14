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
 * 	File contentente la classe CtrlUser
 * 
 *	@file		CtrlUser.java
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.service.interfaceservice.IService;
import com.sequenziatore.server.service.user.*;

/**
 *	La classe CtrlUser svolge il ruolo di controller per le operazioni disponibili ai normali utenti.
 *
 *	@author 	DeSQ
 */
@Controller
@RequestMapping("/user")
public class CtrlUser {

	/** Rappresenta il tipo di parser utilizzato in base all'operazione da svolgere. */
	private IParser iParser;
	
	/** Rappresenta il tipo di service utilizzata in base all'operazione da svolgere. */ 
	private IService iService;
	
	/**
	 * Permette ad un utente di iscriversi ad un processo.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/subscribetoprocess", method = RequestMethod.POST)
	public void ctrlSubscribeToProcess(HttpServletRequest request, HttpServletResponse response) {
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
		Process process = new Process(json);
		entities.add(process);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(false)){
					iService=new ServiceSubscribeToProcess();
					JSONObject jsonResponse = iService.serviceOperation(entities);
					out = response.getWriter();
				    response.setContentType("text/html");
				    out.write(jsonResponse.toString());
					out.close();
				}
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di visualizzare i processi a cui può iscriversi.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/availableprocess", method = RequestMethod.POST)
	public void ctrlAvailableProcess(HttpServletRequest request, HttpServletResponse response) {
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
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(false)){
					iService=new ServiceAvailableProcesses();
					JSONObject jsonResponse = iService.serviceOperation(entities);
					out = response.getWriter();
				    response.setContentType("text/html");
				    out.write(jsonResponse.toString());
					out.close();
				}
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di visualizzare i dati di un passo da svolgere.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/viewstep", method = RequestMethod.POST)
	public void ctrlViewStep(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		List<IEntity> entities = new ArrayList<IEntity>();
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		User user = new User(json);
		entities.add(user);
		Process process = new Process(json);
		entities.add(process);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(false)){
					iService=new ServiceViewStep();
					JSONObject jsonResponse = iService.serviceOperation(entities);
					out = response.getWriter();
				    response.setContentType("text/html");
				    out.write(jsonResponse.toString());
					out.close();
				}
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di visualizzare la lista di processi attivi a cui è iscritto.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/viewactiveprocesses", method = RequestMethod.POST)
	public void ctrlViewActiveProcesses(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		iParser=new ParseJSON();
		List<IEntity> entities = new ArrayList<IEntity>();
		JSONObject json=iParser.parse(request);
		User user = new User(json);
		entities.add(user);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(false)){
					iService=new ServiceViewActiveProcesses();
					JSONObject jsonResponse = iService.serviceOperation(entities);
					out = response.getWriter();
				    response.setContentType("text/html");
				    out.write(jsonResponse.toString());
					out.close();
				}
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di salvare una raccolta dati per completare un passo.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/savedatastep", method = RequestMethod.POST)
	public void ctrlSaveDataStep(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		List<IEntity> entities = new ArrayList<IEntity>();
		if(request.getContentType().contains("application/json"))
			iParser=new ParseJSON();
		else 
			iParser=new ParseMultipartFormData();
		JSONObject json=iParser.parse(request);
		DataCollection data = new DataCollection(json);
		entities.add(data);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(false)){
					iService=new ServiceSaveDataStep();
					JSONObject jsonResponse = iService.serviceOperation(entities);
					out = response.getWriter();
				    response.setContentType("text/html");
				    out.write(jsonResponse.toString());
					out.close();
				}
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette la visualizzazione delle statistiche riguardanti l'utente che ne fa richiesta.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/statisticsuser", method = RequestMethod.POST)
	public void ctrlStatisticsUser(HttpServletRequest request, HttpServletResponse response) {
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
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(false)){
					iService=new ServiceStatisticsUser();
					JSONObject jsonResponse = iService.serviceOperation(entities);
					out = response.getWriter();
				    response.setContentType("text/html");
				    out.write(jsonResponse.toString());
					out.close();
				}
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette di cancellare l'iscrizione ad un processo.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/unsubscribeprocess", method = RequestMethod.POST)
	public void ctrlUnsubscribeProcess(HttpServletRequest request, HttpServletResponse response) {
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
		Process process = new Process(json);
		entities.add(process);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(false)){
					iService=new ServiceUnsubscribeProcess();
					JSONObject jsonResponse = iService.serviceOperation(entities);
					out = response.getWriter();
				    response.setContentType("text/html");
				    out.write(jsonResponse.toString());
					out.close();
				}
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
	
	/**
	 * Permette ad un utente di visualizzare la lista di processi non attivi a cui è iscritto.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/viewprocessnotactive", method = RequestMethod.POST)
	public void ctrlViewProcessNotActive(HttpServletRequest request, HttpServletResponse response) {
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
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(false)){
					iService=new ServiceViewProcessNotActive();
					JSONObject jsonResponse = iService.serviceOperation(entities);
					out = response.getWriter();
				    response.setContentType("text/html");
				    out.write(jsonResponse.toString());
					out.close();
				}
			}
		}
		catch (NullPointerException e) {}
		catch (IOException e) {}
	}
}