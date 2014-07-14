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
 * 	File contentente la classe Subscription
 * 
 *	@file		Subscription.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.json.JSONObject;

/**
 *	La classe Subscription rappresenta logicamente l'iscrizione di un utente ad un processo nell'applicazione.
 *
 *	@author 	DeSQ
 */
@Entity
@Table(name = "Subscriptions")
public class Subscription implements Serializable, IEntity{

	private static final long serialVersionUID = 1L;

	/** Id dell'iscrizione. */
	@Id
	@GeneratedValue
	@Column(name = "IdSubscription", nullable=false)
	private Integer idSubscription;
	
	/** Utente che si è iscritto al processo. */
	@ManyToOne
  	@JoinColumn(name = "IdUser")
	private User idUser;

	/** Id del processo a cui l'utente si è iscritto. */
	@ManyToOne
  	@JoinColumn(name = "IdProcess")
	private Process idProcess;

	/** Attributo che indica il completamento del processo. */
	@Column(name = "Complete", nullable=false)
	private Boolean complete;

	/**
	 * Costruttore di default.
	 */
	public Subscription(){}
	
	/**
 	 * Costruttore che imposta l'entità con i dati contenuti nel JSONObject.
 	 * 
 	 * @param json il valore che permette di impostare gli attributi dell'entità
 	 */
	public Subscription(JSONObject json) {
		idProcess=new Process();
		idUser=new User();
		if(json.has("IdSubscription"))
			idSubscription = (Integer)json.get("IdSubscription");
		if(json.has("Complete"))
			complete = (Boolean)json.get("Complete");
		if(json.has("IdUser"))
			idUser.setIdUser((Integer)json.get("IdUser"));
		if(json.has("IdProcess"))
			idProcess.setIdProcess((Integer)json.get("IdProcess"));
	}

	/**
	 * Ritorna l'id dell'iscrizione.
	 * 
	 * @return l'id dell'iscrizione
	 */
	public Integer getIdSubscription(){ 
		return idSubscription; 
	}
	
	/**
	 * Imposta l'id dell'iscrizione al valore passato.
	 * 
	 * @param idSubscription il valore da impostare come id dell'iscrizione
	 */
	public void setIdSubscription(Integer idSubscription){ 
		this.idSubscription = idSubscription; 
	}
	
	/**
	 * Ritorna l'utente che si è iscritto al processo.
	 * 
	 * @return l'utente che si è iscritto al processo
	 */
	public User getIdUser(){ 
		return idUser; 
	}
	
	/**
	 * Imposta l'utente che si è iscritto al processo al valore passato.
	 * 
	 * @param idUser il valore da impostare come utente che si è iscritto al processo
	 */
	public void setIdUser(User idUser){ 
		this.idUser = idUser; 
	}
	
	/**
	 * Ritorna l'id del processo a cui l'utente si è iscritto.
	 * 
	 * @return l'id del processo a cui l'utente si è iscritto
	 */
	public Process getIdProcess(){ 
		return idProcess; 
	}
	
	/**
	 * Imposta l'id del processo a cui l'utente si è iscritto al valore passato.
	 * 
	 * @param idProcess il valore da impostare come id del processo a cui l'utente si è iscritto
	 */
	public void setIdProcess(Process idProcess){ 
		this.idProcess = idProcess; 
	}
	
	/**
	 * Ritorna l'attributo che indica il completamento del processo.
	 * @return l'attributo che indica il completamento del processo
	 */
	public Boolean getComplete(){ 
		return complete; 
	}
	
	/**
	 * Imposta l'attributo che indica il completamento del processo al valore passato.
	 * 
	 * @param complete il valore da impostare come attributo che indica il completamento del processo
	 */
	public void setComplete(Boolean complete){ 
		this.complete = complete; 
	}
	
	/**
 	 * Ritorna un JSONObject con i dati dell'entità.
 	 * 
 	 * @return un JSONObject con i dati dell'entità
 	 */
	@Override
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("IdSubscription" , idSubscription);
		json.put("Complete" , complete);
		json.put("IdUser" , idUser.getIdUser());
		json.put("IdProcess" , idProcess.getIdProcess());
		return json;
	}
}