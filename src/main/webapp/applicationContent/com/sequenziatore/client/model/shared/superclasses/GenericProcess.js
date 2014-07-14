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
 * Name : GenericProcess.js
 * Module : PreAuthenticationRequestManager
 * Location : /applicationContent/com/sequenziatore/client/model/shared/superclasses
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-20     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 */

var GenericProcess = function(){

	var isAvailable = undefined;
	var description = undefined;
	var publicationDate = undefined;
	var closingDate = undefined;
	var name = undefined;
	var totalSteps = undefined;
	var idAdminOwner = undefined;
	var stepsList = undefined;

	function getIsAvailable(){
		return isAvailable;
	};
	
	function setIsAvailable(newIsAvailable){
		isAvailable = newIsAvailable;
	};

	function getDescription(){
		return description;
	};
	
	function setDescription(newDescription){
		description = newDescription;
	};	

	function getPublicationDate(){
		return publicationDate;
	};
	
	function setPublicationDate(newPublicationDate){
		publicationDate = newPublicationDate;
	};

	function getClosingDate(){
		return closingDate;
	};
	
	function setClosingDate(newClosingDate){
		closingDate = newClosingDate;
	};

	function getName(){
		return name;
	};
	
	function setName(newName){
		name = newName;
	};

	function getTotalSteps(){
		return totalSteps;
	};
	
	function setTotalSteps(newTotalSteps){
		totalSteps = newTotalSteps;
	};
	
	
	//IdAdminOwner
	function getIdAdminOwner(){
		return idAdminOwner;
	};
	
	function setIdAdminOwner(newIdAdminOwner){
		idAdminOwner = newIdAdminOwner;
	};

	function getStepsList(){
		return stepsList;
	};
	
	function setStepsList(newStepsList){
		stepsList = newStepsList;
	};

	// Ritorno solo ciò che voglio rendere pubblico
	return{
		getIsAvailable: getIsAvailable,
		setIsAvailable: setIsAvailable,
		getDescription: getDescription,
		setDescription: setDescription,
		getPublicationDate: getPublicationDate,
		setPublicationDate: setPublicationDate,
		getClosingDate: getClosingDate,
		setClosingDate: setClosingDate,
		getName: getName,
		setName: setName,
		getTotalSteps: getTotalSteps,
		setTotalSteps: setTotalSteps,
		getIdAdminOwner: getIdAdminOwner,
		setIdAdminOwner: setIdAdminOwner,
		getStepsList: getStepsList,
		setStepsList: setStepsList
	};
};
