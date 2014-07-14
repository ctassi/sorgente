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
 * 	File contentente la classe DAOFactory
 * 
 *	@file		DAOFactory.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.dao;

/**
 *	Classe DAOFactory viene utilizzata per gestire la creazione di oggetti delle classi DAOUserData, DAOManagmentProcessAdmin e DAOManagementProcessUser.
 *
 *	@author 	DeSQ
 */
public class DAOFactory implements IDAOFactory {
 
	/**
	 * Ritorna un oggetto IDAOUserData.
	 * 
	 * @return l'oggetto IDAOUserData
	 */
	@Override
	public IDAOUserData createDAOUserData() {
		return new DAOUserData();
	}
	
	/**
	 * Ritorna un oggetto IDAOManagementProcessAdmin.
	 * 
	 * @return l'oggetto IDAOManagementProcessAdmin
	 */
	@Override
	public IDAOManagementProcessAdmin createDAOManagementProcessAdmin() {
		return new DAOManagementProcessAdmin();
	}
	
	/**
	 * Ritorna un oggetto IDAOManagementProcessUser.
	 * 
	 * @return l'oggetto IDAOManagementProcessUser
	 */
	@Override
	public IDAOManagementProcessUser createDAOManagementProcessUser() {
		return new DAOManagementProcessUser();
	}
	 
}
 
