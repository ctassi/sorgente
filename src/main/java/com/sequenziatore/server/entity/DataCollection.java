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
 * 	File contentente la classe DataCollection
 * 
 *	@file		DataCollection.java
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
 *	La classe DataCollection rappresenta logicamente una raccolta dati effettuata da un utente per completare un passo.
 *
 *	@author 	DeSQ
 */
@Entity
@Table(name = "DataCollection")
public class DataCollection implements Serializable, IEntity{
 
	private static final long serialVersionUID = 1L;

	/** Id della raccolta dati. */
	@Id
 	@GeneratedValue
 	@Column(name = "IdCollection", nullable=false)
 	private Integer idCollection;
 	
	/** Path della foto caricata dall'utente. */
 	@Column(name = "Photo", length=300)
 	private String photo;
 	
 	/** Attributo che indica lo stato se la foto inserita dall'utente e' coretta. */
	@Column(name = "WrongPhoto")
	private Boolean wrongPhoto;

	/** Testo caricato dall'utente. */
 	@Column(name = "Text", length=300)
 	private String text;
 	
 	/** Attributo che indica lo stato se il testo inserito dall'utente ha inserito e' corretto. */
	@Column(name = "WrongText")
	private Boolean wrongText;

	/** Latitudine fornita dall'utente. */
 	@Column(name = "Latitude", length=100)
 	private String latitude;

	/** Longitudine fornita dall'utente. */
 	@Column(name = "Longitude", length=100)
 	private String longitude;
 	
 	/** Attributo che indica lo stato se la geolocazione inserita dall'utente e' coretta. */
	@Column(name = "WrongGeolocation")
	private Boolean wrongGeolocation;

	/** Stato della raccolta dati. */
 	@Column(name = "State", nullable=false)
 	@Enumerated(EnumType.STRING)
 	private EnumState state;

	/** Id dell'utente che ha caricato i dati. */
 	@ManyToOne  
  	@JoinColumn(name = "IdUser")
 	private User idUser;

	/** Id del passo soddisfatto dalla raccolta dati. */
 	@ManyToOne  
  	@JoinColumn(name = "IdStep")
 	private Step idStep;

	/**
	 * Stati possibili della raccolta dati: 
	 * VERIFIED se la raccolta è stata verificata;
	 * TOVERIFY se la raccolta è da verificare;
	 * NOTCOLLECTED se la raccolta deve ancora essere eseguita;
	 * SKIPPED se il passo è stato saltato;
	 * FAILED se la raccolta non ha superato la convalida.
	 */
 	public enum EnumState {
    		VERIFIED, TOVERIFY,NOTCOLLECTED, FAILED, SKIPPED; 
 	}
 	
 	/**
 	 * Costruttore di default.
 	 */
 	public DataCollection(){}

 	/**
 	 * Costruttore che imposta l'entità con i dati contenuti nel JSONObject.
 	 * 
 	 * @param json il valore che permette di impostare gli attributi dell'entità
 	 */
	public DataCollection(JSONObject json) {
		idUser = new User();
		idStep = new Step();
		if(json.has("IdCollection"))
			idCollection = (Integer)json.get("IdCollection");
		if(json.has("Photo"))
			photo=(String)json.get("Photo");
		if(json.has("Text"))
			text=(String)json.get("Text");
		if(json.has("Latitude"))
			latitude=(String)json.get("Latitude");
		if(json.has("Longitude"))
			longitude=(String)json.get("Longitude");
		if(json.has("State")){
			if(((String)json.get("State")).equals("VERIFIED"))
				state = EnumState.VERIFIED;
			if(((String)json.get("State")).equals("TOVERIFY"))
				state = EnumState.TOVERIFY;
			if(((String)json.get("State")).equals("NOTCOLLECTED"))
				state = EnumState.NOTCOLLECTED;
			if(((String)json.get("State")).equals("SKIPPED"))
				state = EnumState.SKIPPED;
			if(((String)json.get("State")).equals("FAILED"))
				state = EnumState.FAILED;
		}
		if(json.has("WrongText")){
			if(json.get("WrongText").getClass().toString().contains("java.lang.String")){
				String s = (String)json.get("WrongText");
				wrongText = Boolean.parseBoolean(s);
			}else
				wrongText=(Boolean)json.get("WrongText");
		}
		if(json.has("WrongPhoto")){
			if(json.get("WrongPhoto").getClass().toString().contains("java.lang.String")){
				String s = (String)json.get("WrongPhoto");
				wrongPhoto = Boolean.parseBoolean(s);
			}else
				wrongPhoto=(Boolean)json.get("WrongPhoto");
		}
		if(json.has("WrongGeolocation")){
			if(json.get("WrongGeolocation").getClass().toString().contains("java.lang.String")){
				String s = (String)json.get("WrongGeolocation");
				wrongGeolocation = Boolean.parseBoolean(s);
			}else
				wrongGeolocation=(Boolean)json.get("WrongGeolocation");
		}
		if(json.has("IdUser")){
			if(json.get("IdUser").getClass().toString().contains("java.lang.String")){
				String s = (String)json.get("IdUser");
				Integer i = Integer.parseInt(s);
				idUser.setIdUser(i);
			}else
				idUser.setIdUser((Integer)json.get("IdUser"));
		}
		if(json.has("IdStep")){
			if(json.get("IdStep").getClass().toString().contains("java.lang.String")){
				String s = (String)json.get("IdStep");
				Integer i = Integer.parseInt(s);
				idStep.setIdStep(i);
			}else
				idStep.setIdStep((Integer)json.get("IdStep"));
			
		}
	}
 	
 	/**
 	 * Ritorna l'id della raccolta dati.
 	 * 
 	 * @return l'id della raccolta dati
 	 */
 	public Integer getIdCollection(){
 		return idCollection; 
 	}
 	
 	/**
 	 * Imposta l'id della collezione al valore passato.
 	 * 
 	 * @param idCollection il valore da impostare come id della collezione
 	 */
 	public void setIdCollection(int idCollection){
 		this.idCollection = idCollection; 
 	}
 	
 	/**
 	 * Ritorna il path della foto caricata dall'utente. 
 	 * 
 	 * @return il path della foto caricata dall'utente
 	 */
 	public String getPhoto(){ 
 		return photo; 
 	}
 	
 	/**
 	 * Imposta il path della foto caricata dall'utente.
 	 * 
 	 * @param photo il valore da impostare come path della foto
 	 */
 	public void setPhoto(String photo){ 
 		this.photo = photo; 
 	}
 	
 	/**
 	 * Ritorna lo stato se la foto inserita dall'utente e' coretta. 
 	 * 
 	 * @return lo stato se la foto inserita dall'utente e' coretta
 	 */
 	public Boolean getWrongPhoto(){ 
 		return wrongPhoto; 
 	}
 	
 	/**
 	 * Imposta l'attributo che indica lo stato se la foto inserita dall'utente e' coretta.
	 * 
	 * @param wrongPhoto il valore da impostare come attributo che indica lo stato se la foto inserita dall'utente e' coretta.
 	 */
 	public void setWrongPhoto(Boolean wrongPhoto){ 
 		this.wrongPhoto = wrongPhoto; 
 	}
 	
 	/**
 	 * Ritorna il testo caricato dall'utente.
 	 * 
 	 * @return il testo caricato dall'utente
 	 */
 	public String getText(){ 
 		return text; 
 	}
 	
 	/**
 	 * Imposta il testo caricato dall'utente.
 	 * 
 	 * @param text il valore da impostare come testo
 	 */
 	public void setText(String text){ 
 		this.text = text; 
 	}
 	
 	/**
 	 * Ritorna lo stato se il testo inserito dall'utente e' coretto. 
 	 * 
 	 * @return lo stato se il testo inserito dall'utente e' coretto
 	 */
 	public Boolean getWrongText(){ 
 		return wrongText; 
 	}
 	
 	/**
 	 * Imposta l'attributo che indica lo stato se il testo inserito dall'utente ha inserito e' corretto.
	 * 
	 * @param wrongText il valore da impostare come attributo che indica lo stato se il testo inserito dall'utente ha inserito e' corretto.
 	 */
 	public void setWrongText(Boolean wrongText){ 
 		this.wrongText = wrongText; 
 	}

 	
 	/**
 	 * Ritorna la latitudine fornita dall'utente.
 	 * 
 	 * @return la latitudine fornita dall'utente
 	 */
 	public String getLatitude(){ 
 		return latitude; 
 	}
 	
 	/**
 	 * Imposta la latitudine fornita dall'utente.
 	 * 
 	 * @param latitude il valore da impostare come latitudine
 	 */
 	public void setLatitude(String latitude){ 
 		this.latitude = latitude; 
 	}
 	
 	/**
 	 * Ritorna la longitudine fornita dall'utente.
 	 * 
 	 * @return la longitudine fornita dall'utente
 	 */
 	public String getLongitude(){ 
 		return longitude; 
 	}
 	
 	/**
 	 * Imposta la longitudine fornita dall'utente.
 	 * 
 	 * @param longitude il valore da impostare come longitudine
 	 */
 	public void setLongitude(String longitude){ 
 		this.longitude = longitude;
 	}
 	
 	/**
 	 * Ritorna lo stato se la geolocazione inserita dall'utente e' coretta. 
 	 * 
 	 * @return lo stato se la geolocazione inserita dall'utente e' coretta
 	 */
 	public Boolean getWrongGeolocation(){ 
 		return wrongGeolocation; 
 	}
 	
 	/**
 	 * Imposta l'attributo che indica lo stato se la geolocazione inserita dall'utente e' coretta.
	 * 
	 * @param wrongGeolocation il valore da impostare come attributo che indica lo stato se la geolocazione inserita dall'utente e' coretta.
 	 */
 	public void setWrongGeolocation(Boolean wrongGeolocation){ 
 		this.wrongGeolocation = wrongGeolocation; 
 	}
 	
 	/**
 	 * Ritorna lo stato della raccolta dati.
 	 * 
 	 * @return lo stato della raccolta dati
 	 */
 	public EnumState getState(){ 
 		return state; 
 	}
 	
 	/**
 	 * Imposta lo stato della raccolta dati.
 	 * 
 	 * @param state il valore da impostare come stato della raccolta dati
 	 */
 	public void setState(EnumState state){ 
 		this.state = state;
 	}
 	
 	/**
 	 * Ritorna l'id dell'utente che ha caricato i dati.
 	 * 
 	 * @return l'id dell'utente che ha caricato i dati
 	 */
 	public User getIdUser(){ 
 		return idUser; 
 	}
 	
 	/**
 	 * Imposta l'id dell'utente che ha caricato i dati.
 	 * 
 	 * @param idUser il valore da impostare come id dell'utente che ha caricato i dati
 	 */
 	public void setIdUser(User idUser){ 
 		this.idUser = idUser;
 	}
 	
 	/**
 	 * Ritorna l'id del passo soddisfatto dalla raccolta dati.
 	 * 
 	 * @return l'id del passo soddisfatto dalla raccolta dati
 	 */
 	public Step getIdStep(){ 
 		return idStep;
 	}
 	
 	/**
 	 * Imposta l'id del passo soddisfatto dalla raccolta dati
 	 * 
 	 * @param idStep il valore da impostare come id del passo soddisfatto dalla raccolta dati
 	 */
 	public void setIdStep(Step idStep){ 
 		this.idStep = idStep; 
 	}
 	
 	/**
 	 * Ritorna un JSONObject con i dati dell'entità.
 	 * 
 	 * @return un JSONObject con i dati dell'entità
 	 */
	@Override
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("IdCollection" , idCollection);
		json.put("Photo" , photo);
		json.put("Text" , text);
		json.put("Latitude" , latitude);
		json.put("Longitude" , longitude);
		json.put("State" , state);
		json.put("WrongText" , wrongText);
		json.put("WrongPhoto" , wrongPhoto);
		json.put("WrongGeolocation" , wrongGeolocation);
		json.put("Username" , idUser.getUsername());
		return json;
	}
}