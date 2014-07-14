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
 * Name : GenericStep.js
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

var GenericStep = function(){

	var idStep = undefined;
	var adminVerify= undefined;
	var description = undefined;
	var isGeolocation = undefined;
	var isPhoto = undefined;
	var isText = undefined;
	var level = undefined;
	var parallelism = undefined;
	var state = undefined;
	var wrongText = undefined;
	var wrongGeolocation = undefined;
	var wrongPhoto = undefined;

	function reset(){
		adminVerify= undefined;
		description = undefined;
		isGeolocation = undefined;
		isPhoto = undefined;
		isText = undefined;
		level = undefined;
		parallelism = undefined;
		state = undefined;
		wrongText = undefined;
		wrongGeolocation = undefined;
		wrongPhoto = undefined;
	};

	function getAdminVerify(){
		return adminVerify;
	};
	
	function setAdminVerify(newAdminVerify){
		adminVerify = newAdminVerify;
	};

	function getDescription(){
		return description;
	};
	
	function setDescription(newDescription){
		description = newDescription;
	};

	function getIsGeolocation(){
		return isGeolocation;
	};
	
	function setIsGeolocation(newIsGeolocation){
		isGeolocation = newIsGeolocation;
	};

	function getIsPhoto(){
		return isPhoto;
	};
	
	function setIsPhoto(newIsPhoto){
		isPhoto = newIsPhoto;
	};

	function getIsText(){
		return isText;
	};
	
	function setIsText(newIsText){
		isText = newIsText;
	};

	function getLevel(){
		return level;
	};
	
	function setLevel(newLevel){
		level = newLevel;
	};

	function getParallelism(){
		return parallelism;
	};
	
	function setParallelism(newParallelism){
		parallelism = newParallelism;
	};

	function getState(){
		return state;
	};
	
	function setState(newState){
		state = newState;
	};

	function getIdStep(){
		return idStep;
	};
	
	function setIdStep(newIdStep){
		idStep = newIdStep;
	};

	function getWrongText(){
		return wrongText;
	};
	
	function setWrongText(newWrongText){
		wrongText = newWrongText;
	};

	function getWrongGeolocation(){
		return wrongGeolocation;
	};
	
	function setWrongGeolocation(newWrongGeolocation){
		wrongGeolocation = newWrongGeolocation;
	};

	function getWrongPhoto(){
		return wrongPhoto;
	};
	
	function setWrongPhoto(newWrongPhoto){
		wrongPhoto = newWrongPhoto;
	};

	// Ritorno solo ciò che voglio rendere pubblico
	return{
		getAdminVerify: getAdminVerify,
		setAdminVerify: setAdminVerify,
		getDescription: getDescription,
		setDescription: setDescription,
		getIsGeolocation: getIsGeolocation,
		setIsGeolocation: setIsGeolocation,
		getIsPhoto: getIsPhoto,
		setIsPhoto: setIsPhoto,
		getIsText: getIsText,
		setIsText: setIsText,
		getLevel: getLevel,
		setLevel: setLevel,
		getParallelism: getParallelism,
		setParallelism: setParallelism,
		getState: getState,
		setState: setState,
		getIdStep: getIdStep,
		setIdStep: setIdStep,
		getWrongText: getWrongText,
		setWrongText: setWrongText,
		getWrongPhoto: getWrongPhoto,
		setWrongPhoto: setWrongPhoto,
		getWrongGeolocation: getWrongGeolocation,
		setWrongGeolocation: setWrongGeolocation,
		reset: reset
	};
};