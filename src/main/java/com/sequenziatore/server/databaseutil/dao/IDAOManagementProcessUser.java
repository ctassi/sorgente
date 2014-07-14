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
 * 	File contentente l'interfaccia IDAOManagementProcessUser
 * 
 *	@file		IDAOManagementProcessUser.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.dao;

import java.util.List;

import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;

/**
 *	L'interfaccia IDAOManagementProcessUser offre i metodi per eseguire operazioni di lettura e scrittura sul database disponibili agli utenti.
 *
 *	@author 	DeSQ
 */
public interface IDAOManagementProcessUser {

	/**
	 * Permette di iscriversi ad un processo.
	 * 
	 * @param subscription contiene l'id dell'utente e l'id del processo a cui si vuole iscrivere
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public void insertSubscription(Subscription subscription) throws Exception;
	
	/**
	 * Permette di creare una nuova raccolta dati con i dati forniti.
	 * 
	 * @param data contiene i valori della raccolta dati da inserire
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public void insertDataCollection(DataCollection data) throws Exception;
	
	/**
	 * Permette di cercare una determinata iscrizione ad un processo.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @param process contiene l'id del processo
	 * @return l'iscrizione
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public Subscription findSubscription(User user , Process process) throws Exception;
	
	/**
	 * Permette di cercare un determinato processo.
	 * 
	 * @param process contiene l'id del processo
	 * @return il processo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public Process findProcessById(Process process) throws Exception;
	
	/**
	 * Permette di cercare tutti i processi a cui un determinato utente può iscriversi.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Process> findAvailableProcess(User user) throws Exception;
	
	/**
	 * Permette di recuperare le data collection da completare e i dati dei corrispondenti passi.
	 * 
	 * @param user contiene l'id dell'utente
	 * @param process contiene l'id del processo
	 * @param minLevel il livello a cui è arrivato
	 * @return la lista di data collection
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<DataCollection> findStep(User user, Process process, Integer minLevel) throws Exception;
	
	/**
	 * Permette di trovare il livello a cui è arrivato un utente in un processo.
	 * 
	 * @param user contiene l'id dell'utente
	 * @param process contiene l'id del processo
	 * @return il livello a cui è arrivato
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public Integer findMinStep(User user , Process process) throws Exception;
	
	/**
	 * Permette di trovare tutti i processi a cui l'utente è iscritto.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Subscription> findActiveProcess(User user) throws Exception;
	
	/**
	 * Permette di trovare la lista di raccolte dati di un determinato: livello e utente.
	 * 
	 * @param dataCollection contiene l'id dell'utente che fa la richiesta e il livello
	 * @return la lista delle raccolte dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<DataCollection> findDataCollectionByLevel(DataCollection dataCollection) throws Exception;
	
	/**
	 * Permette di trovare una specifica raccolta dati.
	 * 
	 * @param user contiene l'id del utente
	 * @param step contiene l'id del passo
	 * @return la raccolta dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public DataCollection findDataCollection(User user, Step step) throws Exception;
	
	/**
	 * Permette di trovare uno specifico passo.
	 * 
	 * @param step contiene l'id del passo
	 * @return la raccolta dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public Step findStep(Step step) throws Exception;
	
	/**
	 * Permette di trovare tutti i processi a cui l'utente ha partecipato con successo e terminati.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista degli id delle iscrizioni
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Integer> findClosedSuccessSubscription(User user) throws Exception;
	
	/**
	 * Permette di trovare tutti i processi a cui l'utente ha partecipato.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista degli id delle iscrizioni
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Integer> findAllClosedSubscription(User user) throws Exception;
	
	/**
	 * Permette di cancellare la partecipazione al processo.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @param process contiene l'id del processo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public void deleteSubscription(User user, Process process) throws Exception;
	
	/**
	 * Permette di cancellare la raccolta dati.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @param step contiene l'id del passo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public void deleteDataCollection(User user, Step step) throws Exception;
	
	/**
	 * Permette di trovare tutti i processi a cui l'utente ha partecipato con successo.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista delle iscrizioni
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Subscription> findSuccessSubscription(User user) throws Exception;
	
	/**
	 * Permette di trovare tutti i processi a cui l'utente ha partecipato con insuccesso.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista delle iscrizioni
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Subscription> findClosedSubscriptionFailure(User user) throws Exception;
}