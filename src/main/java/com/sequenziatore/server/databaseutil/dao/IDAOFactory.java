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
 * 	File contentente l'interfaccia IDAOFactory
 * 
 *	@file		IDAOFactory.java
 *	@author		DeSQ
 *	@date		2014-05-12
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.dao;

/**
 *	L'interfaccia IDAOFactory viene utilizzata per gestire la creazione di oggetti delle classi DAOUserData, DAOManagementProcessAdmin e DAOManagementProcessUser.
 *
 *	@author 	DeSQ
 */
public interface IDAOFactory {
 
	/**
	 * Ritorna un oggetto IDAOUserData.
	 * 
	 * @return l'oggetto IDAOUserData
	 */
	public IDAOUserData createDAOUserData();
	
	/**
	 * Ritorna un oggetto IDAOManagementProcessAdmin.
	 * 
	 * @return l'oggetto IDAOManagementProcessAdmin
	 */
	public IDAOManagementProcessAdmin createDAOManagementProcessAdmin();
	
	/**
	 * Ritorna un oggetto IDAOManagementProcessUser.
	 * 
	 * @return l'oggetto IDAOManagementProcessUser
	 */
	public IDAOManagementProcessUser createDAOManagementProcessUser();
}
 
