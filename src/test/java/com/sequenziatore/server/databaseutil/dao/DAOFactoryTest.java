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
 * 	File contentente la classe DAOFactoryTest
 * 
 *	@file		DAOFactoryTest.java
 *	@author		DeSQ
 *	@date		2014-05-14
 *	@version	1.0
 */

package com.sequenziatore.server.databaseutil.dao;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 *	Classe contenente tutti i test di unità dei metodi della classe DAOFactory.
 *
 *	@author 	DeSQ
 */
public class DAOFactoryTest {

	/**
	 *	Test del metodo createDAOUserData() che restituisce un oggetto IDAOUserData.
	 */
    @Test
    public void testCreateDAOUserData() {
        DAOFactory daoFactoryObject = new DAOFactory();
        IDAOUserData iDAOUserDataObject = daoFactoryObject.createDAOUserData();
        
        assertNotNull(iDAOUserDataObject);
    }

    /**
	 *	Test del metodo createDAOManagementProcessAdmin() che restituisce un oggetto DAOManagementProcessAdmin.
	 */
    @Test
    public void testCreateDAOManagementProcessAdmin() {
        DAOFactory daoFactoryObject = new DAOFactory();
        IDAOManagementProcessAdmin DAOManagementProcessAdminObject = daoFactoryObject.createDAOManagementProcessAdmin();
        
        assertNotNull(DAOManagementProcessAdminObject);
    }
    
    /**
	 *	Test del metodo createDAOManagementProcessUser() che restituisce un oggetto DAOManagementProcessUser.
	 */
    @Test
    public void testCreateDAOManagementProcessUser() {
        DAOFactory daoFactoryObject = new DAOFactory();
        IDAOManagementProcessUser DAOManagementProcessUserObject = daoFactoryObject.createDAOManagementProcessUser();
        
        assertNotNull(DAOManagementProcessUserObject);
    }
}
