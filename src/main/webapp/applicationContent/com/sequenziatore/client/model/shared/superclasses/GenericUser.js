/*
 * Copyright [2014] [DeSQ]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 * Name : GenericUser.js
 * Module : PreAuthenticationRequestManager
 * Location : /applicationContent/com/sequenziatore/client/model/shared/superclasses
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-21     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 */

var GenericUser = function(){
	
	var id = undefined;
	var username = undefined;
	var email = undefined;
	var name = undefined;
	var surname = undefined;
	var city = undefined;
	var district = undefined;
	var isAdmin = undefined;
	var isLogged = undefined;
	var processList = undefined;

	function reset(){
		id = undefined;
		username = undefined;
		email = undefined;
		name = undefined;
		surname = undefined;
		city = undefined;
		district = undefined;
		isAdmin = undefined;
		isLogged = undefined;
		processList = undefined;
	};

	function getId(){
		return id;
	};
	
	function setId(newId){
		id = newId;
	};

	function getUsername(){
		return username;
	};
	
	function setUsername(newUsername){
		username = newUsername;
	};

	function getEmail(){
		return email;
	};
	
	function setEmail(newEmail){
		email = newEmail;
	};

	function getName(){
		return name;
	};
	
	function setName(newName){
		name = newName;
	};

	function getSurname(){
		return surname;
	};
	
	function setSurname(newSurname){
		surname = newSurname;
	};

	function getCity(){
		return city;
	};
	
	function setCity(newCity){
		city = newCity;
	};

	function getDistrict(){
		return district;
	};
	
	function setDistrict(newDistrict){
		district = newDistrict;
	};

	function getIsAdmin(){
		return isAdmin;
	};
	
	function setIsAdmin(newIsAdmin){
		isAdmin = newIsAdmin;
	};

	function getIsLogged(){
		return isLogged;
	};
	
	function setIsLogged(newIsLogged){
		isLogged = newIslogged;
	};

	function getProcessList(){
		return processList;
	};
	
	function setProcessList(newProcessList){
		if(newProcessList === undefined){
			processList = [];
		}
		else if(!(newProcessList["null"] === 'null')){
			processList = newProcessList;
		}
	};

	// Ritorno solo ciò che voglio rendere pubblico
	return{
		reset: reset,
		getId: getId,
		setId: setId,
		getUsername: getUsername,
		setUsername: setUsername,
		getEmail: getEmail,
		setEmail: setEmail,
		getName: getName,
		setName: setName,
		getSurname: getSurname,
		setSurname: setSurname,
		getCity: getCity,
		setCity: setCity,
		getDistrict: getDistrict,
		setDistrict: setDistrict,
		getIsAdmin: getIsAdmin,
		setIsAdmin: setIsAdmin,
		getIsLogged: getIsLogged,
		setIsLogged: setIsLogged,
		getProcessList: getProcessList,
		setProcessList: setProcessList
	};
};
