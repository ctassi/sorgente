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
 * Name : RequestManager.js
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

var RequestManager = function(){

    // URL developer, da sostituire con l'indirizzo del server di pubblicazione
	var requestBaseURL = 'http://localhost:8080/seq/Sequenziatore';

	var requestURL = requestBaseURL;
	var requestMethod = 'POST';
	var requestHeader = undefined;
	var requestData = undefined;

	function getRequestBaseURL(){
		return requestBaseURL;
	};
	
	function setRequestBaseURL(newBaseURL){
		requestBaseURL = newBaseURL;
	};

	function getRequestURL(){
		return requestURL;
	};
	
	function setRequestURL(newRequestURL){
		requestURL = newRequestURL;
	}

	function getRequestMethod(){
		return requestMethod;
	};
	
	function setRequestMethod(newRequestMethod){
		requestMethod = newRequestMethod;
	};

	function getRequestHeader(){
		return requestHeader;
	}
	
	function setRequestHeader(newRequestHeader){
		requestHeader = newRequestHeader;
	}

	function getRequestData(){
		return requestData;
	}
	
	function setRequestData(newRequestData){
		requestData = newRequestData;
	};

	function makeJSONObject(){
		return{
			url: requestURL,
			method: requestMethod,
			headers: requestHeader,
			data: requestData
		};
	};
	
	// Ritorno solo ciò che voglio rendere pubblico
	return{
		getRequestBaseURL: getRequestBaseURL,
		setRequestBaseURL: setRequestBaseURL,
		getRequestURL: getRequestURL,
		setRequestURL: setRequestURL,
		getRequestMethod: getRequestMethod,
		setRequestMethod: setRequestMethod,
		getRequestHeader: getRequestHeader,
		setRequestHeader: setRequestHeader,
		getRequestData: getRequestData,
		setRequestData: setRequestData,
		makeJSONObject: makeJSONObject
	};
};