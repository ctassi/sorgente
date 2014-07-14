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
 * 	File contentente l'interfaccia IDAOManagementProcessAdmin
 * 
 *	@file		IDAOManagementProcessAdmin.java
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
 *	L'interfaccia IDAOManagementProcessAdmin offre i metodi per eseguire operazioni di lettura e scrittura sul database disponibili agli amministratori.
 *
 *	@author 	DeSQ
 */
public interface IDAOManagementProcessAdmin {
 
	/**
	 * Permette di creare un nuovo processo con i dati forniti.
	 * 
	 * @param process contiene i dati del nuovo processo da inserire
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public void insertProcess(Process process) throws Exception;
	
	/**
	 * Permette di cercare i processi terminati e amministrati da un certo amministratore.
	 * 
	 * @param user contiene l'id dell'amministratore che esegue la ricerca
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Process> findClosedProcess(User user) throws Exception;
	
	/**
	 * Permette di cercare i passi da validare da un certo amministratore.
	 * 
	 * @param user contiene l'id dell'amministratore che esegue la ricerca
	 * @return la lista degli id della raccolta dati dei passi da validare
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Integer> findIdDataCollectionToVerify(User user) throws Exception;
	
	/**
	 * Permette di trovare tutti gli utenti che partecipano ad un determinato processo dell'amministratore.
	 * 
	 * @param process contiene l'id del processo su cui si vuole eseguire la ricerca
	 * @return la lista degli utenti
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<User> findUserSubscription(Process process) throws Exception;
	
	/**
	 * Permette di cercare gli utenti che hanno completato un determinato processo.
	 * 
	 * @param process contiene l'id del processo su cui si vuole eseguire la ricerca
	 * @return la lista degli id utente che hanno completato il processo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Integer> findUserSubscriptionComplete(Process process) throws Exception;
	
	/**
	 * Permette di trovare tutti i processi attivi amministrati da un certo amministratore.
	 * 
	 * @param user contiene l'id dell'amministratore che fa la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Process> findActiveProcess(User user) throws Exception;
	
	/**
	 * Permette di trovare una specifica raccolta dati.
	 * 
	 * @param data contiene l'id della raccolta dati da trovare
	 * @return la raccolta dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public DataCollection findDataCollection(DataCollection data) throws Exception;
	
	/**
	 * Permette di creare una nuova raccolta dati con i dati forniti.
	 * 
	 * @param data contiene i valori della raccolta dati da inserire
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public void insertDataCollection(DataCollection data) throws Exception;
	
	/**
	 * Permette di trovare tutte le raccolte dati di un determinato: utente, processo e livello .
	 * 
	 * @param datacollection contiene l'id dell'utente, il livello e l'id processo
	 * @return la lista della raccolta dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<DataCollection> findDataCollectionByLevel(DataCollection datacollection) throws Exception;
	
	/**
	 * Permette di trovare una specifica iscrizione a un processo.
	 * 
	 * @param user contiene l'id del user
	 * @param process contiene l'id del processo
	 * @return l'iscrizione al processo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public Subscription findSubscription(User user , Process process) throws Exception;
	
	/**
	 * Permette di creare una nuova iscrizione al processo con i dati forniti.
	 * 
	 * @param subscription contiene i dati dell'iscrizione al processo da inserire
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public void insertSubscription(Subscription subscription) throws Exception;
	
	/**
	 * Permette di trovare tutti i processi di un amministratore.
	 * 
	 * @param user contiene l'id dell'amministratore che esegue la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Process> findAllProcesses(User user) throws Exception;
	
	/**
	 * Permette di trovare tutti i passi di un determinato: amministratore, processo e livello .
	 * 
	 * @param admin contiene l'id dell'amministratore
	 * @param process contiene l'id processo
	 * @param level contiene il livello
	 * @return la lista dei passi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Step> findDataCollectionsStep(User admin, Process process, Integer level) throws Exception;
	
	/**
	 * Permette di trovare tutti i processi amministrati da un certo amministratore e che hanno passi da validare.
	 * 
	 * @param admin contiene l'id dell'amministratore che fa la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Process> findDataCollectionsProcess(User admin) throws Exception;
	
	/**
	 * Permette di trovare tutti i livelli di un processo amministrati da un certo amministratore e che hanno passi da validare.
	 * 
	 * @param admin contiene l'id dell'amministratore che fa la richiesta
	 * @param process contiene l'id del processo
	 * @return la lista dei livelli
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Integer> findDataCollectionsLevel(User admin, Process process) throws Exception;
	
	/**
	 * Permette di trovare tutte le raccolte dati di un passo.
	 * 
	 * @param admin contiene l'id dell'amministratore che fa la richiesta
	 * @param process contiene l'id del processo
	 * @param level contiene il livello del passo
	 * @param idStep contiene il passo
	 * @return la lista della raccolta dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<DataCollection> findDataCollectionIdStep(User admin, Process process, Integer level, Integer idStep) throws Exception;
	
	/**
	 * Permette di trovare tutti gli id degli utenti che sono presenti nel applicazione.
	 * 
	 * @return la lista degli id utente
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Integer> findAllUser() throws Exception;
	
	/**
	 * Permette di trovare un specifico processo.
	 * 
	 * @param process contiene l'id del processo da trovare
	 * @return il processo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public Process findProcess(Process process) throws Exception;
	
	/**
	 * Permette di trovare gli id delle raccolte dati completate di un determinato passo.
	 * 
	 * @param step contiene l'id del passo da trovare
	 * @return la lista degli id delle raccolte dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public List<Integer> findUserStepComplete(Step step) throws Exception;
	
	/**
	 * Permette di trovare il livello a cui è arrivato un utente in un processo.
	 * 
	 * @param user contiene l'id dell'utente
	 * @param process contiene l'id del processo
	 * @return il livello a cui è arrivato
	 * @throws Exception segnala un problema di connessione al database 
	 */
	public Integer findMinStep(User user , Process process) throws Exception;
}
 
