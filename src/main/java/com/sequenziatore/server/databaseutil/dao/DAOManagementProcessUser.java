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
 * 	File contentente la classe DAOManagementProcessUser
 * 
 *	@file		DAOManagementProcessUser.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.dao;

import java.util.List;

import org.hibernate.Query;

import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;

/**
 *	La classe DAOManagementProcessUser implementa i metodi per eseguire operazioni di lettura e scrittura sul database disponibili agli utenti.
 *
 *	@author 	DeSQ
 */
public class DAOManagementProcessUser implements IDAOManagementProcessUser {
 
	/**
	 * Permette di creare una nuova iscrizione al processo con i dati forniti.
	 * 
	 * @param subscription contiene i dati dell'iscrizione al processo da inserire
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public void insertSubscription(Subscription subscription) throws Exception {
		HibernateUtil.getSession().saveOrUpdate(subscription);
	}
	
	/**
	 * Permette di creare una nuova raccolta dati con i dati forniti.
	 * 
	 * @param data contiene i valori della raccolta dati da inserire
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public void insertDataCollection(DataCollection data) throws Exception {
		HibernateUtil.getSession().saveOrUpdate(data);
	}
	
	/**
	 * Permette di cercare una determinata iscrizione ad un processo.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @param process contiene l'id del processo
	 * @return l'iscrizione
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public Subscription findSubscription(User user , Process process) throws Exception {
		Subscription subscription=null;
		Query query = HibernateUtil.getSession().createQuery("select sub "
				+ "from Subscription sub, User us, Process pro "
				+ "where us.idUser = :idUser and pro.idProcess = :idProcess and pro.idProcess = sub.idProcess and us.idUser = sub.idUser");
		query.setParameter("idUser", user.getIdUser());
		query.setParameter("idProcess", process.getIdProcess());
		subscription = (Subscription) query.uniqueResult();
		return subscription;
	}
	
	/**
	 * Permette di cercare un determinato processo.
	 * 
	 * @param process contiene l'id del processo
	 * @return il processo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public Process findProcessById(Process process) throws Exception {
		Process processFind=null;
		Query queryProcess = HibernateUtil.getSession().createQuery("from Process proc where proc.idProcess = :idProcess ");
		queryProcess.setParameter("idProcess", process.getIdProcess());
		processFind= (Process) queryProcess.uniqueResult();
		return processFind;
	}
	
	/**
	 * Permette di cercare tutti i processi a cui un determinato utente può iscriversi.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Process> findAvailableProcess(User user) throws Exception {
		List<Process> processes=null;
		Query query = HibernateUtil.getSession().createQuery("select p "
				+ "from Process p "
				+ "where p.available = 1 and p.publicationDate <= current_date() and p.closingDate >= current_date() and p.endSubscriptionDate>=current_date() and p.idProcess not in "
				+ "( select sub.idProcess "
				+ "from Subscription sub "
				+ "where sub.idUser= :idUser ) "
				+ "order by p.publicationDate DESC");
		query.setParameter("idUser", user);
		processes=(List<Process>)query.list();
		return processes;
	}
	
	/**
	 * Permette di recuperare le data collection da completare e i dati dei corrispondenti passi.
	 * 
	 * @param user contiene l'id dell'utente
	 * @param process contiene l'id del processo
	 * @param minLevel il livello a cui è arrivato
	 * @return la lista di data collection
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollection> findStep(User user, Process process, Integer minLevel) throws Exception {
		List<DataCollection> data=null;		
		Query queryData = HibernateUtil.getSession().createQuery("select d "
				+ "from DataCollection d JOIN d.idStep s "
				+ "where d.idUser = :idUser and s.idProcess = :idProcess and s.level = :minLevel");
		queryData.setParameter("idUser", user);
		queryData.setParameter("idProcess", process);
		queryData.setParameter("minLevel", minLevel);
		data=(List<DataCollection>)queryData.list();
		return data;
	}
	
	/**
	 * Permette di trovare il livello a cui è arrivato un utente in un processo.
	 * 
	 * @param user contiene l'id dell'utente
	 * @param process contiene l'id del processo
	 * @return il livello a cui è arrivato
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public Integer findMinStep(User user , Process process) throws Exception {
		Integer minLevel=null;
		Query query = HibernateUtil.getSession().createQuery("select min(s.level) "
				+ "from DataCollection d JOIN d.idStep s "
				+ "where d.idUser = :idUser and s.idProcess = :idProcess and (d.state='NOTCOLLECTED' or d.state='FAILED' or d.state='TOVERIFY')");
		query.setParameter("idUser", user);
		query.setParameter("idProcess", process);
		minLevel = (Integer) query.uniqueResult();
		return minLevel;
	}
	
	/**
	 * Permette di trovare tutti i processi a cui l'utente è iscritto.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Subscription> findActiveProcess(User user) throws Exception {
		List<Subscription> subscriptions=null;
		Query query = HibernateUtil.getSession().createQuery("select sub from Subscription sub JOIN sub.idProcess p "
				+ "where sub.idUser = :idUser and sub.complete=0 and p.closingDate >= current_date() and p.available = 1 "
				+ "order by p.publicationDate DESC");
		query.setParameter("idUser", user);
		subscriptions=(List<Subscription>)query.list();
		return subscriptions;
	}
	
	/**
	 * Permette di trovare la lista di raccolte dati di un determinato: livello e utente.
	 * 
	 * @param dataCollection contiene l'id dell'utente che fa la richiesta e il livello
	 * @return la lista delle raccolte dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollection> findDataCollectionByLevel(DataCollection dataCollection) throws Exception {
		List<DataCollection> datacollections=null;
		Query queryList = HibernateUtil.getSession().createQuery("select d from DataCollection d JOIN d.idStep s where s.level = :level and d.idUser = :idUser and d.idCollection<> :idCollection");
		queryList.setParameter("level", dataCollection.getIdStep().getLevel());
		queryList.setParameter("idUser", dataCollection.getIdUser());
		queryList.setParameter("idCollection", dataCollection.getIdCollection());
		datacollections=(List<DataCollection>)queryList.list();
		return datacollections;
	}
	
	/**
	 * Permette di trovare una specifica raccolta dati.
	 * 
	 * @param user contiene l'id del utente
	 * @param step contiene l'id del passo
	 * @return la raccolta dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public DataCollection findDataCollection(User user, Step step) throws Exception {
		DataCollection data = null;
		Query queryData = HibernateUtil.getSession().createQuery("select d from DataCollection d, Step s , User us where us.idUser = :idUser and s.idStep=:idStep and d.idUser = us.idUser and d.idStep = s.idStep");
		queryData.setParameter("idUser", user.getIdUser());
		queryData.setParameter("idStep", step.getIdStep());
		data = (DataCollection) queryData.uniqueResult();
		return data;
	}
	
	/**
	 * Permette di trovare uno specifico passo.
	 * 
	 * @param step contiene l'id del passo
	 * @return la raccolta dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public Step findStep(Step step) throws Exception {
		Step stepQery = null;
		Query queryStep = HibernateUtil.getSession().createQuery("select s from Step s where s.idStep=:idStep ");
		queryStep.setParameter("idStep", step.getIdStep());
		stepQery = (Step) queryStep.uniqueResult();
		return stepQery;
	}
	
	/**
	 * Permette di trovare tutti i processi a cui l'utente ha partecipato con successo e terminati.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista degli id delle iscrizioni
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findClosedSuccessSubscription(User user) throws Exception {
		List<Integer> subscriptions=null;
		Query query = HibernateUtil.getSession().createQuery("select sub.idSubscription from Subscription sub, User us "
				+ "where us.idUser = :idUser and sub.complete=1 and sub.idUser = us.idUser and sub.idProcess in "
				+ "(select p.idProcess from Process p "
				+ "where p.closingDate < current_date() or p.available = 0)");
		query.setParameter("idUser", user.getIdUser());
		subscriptions=(List<Integer>)query.list();
		return subscriptions;
	}
	
	/**
	 * Permette di trovare tutti i processi a cui l'utente ha partecipato.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista degli id delle iscrizioni
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findAllClosedSubscription(User user) throws Exception {
		List<Integer> subscriptions=null;
		Query query = HibernateUtil.getSession().createQuery("select sub.idSubscription from Subscription sub, User us "
				+ "where us.idUser = :idUser and sub.idUser = us.idUser and sub.idProcess in "
				+ "(select p.idProcess from Process p "
				+ "where p.closingDate < current_date() or p.available = 0)");
		query.setParameter("idUser", user.getIdUser());
		subscriptions=(List<Integer>)query.list();
		return subscriptions;
	}
	
	/**
	 * Permette di cancellare la partecipazione al processo.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @param process contiene l'id del processo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public void deleteSubscription(User user, Process process) throws Exception {
		Query query = HibernateUtil.getSession().createQuery("delete Subscription sub where sub.idUser = :idUser and sub.idProcess = :idProcess");
		query.setParameter("idUser", user);
		query.setParameter("idProcess", process);
		query.executeUpdate();
	}
	
	/**
	 * Permette di cancellare la raccolta dati.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @param step contiene l'id del passo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public void deleteDataCollection(User user, Step step) throws Exception {
		Query query = HibernateUtil.getSession().createQuery("delete DataCollection d where d.idUser = :idUser and d.idStep=:idStep");
		query.setParameter("idUser", user);
		query.setParameter("idStep", step);
		query.executeUpdate();
	}
	
	/**
	 * Permette di trovare tutti i processi a cui l'utente ha partecipato con successo.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista delle iscrizioni
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Subscription> findSuccessSubscription(User user) throws Exception {
		List<Subscription> subscriptions=null;
		Query query = HibernateUtil.getSession().createQuery("select sub from Subscription sub "
				+ "where sub.idUser = :idUser and sub.complete= 1 ");
		query.setParameter("idUser", user);
		subscriptions=(List<Subscription>)query.list();
		return subscriptions;
	}
		
	/**
	 * Permette di trovare tutti i processi a cui l'utente ha partecipato con insuccesso.
	 * 
	 * @param user contiene l'id dell'utente che fa la richiesta
	 * @return la lista delle iscrizioni
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Subscription> findClosedSubscriptionFailure(User user) throws Exception {
		List<Subscription> subscriptions=null;
		Query query = HibernateUtil.getSession().createQuery("select sub from Subscription sub "
				+ "where sub.idUser = :idUser and sub.complete= 0 and sub.idProcess in "
				+ "(select p.idProcess from Process p "
				+ "where p.closingDate < current_date() or p.available = 0)");
		query.setParameter("idUser", user);
		subscriptions=(List<Subscription>)query.list();
		return subscriptions;
	}	
}
 
