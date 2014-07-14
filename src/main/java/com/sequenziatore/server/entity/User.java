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
 * 	File contentente la classe User
 * 
 *	@file		User.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.entity;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.json.JSONObject;

/**
 *	La classe User rappresenta logicamente un utente dell'applicazione, sia amministratore che utente standard.
 *
 *	@author 	DeSQ
 */
@Entity
@Table(name = "Users")
public class User implements Serializable, IEntity{

	private static final long serialVersionUID = 1L;
	
	/** Id dell'utente. */
	@Id
	@GeneratedValue
	@Column(name = "IdUser", nullable=false)
	private Integer idUser;

	/** Username dell'utente. */
	@Column(name = "Username", nullable=false, unique=true, length=50)
	private String username;

	/** Password dell'utente. */
	@Column(name = "Password", nullable=false, length=40)
	private String password;

	/** Nome dell'utente. */
	@Column(name = "Name", length=30)
	private String name;

	/** Cognome dell'utente. */
	@Column(name = "Surname", length=30)
	private String surname;

	/** Email dell'utente. */
	@Column(name = "Email", nullable=false, length=50)
	private String email;

	/** Città dell'utente. */
	@Column(name = "City", length=30)
	private String city;

	/** Provincia dell'utente. */
	@Column(name = "District", length=2)
	private String district;

	/** Attributo per distinguere amministratori da utenti standard. */
	@Column(name = "IsAdmin", nullable=false)
	private Boolean isAdmin;

	/**
	 * Costruttore di default.
	 */
	public User(){}
	
	/**
 	 * Costruttore che imposta l'entità con i dati contenuti nel JSONObject.
 	 * 
 	 * @param json il valore che permette di impostare gli attributi dell'entità
 	 */
	public User(JSONObject json) {
		if(json.has("IdUser"))
			idUser = (Integer)json.get("IdUser");
		if(json.has("Username"))
			username = (String)json.get("Username");
		if(json.has("Password"))
			password = this.hashPassword((String)json.get("Password"));
		if(json.has("Name"))
			name = (String)json.get("Name");
		if(json.has("Surname"))
			surname = (String)json.get("Surname");
		if(json.has("Email"))
			email = (String)json.get("Email");
		if(json.has("City"))
			city = (String)json.get("City");
		if(json.has("District"))
			district = (String)json.get("District");
		if(json.has("IsAdmin"))
			isAdmin = (Boolean)json.get("IsAdmin");
	}
	
	/**
	 * Ritorna l'id dell'utente.
	 * 
	 * @return l'id dell'utente
	 */
	public Integer getIdUser(){
		return idUser; 
	}
	
	/**
	 * Imposta l'id dell'utente al valore passato.
	 * 
	 * @param idUser il valore da impostare come id dell'utente
	 */
	public void setIdUser(Integer idUser){
		this.idUser = idUser; 
	}
	
	/**
	 * Ritorna l'username dell'utente.
	 * 
	 * @return l'username dell'utente
	 */
	public String getUsername(){
		return username; 
	}
	
	/**
	 * Imposta l'username dell'utente al valore passato.
	 * 
	 * @param username il valore da impostare come username dell'utente
	 */
	public void setUsername(String username){
		this.username = username; 
	}
	
	/**
	 * Ritorna la password dell'utente.
	 * 
	 * @return la password dell'utente
	 */
	public String getPassword(){ 
		return password;
	}
	
	/**
	 * Imposta la password dell'utente al valore passato.
	 * 
	 * @param password il valore da impostare come password dell'utente
	 */
	public void setPassword(String password){
		this.password = password; 
	}
	
	/**
	 * Imposta la password dell'utente al valore passato dopo averla hashata.
	 * 
	 * @param password password il valore da impostare come password dell'utente
	 */
	public void hashAndSetPassword(String password){
		this.password = hashPassword(password); 
	}
	
	/**
	 * Ritorna il nome dell'utente.
	 * 
	 * @return name il nome dell'utente
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Imposta il nome dell'utente al valore passato.
	 * 
	 * @param name il valore da impostare come nome dell'utente
	 */
	public void setName(String name){
		this.name = name; 
	}
	
	/**
	 * Ritorna il cognome dell'utente.
	 * 
	 * @return surname il cognome dell'utente
	 */
	public String getSurname(){ 
		return surname; 
	}
	
	 /**
	 * Imposta il cognome dell'utente al valore passato.
	 * 
	 * @param surname il valore da impostare come cognome dell'utente
	 */
	public void setSurname(String surname){
		this.surname = surname;
	}
	
	/**
	 * Ritorna l'email dell'utente.
	 * 
	 * @return email l'email dell'utente
	 */
	public String getEmail(){
		return email;
	}
	
	/**
	 * Imposta l'email dell'utente al valore passato.
	 * 
	 * @param email il valore da impostare come email dell'utente
	 */
	public void setEmail(String email){
		this.email = email;
	}

	/**
	 * Ritorna la città dell'utente.
	 * 
	 * @return la città dell'utente
	 */
	public String getCity(){
		return city;
	}
	
	/**
	 * Imposta la città dell'utente al valore passato.
	 * 
	 * @param city il valore da impostare come città dell'utente
	 */
	public void setCity(String city){
		this.city = city;
	}

	/**
	 * Ritorna la provincia dell'utente.
	 * 
	 * @return la provincia dell'utente
	 */
	public String getDistrict(){
		return district;
	}
	
	/**
	 * Imposta la provincia dell'utente al valore passato.
	 * 
	 * @param district il valore da impostare come provincia dell'utente
	 */
	public void setDistrict(String district){
		this.district = district;
	}

	/**
	 * Ritorna l'attributo per distinguere amministratori da utenti standard.
	 * 
	 * @return l'attributo per distinguere amministratori da utenti standard
	 */
	public Boolean getIsAdmin(){
		return isAdmin;
	}
	
	/**
	 * Imposta l'attributo per distinguere amministratori da utenti standard al valore passato.
	 * 
	 * @param isAdmin il valore da impostare come attributo per distinguere amministratori da utenti standard
	 */
	public void setIsAdmin(Boolean isAdmin){
		this.isAdmin = isAdmin;
	}

	/**
 	 * Ritorna un JSONObject con i dati dell'entità.
 	 * 
 	 * @return un JSONObject con i dati dell'entità
 	 */
	@Override
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("IdUser" , idUser);
		json.put("Username" , username);
		json.put("Name" , name);
		json.put("Surname" , surname);
		json.put("Email" , email);
		json.put("City" , city);
		json.put("District" , district);
		json.put("IsAdmin" , isAdmin);
		return json;
	}
	
	/**
 	 * Ritorna la password criptata attraverso SHA-1.
 	 * 
 	 * @param passwordNotSHA password non criptata
 	 * @return password criptata
 	 */
	private String hashPassword(String passwordNotSHA){
		MessageDigest digest;
		String passwordSHA=new String();
		try {
			digest = MessageDigest.getInstance("SHA-1");
			digest.update(passwordNotSHA.getBytes());
	        byte byteData[] = digest.digest();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        	passwordSHA=sb.toString();
	        }
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return passwordSHA;
	}
}
