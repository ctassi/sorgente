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
 * 	File contentente la classe ServiceViewStep
 * 
 *	@file		ServiceViewStep.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.service.user;

import java.util.List;

import com.sequenziatore.server.databaseutil.dao.DAOFactory;
import com.sequenziatore.server.databaseutil.dao.IDAOFactory;
import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.IEntity;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.User;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceViewStep offre il servizio di visualizzazione di un passo da compiere.
 *
 *	@author 	DeSQ
 */
public class ServiceViewStep implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette ad un utente di visualizzare le data collection da completare e i dati dei corrispondenti passi grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene l'id del processo che contiene il passo e l'id dell'utente
	 * @return tutte le informazioni delle data collection
	 */
	
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		JSONObject objectJson=new JSONObject();
		JSONArray steps= new JSONArray();
		User user=(User)entity.get(0);
		Integer minLevel=null;
		Process process=(Process)entity.get(1);
		iDAOFactory = new DAOFactory();
		List<DataCollection> data=null;
		
		try {
		    HibernateUtil.getSession().beginTransaction();
		    minLevel = iDAOFactory.createDAOManagementProcessUser().findMinStep(user, process);
		    data = iDAOFactory.createDAOManagementProcessUser().findStep(user, process, minLevel);
		    process=iDAOFactory.createDAOManagementProcessUser().findProcessById(process);
		    HibernateUtil.commitTransaction();
		} catch(Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
		
	    for(int i=0;i<data.size();i++){
	    	JSONObject objectJsonStep=data.get(i).getIdStep().toJSONObject();
	    	objectJsonStep.put("Photo", data.get(i).getPhoto());
	    	objectJsonStep.put("Text", data.get(i).getText());
	    	objectJsonStep.put("Latitude", data.get(i).getLatitude());
	    	objectJsonStep.put("Longitude", data.get(i).getLongitude());
	    	objectJsonStep.put("State", data.get(i).getState());
	    	objectJsonStep.put("WrongText", data.get(i).getWrongText());
	    	objectJsonStep.put("WrongGeolocation", data.get(i).getWrongGeolocation());
	    	objectJsonStep.put("WrongPhoto", data.get(i).getWrongPhoto());
	    	steps.put(objectJsonStep);
	    }
	    objectJson.put("Confirmation", "step");
	    objectJson.put("ProcessName", process.getName());
	    objectJson.put("StepList", steps);
		return objectJson;
	}
	 
}
 
