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
 * 	File contentente la classe ServiceStepListValidation
 * 
 *	@file		ServiceStepListValidation.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.admin;

import java.util.List;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.User;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceStepListValidation offre il servizio di visualizzazione delle raccolte dati da validare.
 *
 *	@author 	DeSQ
 */
public class ServiceStepListValidation implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette la ricerca delle raccolte dati da validare grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id dell'amministratore che fa la richiesta
	 * @return i dati delle collezioni da validare
	 */
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		User user=(User)entity.get(0);
		iDAOFactory = new DAOFactory();
		try {
		    HibernateUtil.getSession().beginTransaction();
		    objectJson.put("Collections", this.order(user));
		    objectJson.put("Confirmation", "steps");
		    HibernateUtil.commitTransaction();
		} catch(Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
	    return objectJson;
	}
	
	/**
	 * Permette di ordinare la lista degli step in base: idProcesso e livello.
	 * 
	 * @param user contiene l'id dell'amministratore che fa la richiesta
	 * @return un JSONArray contenente gli step da validare ordinati
	 * @throws Exception segnala un problema di connessione al database
	 */
	private JSONArray order(User user) throws Exception{
		JSONArray arrayProcess= new JSONArray();
		List<Process> listProcess = iDAOFactory.createDAOManagementProcessAdmin().findDataCollectionsProcess(user);
		for(int i=0;i<listProcess.size();i++){
			JSONArray arrayLevel= new JSONArray();
			JSONObject process=new JSONObject();
			process.put("Name", listProcess.get(i).getName());
			process.put("List", arrayLevel);
			arrayProcess.put(process);
			List<Integer> listLevels =  iDAOFactory.createDAOManagementProcessAdmin().findDataCollectionsLevel(user, listProcess.get(i));
			for(int j=0;j<listLevels.size();j++){
				JSONObject level=new JSONObject();
				level.put("Name", listLevels.get(j));
				level.put("List", this.orderStep(user, listProcess.get(i), listLevels.get(j)));
				arrayLevel.put(level);
			}
		}
		return arrayProcess;
	}
	/**
	 * Permette di ordinare la lista degli step.
	 * 
	 * @param user contiene l'id dell'amministratore che fa la richiesta
	 * @param process contiene l'id del processo
	 * @param level contiene il livello del processo
	 * @return un JSONArray contenente gli step da validare ordinati
	 * @throws Exception segnala un problema di connessione al database
	 */
	private JSONArray orderStep(User user, Process process, Integer level) throws Exception{
		List<Step> listIdStep = iDAOFactory.createDAOManagementProcessAdmin().findDataCollectionsStep(user, process, level);
		JSONArray arrayStep= new JSONArray();
		for(int k=0;k<listIdStep.size();k++){	
			JSONArray arrayDataCollection= new JSONArray();
			JSONObject step=new JSONObject();
			step.put("Name",listIdStep.get(k).getDescription());
			step.put("List", arrayDataCollection);
			arrayStep.put(step);
			List<DataCollection> listDataCollections = iDAOFactory.createDAOManagementProcessAdmin().findDataCollectionIdStep(user, process, level, listIdStep.get(k).getIdStep());
			for(int z=0; z < listDataCollections.size(); z++){
				JSONObject objectJsonCollections=listDataCollections.get(z).toJSONObject();
				objectJsonCollections.put("Description",listDataCollections.get(z).getIdStep().getDescription());
				objectJsonCollections.put("CheckText",listDataCollections.get(z).getIdStep().getCheckText());
				objectJsonCollections.put("CheckLatitude",listDataCollections.get(z).getIdStep().getCheckLatitude());
				objectJsonCollections.put("CheckLongitude",listDataCollections.get(z).getIdStep().getCheckLongitude());
				objectJsonCollections.put("Level",listDataCollections.get(z).getIdStep().getLevel());
				objectJsonCollections.put("IdProcess",listDataCollections.get(z).getIdStep().getIdProcess().getIdProcess());
				objectJsonCollections.put("Name",listDataCollections.get(z).getIdStep().getIdProcess().getName());
				arrayDataCollection.put(objectJsonCollections);
			}
		}
		return arrayStep;
	}
	 
}
 
