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
 * 	File contentente la classe ServiceSaveDataStep
 * 
 *	@file		ServiceSaveDataStep.java
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
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.DataCollection.EnumState;
import com.sequenziatore.server.entity.Step.EnumParallelism;

import org.json.JSONObject;

import com.sequenziatore.server.service.interfaceservice.IService;

/**
 *	La classe ServiceSaveDataStep offre il servizio di salvataggio dei dati raccolti dall'utente.
 *
 *	@author 	DeSQ
 */
public class ServiceSaveDataStep implements IService {
 
	/** Rappresenta l'interfaccia che garantisce l'accesso alle classi DAO per accedere al database. */
	private IDAOFactory iDAOFactory;
	
	/**
	 * Permette ad un utente di salvare i dati raccolti grazie alle informazioni acquisite dal front-end.
	 * 
	 * @param entity contiene le informazioni necessarie per il completamento del passo
	 * @return l'esito dell'operazione di salvataggio dei dati
	 */
	@Override
	public JSONObject serviceOperation(List<IEntity> entity) {
		iDAOFactory = new DAOFactory();
		Step stepQuery = null;
		DataCollection data=(DataCollection)entity.get(0);
		JSONObject objectJson=new JSONObject();
		DataCollection dataQuery = null;
		
		try {
		HibernateUtil.getSession().beginTransaction();
		dataQuery = iDAOFactory.createDAOManagementProcessUser().findDataCollection(data.getIdUser(),data.getIdStep());
		HibernateUtil.commitTransaction();
		} catch (Exception e) {
			return objectJson.put("Confirmation", "connectionError");
		}
		
		if(dataQuery.getState().equals(EnumState.TOVERIFY) || dataQuery.getState().equals(EnumState.VERIFIED)){
	    	objectJson.put("Confirmation", "notSendData");
		} else {
			try {
				HibernateUtil.getSession().beginTransaction();
				stepQuery = iDAOFactory.createDAOManagementProcessUser().findStep(data.getIdStep());
				if(stepQuery.getIdProcess().getAvailable().booleanValue()==false){
					HibernateUtil.commitTransaction();
					return objectJson.put("Confirmation", "processNotActive");
				}
				if(stepQuery.getAdminVerify()==true){
					data.setState(EnumState.TOVERIFY);
					objectJson.put("Confirmation", "successSendData");
				} else if(stepQuery.getCheckText().equalsIgnoreCase("null") == true){
					data.setState(EnumState.VERIFIED);
					this.lastStep(dataQuery);
					objectJson.put("Confirmation", "successSendData");
				} else if(stepQuery.getCheckText().equalsIgnoreCase(data.getText())==true){
						data.setState(EnumState.VERIFIED);
						this.lastStep(dataQuery);
						objectJson.put("Confirmation", "successAutoVerify");
				} else{
						data.setState(EnumState.FAILED);
						data.setWrongPhoto(true);
						data.setWrongGeolocation(true);
						objectJson.put("Confirmation", "notSuccessAutoVerify");
				}
				data.setIdCollection(dataQuery.getIdCollection());
				iDAOFactory.createDAOManagementProcessUser().insertDataCollection(data);
				HibernateUtil.commitTransaction();
			} catch (Exception e) {
				return objectJson.put("Confirmation", "connectionError");
			}
		}
	    return objectJson;
	}
	
	/**
	 * Permette di verificare se è l'ultimo passo di un processo.
	 * 
	 * @param dataCollection contiene i dati di un passo inviati dall'utente
	 * @throws Exception segnala un problema di connessione al database 
	 */
	private void lastStep(DataCollection dataCollection) throws Exception {
		if(dataCollection.getIdStep().getParallelism().equals(EnumParallelism.OR)){
			this.lastStepOr(dataCollection);
		}else if(dataCollection.getIdStep().getParallelism().equals(EnumParallelism.AND)){
			this.lastStepAnd(dataCollection);
		}else lastStepNot(dataCollection);
	}
	
	/**
	 * Permette di verificare se è l'ultimo passo di un processo in un livello in OR.
	 * 
	 * @param dataCollection contiene i dati di un passo inviati dall'utente
	 * @throws Exception segnala un problema di connessione al database 
	 */
	private void lastStepOr(DataCollection dataCollection) throws Exception {
		List<DataCollection> datacollections=null;
		Subscription subsrcription=null;
		Integer lvlMax=new Integer(dataCollection.getIdStep().getIdProcess().getTotalLevel());
		datacollections = iDAOFactory.createDAOManagementProcessUser().findDataCollectionByLevel(dataCollection);
		for(int i=0;i<datacollections.size();i++){
			datacollections.get(i).setState(EnumState.SKIPPED);
			iDAOFactory.createDAOManagementProcessUser().insertDataCollection(datacollections.get(i));
		}
		if(dataCollection.getIdStep().getLevel().compareTo(lvlMax-1) == 0){
			subsrcription = iDAOFactory.createDAOManagementProcessUser().findSubscription(dataCollection.getIdUser(), dataCollection.getIdStep().getIdProcess());
			subsrcription.setComplete(true);
			iDAOFactory.createDAOManagementProcessUser().insertSubscription(subsrcription);
		}
	}
	
	/**
	 * Permette di verificare se è l'ultimo passo di un processo in un livello in AND.
	 * 
	 * @param dataCollection contiene i dati di un passo inviati dall'utente
	 * @throws Exception segnala un problema di connessione al database 
	 */
	private void lastStepAnd(DataCollection dataCollection) throws Exception {
		Subscription subsrcription=null;
		List<DataCollection> datacollections=null;
		boolean verified=true;
		Integer lvlMax=new Integer(dataCollection.getIdStep().getIdProcess().getTotalLevel());
		if(dataCollection.getIdStep().getLevel().compareTo(lvlMax-1) == 0){
			datacollections = iDAOFactory.createDAOManagementProcessUser().findDataCollectionByLevel(dataCollection);
			for(int i=0; i<datacollections.size() && verified; i++){
				if(!(datacollections.get(i).getState().equals(EnumState.VERIFIED)))
					verified=false;
			}
			if(verified == true){
				subsrcription = iDAOFactory.createDAOManagementProcessUser().findSubscription(dataCollection.getIdUser(), dataCollection.getIdStep().getIdProcess());
				subsrcription.setComplete(true);
				iDAOFactory.createDAOManagementProcessUser().insertSubscription(subsrcription);
			}
		}
	}
	
	/**
	 * Permette di verificare se è l'ultimo passo di un processo in un livello con un solo passo.
	 * 
	 * @param dataCollection contiene i dati di un passo inviati dall'utente
	 * @throws Exception segnala un problema di connessione al database 
	 */
	private void lastStepNot(DataCollection dataCollection) throws Exception{
		Integer lvlMax=new Integer(dataCollection.getIdStep().getIdProcess().getTotalLevel());
		Subscription subsrcription=null;
		if(dataCollection.getIdStep().getLevel().compareTo(lvlMax-1) == 0){
			subsrcription = iDAOFactory.createDAOManagementProcessUser().findSubscription(dataCollection.getIdUser(), dataCollection.getIdStep().getIdProcess());
			subsrcription.setComplete(true);
			iDAOFactory.createDAOManagementProcessUser().insertSubscription(subsrcription);
		}
	}
}
 
