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
 * 	File contentente la classe Notification
 * 
 *	@file		Notification.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.controller;

/**
 *	La classe permette di far visualizzare delle notifiche al front-end.
 *
 *	@author 	DeSQ
 */
public class Notification {
	
	/** Username di un utente. */
	private String username;
	
	/** Testo che verrà visualizzato dall'utente. */
	private String message;
	
	/** Tipo di messaggio da far visualizzare. */
	private String actionType;
	
	/** Indicatore dell'esito dell'operazione. */
	private String success;
	
	/**
	 * Ritorna il valore del campo username.
	 * 
	 * @return il valore del campo username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Imposta l'username al valore passato.
	 * 
	 * @param username il valore da impostare come username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Ritorna il messaggio da far vedere all'utente.
	 * 
	 * @return il messaggio da far vedere all'utente
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Imposta il messaggio da far vedere all'utente.
	 * 
	 * @param message il valore da impostare come messaggio da far vedere all'utente
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Ritorna il tipo del messaggio da far visualizzare.
	 * 
	 * @return il tipo del messaggio da far visualizzare
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * Imposta il tipo del messaggio da far visualizzare.
	 * 
	 * @param actionType il valore da impostare come tipo del messaggio da far visualizzare
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	/**
	 * Ritorna il valore del campo dati che indica l'esito dell'operazione.
	 * 
	 * @return il valore del campo dati che indica l'esito dell'operazione
	 */
	public String getSuccess() {
		return success;
	}

	/**
	 * Imposta il valore del campo dati che indica l'esito dell'operazione.
	 * 
	 * @param success il valore da impostare al campo dati che indica l'esito dell'operazione
	 */
	public void setSuccess(String success) {
		this.success = success;
	}
	
}
