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
 * 	File contentente la classe ServiceReport
 * 
 *	@file		ServiceReport.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.User;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceReport offre il servizio di report di un processo.
 *
 *	@author 	DeSQ
 */
public class ServiceReport implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette ad un utente di ottenere il report di un processo grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id del processo di cui è richiesto il report
	 * @return le informazioni del report del processo
	 */
	
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		JSONArray arrayStep= new JSONArray();
		iDAOFactory = new DAOFactory();
		Process process=(Process)entity.get(0);
		Process processStepList= null;
		List<User> allSubscription;
		List<Integer> subscriptionComplete;
		List<Integer> step;
		try {
		    HibernateUtil.getSession().beginTransaction();
		    allSubscription=iDAOFactory.createDAOManagementProcessAdmin().findUserSubscription(process);
		    processStepList=iDAOFactory.createDAOManagementProcessAdmin().findProcess(process);
		    HibernateUtil.commitTransaction();
		} catch(Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
	    if(allSubscription.size() != 0){
	    	try {
			    HibernateUtil.getSession().beginTransaction();
			    subscriptionComplete=iDAOFactory.createDAOManagementProcessAdmin().findUserSubscriptionComplete(process);
			    for(int i=0;i<processStepList.getSteps().size();i++){
			    	step=iDAOFactory.createDAOManagementProcessAdmin().findUserStepComplete(processStepList.getSteps().get(i));
			    	JSONObject objectJsonStep=new JSONObject();
			    	objectJsonStep.put("Step", processStepList.getSteps().get(i).getDescription());
			    	objectJsonStep.put("StepComplete", step.size());
			    	objectJsonStep.put("StepNotComplete", allSubscription.size()-step.size());
			    	objectJsonStep.put("StepLevel", processStepList.getSteps().get(i).getLevel());
			    	arrayStep.put(objectJsonStep);
			    }
			    objectJson.put("Level", this.report(allSubscription, subscriptionComplete,processStepList));
			    HibernateUtil.commitTransaction();
	        } catch(Exception e) {
	    		return objectJson.put("Confirmation", "connectionError");
	    	}
		    objectJson.put("Process", processStepList.getName());
	    	objectJson.put("Step", arrayStep);
		    
		    objectJson.put("AllSubscription", allSubscription.size());
		    objectJson.put("SubscriptionComplete", subscriptionComplete.size());
		    objectJson.put("SubscriptionNotComplete", allSubscription.size()-subscriptionComplete.size());
		    objectJson.put("Confirmation","SomebodyIsSubscribed");
	    }else{
	    	objectJson.put("Confirmation", "NoOneIsSubscribed");
	    	objectJson.put("ProcessName", processStepList.getName());
	    }
	    return objectJson;
	}
	
	/**
	 * Permette di ricavare il report dei livelli.
	 * 
	 * @param allSubscription contiene gli utenti che si sono iscritti al processo
	 * @param subscriptionComplete contiene gli id degli utenti che hanno completato il processo
	 * @param process contiene i dati del processo
	 * @return un JSONArray i report dei livelli
	 * @throws Exception segnala un problema di connessione al database
	 */
	private JSONArray report(List<User> allSubscription, List<Integer> subscriptionComplete, Process process) throws Exception{
		List<Integer> levelUser= new ArrayList<Integer>();
		List<Integer> level= new ArrayList<Integer>();
		JSONArray arrayLevel= new JSONArray();
		for(int i=0;i<allSubscription.size();i++){
	    	Integer minLevel = iDAOFactory.createDAOManagementProcessAdmin().findMinStep(allSubscription.get(i),process);
	    	if(minLevel != null)
	    		levelUser.add(minLevel-1);
	    }
	    level.add(subscriptionComplete.size());
	    for(int i=process.getTotalLevel()-1 ; i>0 ; i--){
	    	level.add(Collections.frequency(levelUser, i-1)+level.get(i-1));
	    }
	    for(int i=1 ; i<=level.size() ; i++){
	    	JSONObject objectJsonLevel=new JSONObject();
	    	objectJsonLevel.put("Level", i);
	    	objectJsonLevel.put("LevelComplete", level.get(level.size()-i));
	    	objectJsonLevel.put("LevelNotComplete", allSubscription.size()-level.get(level.size()-i));
	    	arrayLevel.put(objectJsonLevel);
	    }
		return arrayLevel;		
	}
}
 
