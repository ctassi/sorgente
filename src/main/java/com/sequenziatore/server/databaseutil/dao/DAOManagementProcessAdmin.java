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
 * 	File contentente la classe DAOManagementProcessAdmin
 * 
 *	@file		DAOManagementProcessAdmin.java
 *	@author		DeSQ
 *	@date		2014-05-07
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.dao;

import java.util.List;

import org.hibernate.Query;

import com.sequenziatore.server.databaseutil.util.HibernateUtil;
import com.sequenziatore.server.entity.DataCollection;
import com.sequenziatore.server.entity.Process;
import com.sequenziatore.server.entity.Step;
import com.sequenziatore.server.entity.Subscription;
import com.sequenziatore.server.entity.User;

/**
 *	La classe DAOManagementProcessAdmin implementa i metodi per eseguire operazioni di lettura e scrittura sul database disponibili agli amministratori.
 *
 *	@author 	DeSQ
 */
public class DAOManagementProcessAdmin implements IDAOManagementProcessAdmin {
 
	/**
	 * Permette di creare un nuovo processo con i dati forniti.
	 * 
	 * @param process contiene i dati del nuovo processo da inserire
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public void insertProcess(Process process) throws Exception {
		HibernateUtil.getSession().saveOrUpdate(process);
	}
	
	/**
	 * Permette di cercare i processi terminati e amministrati da un certo amministratore.
	 * 
	 * @param user contiene l'id dell'amministratore che esegue la ricerca
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Process> findClosedProcess(User user) throws Exception {
		List<Process> processes=null;
		Query query = HibernateUtil.getSession().createQuery("select p from Process p where p.admin = :idAdmin and (p.closingDate < current_date() or p.available = 0) order by p.publicationDate DESC");
		query.setParameter("idAdmin", user);
		processes=(List<Process>)query.list();
		return processes;
	}
	
	/**
	 * Permette di cercare i passi da validare da un certo amministratore.
	 * 
	 * @param user contiene l'id dell'amministratore che esegue la ricerca
	 * @return la lista degli id della raccolta dati dei passi da validare
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findIdDataCollectionToVerify(User user) throws Exception {
		List<Integer> dataCollections=null;
		Query query = HibernateUtil.getSession().createQuery("select d.idCollection from DataCollection d join d.idStep as s join s.idProcess as p where p.admin = :idAdmin and d.state='TOVERIFY'");
		query.setParameter("idAdmin", user);
		dataCollections=(List<Integer>)query.list();
		return dataCollections;
		}
	
	/**
	 * Permette di trovare tutti gli utenti che partecipano ad un determinato processo dell'amministratore.
	 * 
	 * @param process contiene l'id del processo su cui si vuole eseguire la ricerca
	 * @return la lista degli utenti
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<User> findUserSubscription(Process process) throws Exception {
		List<User> users=null;
		Query query = HibernateUtil.getSession().createQuery("select us "
				+ "from Process p, User us, Subscription sub "
				+ "where  p.idProcess =:idProcess and p.idProcess = sub.idProcess and us.idUser = sub.idUser");
		query.setParameter("idProcess", process.getIdProcess());
		users=(List<User>)query.list();
		return users;	
	}
	
	/**
	 * Permette di cercare gli utenti che hanno completato un determinato processo.
	 * 
	 * @param process contiene l'id del processo su cui si vuole eseguire la ricerca
	 * @return la lista degli id utente che hanno completato il processo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findUserSubscriptionComplete(Process process) throws Exception {
		List<Integer> users=null;
		Query query = HibernateUtil.getSession().createQuery("select us.idUser "
				+ "from Process p, User us, Subscription sub "
				+ "where  p.idProcess =:idProcess and p.idProcess = sub.idProcess and us.idUser = sub.idUser and sub.complete = 1");
		query.setParameter("idProcess", process.getIdProcess());
		users=(List<Integer>)query.list();
		return users;	
	}
	
	
	/**
	 * Permette di trovare tutti i processi attivi amministrati da un certo amministratore.
	 * 
	 * @param user contiene l'id dell'amministratore che fa la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Process> findActiveProcess(User user) throws Exception {
		List<Process> processes=null;
		Query query = HibernateUtil.getSession().createQuery("select p from Process p, User us where us.idUser = :idAdmin and p.closingDate >= current_date() and us.idUser = p.admin order by p.publicationDate DESC");
		query.setParameter("idAdmin", user.getIdUser());
		processes=(List<Process>)query.list();
		return processes;
	}
	
	
	/**
	 * Permette di trovare una specifica raccolta dati.
	 * 
	 * @param data contiene l'id della raccolta dati da trovare
	 * @return la raccolta dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public DataCollection findDataCollection(DataCollection data) throws Exception {
		DataCollection datacollection = null;
		Query query = HibernateUtil.getSession().createQuery("select d from DataCollection d where d.idCollection = :idCollection");
		query.setParameter("idCollection", data.getIdCollection());
		datacollection = (DataCollection) query.uniqueResult();
		return datacollection;
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
	 * Permette di trovare tutte le raccolte dati di un determinato: utente, processo e livello .
	 * 
	 * @param datacollection contiene l'id dell'utente, il livello e l'id processo
	 * @return la lista della raccolta dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollection> findDataCollectionByLevel(DataCollection datacollection) throws Exception {
		List<DataCollection> datacollections=null;
		Query queryList = HibernateUtil.getSession().createQuery("select d from DataCollection d JOIN d.idStep s where s.level = :level and s.idProcess = :idProcess and d.idUser = :idUser and d.idCollection<> :idCollection");
		queryList.setParameter("level", datacollection.getIdStep().getLevel());
		queryList.setParameter("idUser", datacollection.getIdUser());
		queryList.setParameter("idProcess", datacollection.getIdStep().getIdProcess());
		queryList.setParameter("idCollection", datacollection.getIdCollection());
		datacollections=(List<DataCollection>)queryList.list();
		return datacollections;
	}
	
	/**
	 * Permette di trovare una specifica iscrizione a un processo.
	 * 
	 * @param user contiene l'id del user
	 * @param process contiene l'id del processo
	 * @return l'iscrizione al processo
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
	 * Permette di trovare tutti i processi di un amministratore.
	 * 
	 * @param user contiene l'id dell'amministratore che esegue la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Process> findAllProcesses(User user) throws Exception {
		List<Process> processes=null;
		Query query = HibernateUtil.getSession().createQuery("select p from Process p, User u where u.idUser = :idUser and p.admin = u.idUser order by p.publicationDate DESC");
		query.setParameter("idUser", user.getIdUser());
		processes=(List<Process>)query.list();
		return processes;
	}
	
	/**
	 * Permette di trovare tutti i passi di un determinato: amministratore, processo e livello .
	 * 
	 * @param admin contiene l'id dell'amministratore
	 * @param process contiene l'id processo
	 * @param level contiene il livello
	 * @return la lista dei passi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Step> findDataCollectionsStep(User admin, Process process, Integer level) throws Exception {
		List<Step> resultQuery = null;
		Query query = HibernateUtil.getSession().createQuery("select distinct s from DataCollection d join d.idStep as s join s.idProcess as p where p.admin = :idAdmin and s.idProcess = :idProcess and s.level = :level and d.state='TOVERIFY' order by s.idStep");
		query.setParameter("idAdmin", admin);
		query.setParameter("idProcess", process);
		query.setParameter("level", level);
		resultQuery = (List<Step>) query.list();
		return resultQuery;
	}
	
	/**
	 * Permette di trovare tutti i processi amministrati da un certo amministratore e che hanno passi da validare.
	 * 
	 * @param admin contiene l'id dell'amministratore che fa la richiesta
	 * @return la lista dei processi
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Process> findDataCollectionsProcess(User admin) throws Exception {
		List<Process> resultQuery = null;
		Query query = HibernateUtil.getSession().createQuery("select distinct p from DataCollection d join d.idStep as s join s.idProcess as p where p.admin = :idAdmin and d.state='TOVERIFY' order by p.idProcess asc");
		query.setParameter("idAdmin", admin);
		resultQuery = (List<Process>) query.list();
		return resultQuery;

	}
	
	/**
	 * Permette di trovare tutti i livelli di un processo amministrati da un certo amministratore e che hanno passi da validare.
	 * 
	 * @param admin contiene l'id dell'amministratore che fa la richiesta
	 * @param process contiene l'id del processo
	 * @return la lista dei livelli
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findDataCollectionsLevel(User admin, Process process) throws Exception {
		List<Integer> resultQuery = null;
		Query query = HibernateUtil.getSession().createQuery("select distinct s.level from DataCollection d join d.idStep as s join s.idProcess as p where p.admin = :idAdmin and p.idProcess = :idProcess and d.state='TOVERIFY' order by s.level");
		query.setParameter("idAdmin", admin);
		query.setParameter("idProcess", process.getIdProcess());
		resultQuery = (List<Integer>) query.list();
		return resultQuery;
	}
	
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
	@Override
	@SuppressWarnings("unchecked")
	public List<DataCollection> findDataCollectionIdStep(User admin, Process process, Integer level, Integer idStep) throws Exception {
		List<DataCollection> resultQuery = null;
		Query query = HibernateUtil.getSession().createQuery("select d from DataCollection d join d.idStep as s join s.idProcess as p where p.admin = :idAdmin and s.idProcess = :idProcess and s.level = :level and s.idStep = :idStep and d.state='TOVERIFY'");
		query.setParameter("idAdmin", admin);
		query.setParameter("idProcess", process);
		query.setParameter("level", level);
		query.setParameter("idStep", idStep);
		resultQuery = (List<DataCollection>) query.list();
		return resultQuery;
	}
	
	/**
	 * Permette di trovare tutti gli id degli utenti che sono presenti nel applicazione.
	 * 
	 * @return la lista degli id utente
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findAllUser() throws Exception {
		List<Integer> resultQuery = null;
		Query query = HibernateUtil.getSession().createQuery("select us.idUser from User us where us.isAdmin = 0");
		resultQuery = (List<Integer>) query.list();
		return resultQuery;
	}
	
	/**
	 * Permette di trovare un specifico processo.
	 * 
	 * @param process contiene l'id del processo da trovare
	 * @return il processo
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	public Process findProcess(Process process) throws Exception {
		Process resultQuery = null;
		Query query = HibernateUtil.getSession().createQuery("select p from Process p where p.idProcess = :idProcess");
		query.setParameter("idProcess", process.getIdProcess());
		resultQuery = (Process) query.uniqueResult();
		return resultQuery;
	}
	
	/**
	 * Permette di trovare gli id delle raccolte dati completate di un determinato passo.
	 * 
	 * @param step contiene l'id del passo da trovare
	 * @return la lista degli id delle raccolte dati
	 * @throws Exception segnala un problema di connessione al database 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> findUserStepComplete(Step step) throws Exception {
		List<Integer> resultQuery = null;
		Query query = HibernateUtil.getSession().createQuery("select d.idCollection from DataCollection d join d.idStep as s where s.idStep = :idStep and d.state='VERIFIED'");
		query.setParameter("idStep", step.getIdStep());
		resultQuery = (List<Integer>) query.list();
		return resultQuery;
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
}
 
