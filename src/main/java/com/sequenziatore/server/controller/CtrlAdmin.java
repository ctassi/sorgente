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
 * 	File contentente la classe CtrlAdmin
 * 
 *	@file		CtrlAdmin.java
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.User;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.service.admin.*;
import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe CtrlAdmin svolge il ruolo di controller per le operazioni disponibili agli amministratori.
 *
 *	@author 	DeSQ
 */
@Controller
@RequestMapping("/admin")
public class CtrlAdmin {
	
	/** Rappresenta il tipo di parser utilizzato in base all'operazione da svolgere. */
	private IParser iParser;
	
	/** Rappresenta il tipo di service utilizzata in base all'operazione da svolgere. */ 
	private IService iService;
	
	/**
	 * Permette di visualizzare i dati di un processo.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/viewprocess", method = RequestMethod.POST)
	public void ctrlViewProcess(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		List<IEntity> entities=new ArrayList<IEntity>();
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		Process process = new Process(json);
		entities.add(process);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService = new ServiceViewProcess();
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
	 * Permette la modifica di un processo.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/modifyprocess", method = RequestMethod.POST)
	public void ctrlModifyProcess(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		JSONArray array=null;
		List<Step> steps = new ArrayList<Step>();
		List<IEntity> entities=new ArrayList<IEntity>();
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		Process process = new Process(json);
		entities.add(process);
		array=(JSONArray)json.get("StepList");
		for(int i=0;i<array.length();i++){
			Step step=new Step((JSONObject)array.get(i));
			step.setIdProcess(process);
			steps.add(step);
		}
		process.setSteps(steps);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceModifyProcess();
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
	 * Permette la visualizzazione della lista degli utenti che partecipano ai processi creati dall'amministratore che fa la richiesta.
	 *  
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/listuserprocess", method = RequestMethod.POST)
	public void ctrlListUserProcess(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		List<IEntity> entities=new ArrayList<IEntity>();
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		Process process = new Process(json);
		entities.add(process);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceListUserProcess();
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
	 * Permette la visualizzazione di tutti i processi attivi dell'amministratore che fa la richiesta.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/viewadminprocessactive", method = RequestMethod.POST)
	public void ctrlViewAdminProcessActive(HttpServletRequest request, HttpServletResponse response) {
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
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceViewAdminProcessActive();
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
	 * Permette di visualizzare la lista di tutte le raccolte dati ancora da convalidare.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/steplistvalidation", method = RequestMethod.POST)
	public void ctrlStepListValidation(HttpServletRequest request, HttpServletResponse response) {
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
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceStepListValidation();
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
	 * Premette la convalida o l'annullamento di una raccolta dati.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/stepvalidation", method = RequestMethod.POST)
	public void ctrlStepValidation(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		List<IEntity> entities=new ArrayList<IEntity>();
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		DataCollection data = new DataCollection(json);
		entities.add(data);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceStepValidation();
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
	 * Permette la visualizzazione di tutti i processi presenti nel sistema.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/viewallprocesses", method = RequestMethod.POST)
	public void ctrlViewAllProcesses(HttpServletRequest request, HttpServletResponse response) {
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
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceViewAllProcesses();
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
	 * Permette la creazione di un processo con i relativi passi.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 * @param message il messaggio da visualizzare nel front-end
	 * @return il messaggio da visualizzare nel front-end
	 */
	@RequestMapping(value= "/createprocess", method = RequestMethod.POST)
	public Notification ctrlCreateProcess(HttpServletRequest request, HttpServletResponse response, Notification message) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		JSONArray array=null;
		List<Step> steps = new ArrayList<Step>();
		List<IEntity> entities=new ArrayList<IEntity>();
		iParser=new ParseJSON();
		JSONObject json=iParser.parse(request);
		Process process = new Process(json);
		entities.add(process);
		array=(JSONArray)json.get("Steps");
		for(int i=0;i<array.length();i++){
			Step step=new Step((JSONObject)array.get(i));
			step.setIdProcess(process);
			steps.add(step);
		}
		process.setSteps(steps);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceCreateProcess();
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

		return message;
	}
	
	
	/**
	 * Permette la visualizzazione di un report di un processo.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/report", method = RequestMethod.POST)
	public void ctrlReport(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		PrintWriter out = null;
		iParser=new ParseJSON();
		List<IEntity> entities=new ArrayList<IEntity>();
		JSONObject json=iParser.parse(request);
		Process process = new Process(json);
		entities.add(process);
		try {
			if(request.getSession().getAttribute(json.get("IdUser").toString()).equals(true)){
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceReport();
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
	 * Permette la visualizzazione delle statistiche riguardanti l'admin che ne fa richiesta.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/statisticsadmin", method = RequestMethod.POST)
	public void ctrlStatisticsAdmin(HttpServletRequest request, HttpServletResponse response) {
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
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceStatisticsAdmin();
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
	 * Permette la visualizzazione di tutti i processi non attivi dell'amministratore che fa la richiesta.
	 * 
	 * @param request la richiesta di informazioni che arriva dal front-end
	 * @param response la risposta che verrà inviata al front-end con le informazioni richieste
	 */
	@RequestMapping(value= "/viewadminprocessnotactive", method = RequestMethod.POST)
	public void ctrlViewAdminProcessNotActive(HttpServletRequest request, HttpServletResponse response) {
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
				if(request.getSession().getAttribute(json.get("IdUser").toString()+"Admin").equals(true)){
					iService=new ServiceViewAdminProcessNotActive();
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
	
	
};