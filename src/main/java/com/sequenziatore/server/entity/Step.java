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
 * 	File contentente la classe Step
 * 
 *	@file		Step.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.json.JSONObject;

/**
 *	La classe Step rappresenta logicamente un passo di un processo nell'applicazione.
 *
 *	@author 	DeSQ
 */
@Entity
@Table(name = "Steps")
public class Step implements Serializable, IEntity{

	private static final long serialVersionUID = 1L;

	/** Id del passo. */
	@Id
	@GeneratedValue
	@Column(name = "IdStep", nullable=false)
	private Integer idStep;
	
	/** Livello del passo nel processo a cui appartiene. */
	@Column(name = "Level", nullable=false)
	private Integer level;

	/** Descrizione del passo. */
	@Column(name = "Description", nullable=false, length=300)
	private String description;

	/** Attributo che indica se il passo richiede una foto. */
	@Column(name = "IsPhoto", nullable=false)
	private Boolean isPhoto;

	/** Attributo che indica se il passo richiede del testo. */
	@Column(name = "IsText", nullable=false)
	private Boolean isText;

	/** Attributo che indica se il passo richiede la geolocalizzazione. */
	@Column(name = "IsGeolocation", nullable=false)
	private Boolean isGeolocation;

	/** Testo da usare per la verifica automatica dei dati trasmessi. */
	@Column(name = "CheckText", length=300)
	private String checkText;

	/** Latitudine da usare per la verifica automatica dei dati trasmessi. */
	@Column(name = "CheckLatitude", length=100)
	private String checkLatitude;

	/** Longitudine da usare per la verifica automatica dei dati trasmessi. */
	@Column(name = "CheckLongitude", length=100)
	private String checkLongitude;

	/** Attributo che indica se il passo richiede la verifica manuale da un amministratore. */
	@Column(name = "AdminVerify", nullable=false)
	private Boolean adminVerify;

	/** L'attributo che indica il rapporto logico tra passi dello stesso livello nello stesso processo. */
	@Column(name = "Parallelism", nullable=false)
	@Enumerated(EnumType.STRING)
	private EnumParallelism parallelism;

	/** Id del processo a cui il passo appartiene. */
	@ManyToOne  
 	@JoinColumn(name = "IdProcess")
	private Process idProcess;

	/**
	 * Stati possibili di un passo: AND, OR, NOT.
	 */
	public enum EnumParallelism {
   		AND, OR, NOT; 
	}
	
	/**
	 * Costruttore di default.
	 */
	public Step(){}
	
	/**
 	 * Costruttore che imposta l'entità con i dati contenuti nel JSONObject.
 	 * 
 	 * @param json il valore che permette di impostare gli attributi dell'entità
 	 */
	public Step(JSONObject json) {
		idProcess=new Process();
		if(json.has("IdStep"))
			idStep = (Integer)json.get("IdStep");
		if(json.has("Level"))
			level = (Integer)json.get("Level");
		if(json.has("Description"))
			description = (String)json.get("Description");
		if(json.has("IsPhoto"))
			isPhoto = (Boolean)json.get("IsPhoto");
		if(json.has("IsText"))
			isText = (Boolean)json.get("IsText");
		if(json.has("IsGeolocation"))
			isGeolocation = (Boolean)json.get("IsGeolocation");
		if(json.has("CheckText"))
			checkText = (String)json.get("CheckText");
		if(json.has("CheckLatitude"))
			checkLatitude = (String)json.get("CheckLatitude");
		if(json.has("CheckLongitude"))
			checkLongitude = (String)json.get("CheckLongitude");
		if(json.has("AdminVerify"))
			adminVerify = (Boolean)json.get("AdminVerify");
		if(json.has("Parallelism")){
			if(((String)json.get("Parallelism")).equals("OR"))
				parallelism = EnumParallelism.OR;
			if(((String)json.get("Parallelism")).equals("AND"))
				parallelism = EnumParallelism.AND;
			if(((String)json.get("Parallelism")).equals("NOT"))
				parallelism = EnumParallelism.NOT;
		}

		if(json.has("IdProcess"))
			idProcess.setIdProcess((Integer)json.get("IdProcess"));
	}
	
	/**
	 * Ritorna l'id del passo.
	 * 
	 * @return l'id del passo
	 */
	public Integer getIdStep(){
		return idStep; 
	}
	
	/**
	 * Imposta l'id del passo al valore passato.
	 * 
	 * @param idStep il valore da impostare come id del passo
	 */
	public void setIdStep(Integer idStep){ 
		this.idStep = idStep; 
	}
	
	/**
	 * Ritorna il livello del passo nel processo a cui appartiene.
	 * 
	 * @return il livello del passo nel processo a cui appartiene
	 */
	public Integer getLevel(){ 
		return level; 
	}
	
	/**
	 * Imposta il livello del passo nel processo a cui appartiene al valore passato.
	 * 
	 * @param level il valore da impostare come livello del passo nel processo a cui appartiene
	 */
	public void setLevel(Integer level){ 
		this.level = level; 
	}
	
	/**
	 * Ritorna la descrizione del passo.
	 * 
	 * @return la descrizione del passo
	 */
	public String getDescription(){ 
		return description; 
	}
	
	/**
	 * Imposta la descrizione del passo al valore passato.
	 * 
	 * @param description il valore da impostare come descrizione del passo
	 */
	public void setDescription(String description){ 
		this.description = description; 
	}
	
	/**
	 * Ritorna l'attributo che indica se il passo richiede una foto.
	 * 
	 * @return l'attributo che indica se il passo richiede una foto
	 */
	public Boolean getIsPhoto(){
		return isPhoto; 
	}
	
	/**
	 * Imposta l'attributo che indica se il passo richiede una foto al valore passato.
	 * 
	 * @param isPhoto il valore da impostare come attributo che indica se il passo richiede una foto
	 */
	public void setIsPhoto(Boolean isPhoto){ 
		this.isPhoto = isPhoto; 
	}
	
	/**
	 * Ritorna l'attributo che indica se il passo richiede del testo.
	 * 
	 * @return l'attributo che indica se il passo richiede del testo
	 */
	public Boolean getIsText(){ 
		return isText; 
	}
	
	/**
	 * Imposta l'attributo che indica se il passo richiede del testo al valore passato.
	 * 
	 * @param isText il valore da impostare come attributo che indica se il passo richiede del testo
	 */
	public void setIsText(Boolean isText){ 
		this.isText = isText; 
	}
	
	/**
	 * Ritorna l'attributo che indica se il passo richiede la geolocalizzazione.
	 * 
	 * @return l'attributo che indica se il passo richiede la geolocalizzazione
	 */
	public Boolean getIsGeolocation(){ 
		return isGeolocation; 
	}
	
	/**
	 * Imposta l'attributo che indica se il passo richiede la geolocalizzazione al valore passato.
	 * 
	 * @param isGeolocation il valore da impostare come attributo che indica se il passo richiede la geolocalizzazione
	 */
	public void setIsGeolocation(Boolean isGeolocation){ 
		this.isGeolocation = isGeolocation; 
	}
	
	/**
	 * Ritorna il testo da usare per la verifica automatica dei dati trasmessi.
	 * 
	 * @return il testo da usare per la verifica automatica dei dati trasmessi
	 */
	public String getCheckText(){ 
		return checkText; 
	}
	
	/**
	 * Imposta il testo da usare per la verifica automatica dei dati trasmessi al valore passato.
	 * 
	 * @param checkText il valore da impostare come testo da usare per la verifica automatica dei dati trasmessi
	 */
	public void setCheckText(String checkText){ 
		this.checkText = checkText; 
	}
	
	/**
	 * Ritorna la latitudine da usare per la verifica automatica dei dati trasmessi.
	 * 
	 * @return la latitudine da usare per la verifica automatica dei dati trasmessi
	 */
	public String getCheckLatitude(){ 
		return checkLatitude; 
	}
	
	/**
	 * Imposta la latitudine da usare per la verifica automatica dei dati trasmessi al valore passato.
	 * 
	 * @param checkLatitude il valore da impostare come latitudine da usare per la verifica automatica dei dati trasmessi
	 */
	public void setCheckLatitude(String checkLatitude){ 
		this.checkLatitude = checkLatitude; 
	}
	
	/**
	 * Ritorna la longitudine da usare per la verifica automatica dei dati trasmessi.
	 * 
	 * @return la longitudine da usare per la verifica automatica dei dati trasmessi
	 */
	public String getCheckLongitude(){ 
		return checkLongitude; 
	}
	
	/**
	 * Imposta la longitudine da usare per la verifica automatica dei dati trasmessi al valore passato.
	 * 
	 * @param checkLongitude il valore da impostare come longitudine da usare per la verifica automatica dei dati trasmessi
	 */
	public void setCheckLongitude(String checkLongitude){ 
		this.checkLongitude = checkLongitude; 
	}
	
	/**
	 * Ritorna l'attributo che indica se il passo richiede la verifica manuale da un amministratore.
	 * @return l'attributo che indica se il passo richiede la verifica manuale da un amministratore
	 */
	public Boolean getAdminVerify(){ 
		return adminVerify; 
	}
	
	/**
	 * Imposta l'attributo che indica se il passo richiede la verifica manuale da un amministratore al valore passato.
	 * 
	 * @param adminVerify il valore da impostare come attributo che indica se il passo richiede la verifica manuale da un amministratore
	 */
	public void setAdminVerify(Boolean adminVerify){ 
		this.adminVerify = adminVerify; 
	}
	
	/**
	 * Ritorna l'attributo che indica il rapporto logico tra passi dello stesso livello nello stesso processo.
	 * 
	 * @return l'attributo che indica il rapporto logico tra passi dello stesso livello nello stesso processo
	 */
	public EnumParallelism getParallelism(){ 
		return parallelism; 
	}
	
	/**
	 * Imposta l'attributo che indica il rapporto logico tra passi dello stesso livello nello stesso processo al valore passato.
	 * 
	 * @param parallelism il valore da impostare come attributo che indica il rapporto logico tra passi dello stesso livello nello stesso processo
	 */
	public void setParallelism(EnumParallelism parallelism){ 
		this.parallelism = parallelism; 
	}

	/**
	 * Ritorna l'id del processo a cui il passo appartiene.
	 * 
	 * @return l'id del processo a cui il passo appartiene
	 */
	public Process getIdProcess(){ 
		return idProcess; 
	}
	
	/**
	 * Imposta l'id del processo a cui il passo appartiene al valore passato.
	 * 
	 * @param idProcess il valore da impostare come id del processo a cui il passo appartiene
	 */
	public void setIdProcess(Process idProcess){ 
		this.idProcess = idProcess; 
	}
	
	/**
 	 * Ritorna un JSONObject con i dati dell'entità.
 	 * 
 	 * @return un JSONObject con i dati dell'entità
 	 */
	@Override
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("IdStep" , idStep);
		json.put("Level" , level);
		json.put("Description" , description);
		json.put("IsPhoto" , isPhoto.toString());
		json.put("IsText" , isText.toString());
		json.put("IsGeolocation" , isGeolocation.toString());
		json.put("CheckText" , checkText);
		json.put("CheckLatitude" , checkLatitude);
		json.put("CheckLongitude" , checkLongitude);
		json.put("AdminVerify" , adminVerify);
		json.put("Parallelism" , parallelism);
		json.put("IdProcess" , idProcess.getIdProcess());
		return json;
	}
}