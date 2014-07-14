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
 * 	File contentente la classe Process
 * 
 *	@file		Process.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.json.JSONObject;

/**
 *	La classe Process rappresenta logicamente un processo nell'applicazione.
 *
 *	@author 	DeSQ
 */
@Entity
@Table(name = "Processes")
public class Process implements Serializable, IEntity{
	
	private static final long serialVersionUID = 1L;

	/** Id del processo. */
	@Id
	@GeneratedValue
	@Column(name = "IdProcess", nullable=false)
	private Integer idProcess;
	
	/** Nome del processo. */
	@Column(name = "Name", nullable=false, length=50)
	private String name;

	/** Descrizione del processo. */
	@Column(name = "Description", nullable=false, length=400)
	private String description;

	/** Numero totale di passi del processo. */
	@Column(name = "TotalLevels", nullable=false)
	private Integer totalLevel;

	/** Data di pubblicazione del processo. */
	@Column(name = "PublicationDate", nullable=false)
	@Temporal(TemporalType.DATE)
	private Calendar publicationDate;

	/** Data di chiusura del processo. */
	@Column(name = "ClosingDate", nullable=false)
	@Temporal(TemporalType.DATE)
	private Calendar closingDate;

	/** Disponibilità del processo. */
	@Column(name = "Available", nullable=false)
	private Boolean available;

	/** Data di chiusura delle iscrizioni al processo. */
	@Column(name = "EndSubscriptionDate")
	@Temporal(TemporalType.DATE)
	private Calendar endSubscriptionDate;

	/** Id dell'amministratore del processo. */
	@ManyToOne  
 	@JoinColumn(name = "Admin")
	private User admin;

	/** Lista dei passi che compongono il processo. */
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL,CascadeType.PERSIST,CascadeType.MERGE }, mappedBy = "idProcess")
	private List<Step> steps;

	/**
	 * Costruttore di default.
	 */
	public Process(){}

	/**
 	 * Costruttore che imposta l'entità con i dati contenuti nel JSONObject.
 	 * 
 	 * @param json il valore che permette di impostare gli attributi dell'entità
 	 */
	public Process(JSONObject json) {
		admin=new User();
		if(json.has("IdProcess"))
			idProcess = (Integer)json.get("IdProcess");
		if(json.has("Name"))
			name = (String)json.get("Name");
		if(json.has("Description"))
			description = (String)json.get("Description");
		if(json.has("TotalLevel"))
			totalLevel = (Integer)json.get("TotalLevel");
		if(json.has("PublicationDate")){
			JSONObject jsonDate= (JSONObject)json.get("PublicationDate");
			publicationDate = new GregorianCalendar((Integer)jsonDate.get("Year"),(Integer)jsonDate.get("Month")-1,(Integer)jsonDate.get("Day"));
		}
		if(json.has("ClosingDate")){
			JSONObject jsonDate= (JSONObject)json.get("ClosingDate");
			closingDate = new GregorianCalendar((Integer)jsonDate.get("Year"),(Integer)jsonDate.get("Month")-1,(Integer)jsonDate.get("Day"));
		}
		if(json.has("EndSubscriptionDate")){
			JSONObject jsonDate= (JSONObject)json.get("EndSubscriptionDate");
			endSubscriptionDate = new GregorianCalendar((Integer)jsonDate.get("Year"),(Integer)jsonDate.get("Month")-1,(Integer)jsonDate.get("Day"));
		}
		if(json.has("Available"))
			available = (Boolean)json.get("Available");
		if(json.has("IdUser"))
			admin.setIdUser((Integer)json.get("IdUser"));
	}
	
	/**
	 * Ritorna l'id del processo.
	 * 
	 * @return l'id del processo
	 */
	public Integer getIdProcess(){ 
		return idProcess; 
	}
	
	/**
	 * Imposta l'id del processo al valore passato.
	 * 
	 * @param idProcess il valore da impostare come id del processo
	 */
	public void setIdProcess(Integer idProcess){
		this.idProcess = idProcess; 
	}
	
	/**
	 * Ritorna il nome del processo.
	 * 
	 * @return il nome del processo
	 */
	public String getName(){ 
		return name; 
	}
	
	/**
	 * Imposta il nome del processo al valore passato.
	 * 
	 * @param name il valore da impostare come nome del processo
	 */
	public void setName(String name){ 
		this.name = name; 
	}
	
	/**
	 * Ritorna la descrizione del processo.
	 * 
	 * @return la descrizione del processo
	 */
	public String getDescription(){ 
		return description; 
	}
	
	/**
	 * Imposta la descrizione del processo al valore passato.
	 * 
	 * @param description il valore da impostare come descrizione del processo
	 */
	public void setDescription(String description){ 
		this.description = description;
	}
	
	/**
	 * Ritorna il numero totale di passi del processo.
	 * 
	 * @return il numero totale di passi del processo
	 */
	public Integer getTotalLevel(){ 
		return totalLevel; 
	}
	
	/**
	 * Imposta il numero totale di passi del processo al valore passato.
	 * 
	 * @param totalLevel il valore da impostare come numero totale di passi del processo
	 */
	public void setTotalLevel(Integer totalLevel){ 
		this.totalLevel = totalLevel; 
	}
	
	/**
	 * Ritorna la data di pubblicazione del processo.
	 * 
	 * @return la data di pubblicazione del processo
	 */
	public Calendar getPublicationDate(){ 
		return publicationDate; 
	}
	
	/**
	 * Imposta la data di pubblicazione del processo al valore passato.
	 * 
	 * @param publicationDate il valore da impostare come data di pubblicazione del processo
	 */
	public void setPublicationDate(Calendar publicationDate){ 
		this.publicationDate = publicationDate; 
	}
	
	/**
	 * Ritorna la data di chiusura del processo.
	 * 
	 * @return la data di chiusura del processo
	 */
	public Calendar getClosingDate(){ 
		return closingDate; 
	}
	
	/**
	 * Imposta la data di chiusura del processo al valore passato.
	 * 
	 * @param closingDate il valore da impostare come data di chiusura del processo
	 */
	public void setClosingDate(Calendar closingDate){ 
		this.closingDate = closingDate; 
	}
	
	/**
	 * Ritorna la disponibilità del processo.
	 * 
	 * @return la disponibilità del processo
	 */
	public Boolean getAvailable(){ 
		return available; 
	}
	
	/**
	 * Imposta la disponibilità del processo al valore passato.
	 * 
	 * @param available il valore da impostare come disponibilità del processo
	 */
	public void setAvailable(Boolean available){ 
		this.available = available;
	}
	
	/**
	 * Ritorna la data di chiusura delle iscrizioni al processo.
	 * 
	 * @return la data di chiusura delle iscrizioni al processo
	 */
	public Calendar getEndSubscriptionDate(){ 
		return endSubscriptionDate; 
	}
	
	/**
	 * Imposta la data di chiusura delle iscrizioni al processo al valore passato.
	 * 
	 * @param endSubscriptionDate il valore da impostare come data di chiusura delle iscrizioni al processo
	 */
	public void setEndSubscriptionDate(Calendar endSubscriptionDate){ 
		this.endSubscriptionDate = endSubscriptionDate; 
	}
	
	/**
	 * Ritorna l'id dell'amministratore del processo.
	 * 
	 * @return l'id dell'amministratore del processo
	 */
	public User getAdmin(){ 
		return admin; 
	}
	
	/**
	 * Imposta l'id dell'amministratore del processo al valore passato.
	 * 
	 * @param admin il valore da impostare come id dell'amministratore del processo
	 */
	public void setAdmin(User admin){ 
		this.admin = admin; 
	}
	
	/**
	 * Ritorna la lista dei passi che compongono il processo.
	 * 
	 * @return la lista dei passi che compongono il processo
	 */
	public List<Step> getSteps(){ 
		return steps; 
	}
	
	/**
	 * Imposta la lista di passi che compongono il processo al valore passato.
	 * 
	 * @param steps il valore da impostare come lista di passi che compongono il processo
	 */
	public void setSteps(List<Step> steps){ 
		this.steps = steps; 
	}
	
	/**
 	 * Ritorna un JSONObject con i dati dell'entità.
 	 * 
 	 * @return un JSONObject con i dati dell'entità
 	 */
	@Override
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		int publicationDateM =  publicationDate.get(Calendar.MONTH)+1;
		int closingDateM = closingDate.get(Calendar.MONTH)+1;
		int endSubscriptionDateM = endSubscriptionDate.get(Calendar.MONTH)+1;
		json.put("IdProcess" , idProcess);
		json.put("Name" , name);
		json.put("Description" , description);
		json.put("TotalLevel" , totalLevel);
		json.put("PublicationDate" , publicationDate.get(Calendar.DAY_OF_MONTH)+ "-" + publicationDateM + "-" + publicationDate.get(Calendar.YEAR));
		json.put("ClosingDate" ,  closingDate.get(Calendar.DAY_OF_MONTH) + "-" + closingDateM + "-" + closingDate.get(Calendar.YEAR));
		json.put("EndSubscriptionDate" , endSubscriptionDate.get(Calendar.DAY_OF_MONTH)+ "-" + endSubscriptionDateM + "-" + endSubscriptionDate.get(Calendar.YEAR));
		json.put("Available" , available);
		return json;
	}
}