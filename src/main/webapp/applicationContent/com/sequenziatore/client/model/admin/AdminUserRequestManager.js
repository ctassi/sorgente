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
 * Name : AdminUserRequestManager.js
 * Module : AdminUserRequestManager
 * Location : /applicationContent/com/sequenziatore/client/model/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-24     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-03-28     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiunta gestione errori di connessione
 * ======================================================
 */

var application = angular.module('AdminUserRequestManager',['ngCookies','LocationManager','AdminUser']);

application.factory('AdminUserRequestManager',['$http','LocationManager','AdminUser',
                                                  function($http,LocationManager,AdminUser){

var AdminUserRequestManager = function() {

		var adminUserRequestManager = {};
		
		// Memorizzo il sotto oggetto della superclasse
		adminUserRequestManager.__proto__ = RequestManager();
				
		// Metodo che serve ad eseguire una query per ottenere le info di un utente
		adminUserRequestManager.getAdminInfo = function(scope) {
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/shared/viewaccount');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult) {
				 if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('REQUEST_INFO_IS_END', 'connectionError');
				 }
				 else if(requestResult['Confirmation'] === 'notReceivedInfo'){
					scope.$broadcast('REQUEST_INFO_IS_END', 'Informazioni account non ricevute');
				 }
				 else if(requestResult['Confirmation'] === 'receivedInfo'){
					AdminUser.build(requestResult);
					scope.$broadcast('REQUEST_INFO_IS_END','Informazioni account ricevute correttamente');
				 }

			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });				
		};
		
		// Metodo che serve ad eseguire il logout di un utente
		adminUserRequestManager.doLogout = function(){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/shared/logout');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData({'IdUser':LocationManager.getUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult) {
				// Operazioni di pulizia
			});
		};
		
		// Metodo che ritorna la lista di tutti i processi creati dall'admin correntemente loggato
		adminUserRequestManager.getAllAdminCreatedActiveProcessList = function(scope){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/viewadminprocessactive');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('GET_ALL_ADMIN_CREATED_ACTIVE_PROCESS_IS_END', 'connectionError');
				}
				else if(requestResult['Confirmation'] === 'noneActiveProcess'){
					scope.$broadcast('GET_ALL_ADMIN_CREATED_ACTIVE_PROCESS_IS_END', 'Non ci sono processi attivi');
				}
				else if(requestResult['Confirmation'] === 'activeProcess'){
					AdminUser.setProcessList(requestResult['ListProcess']);
					scope.$broadcast('GET_ALL_ADMIN_CREATED_ACTIVE_PROCESS_IS_END','Processi attivi trovati correttamente');
				}
				
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};
		
		// Metodo che ritorna la lista di tutti i processi che necessitano di validazione
		adminUserRequestManager.getAllStepsToBeValidatedList = function(scope){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/steplistvalidation');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult) {
				
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('GET_ALL_STEPS_TO_BE_VALIDATED_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'steps'){
					AdminUser.setStepList(requestResult['Collections']);
					scope.$broadcast('GET_ALL_STEPS_TO_BE_VALIDATED_IS_END', 'Passi da validare ricevuti correttamente');
				}
				
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};

		// Metodo che ritorna la lista di tutti gli utenti che partecipano un processo dato
		adminUserRequestManager.getStatisticsAdmin = function(scope) {
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/statisticsadmin');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData({'IdUser': LocationManager.getIdUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('GET_ADMIN_STATISTICS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'receivedStatistics'){
					scope.$broadcast('GET_ADMIN_STATISTICS_IS_END', requestResult);
				}

			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });	
		
		};
		
		// Metodo che ritorna la lista di tutti gli utenti che partecipano un processo dato
		adminUserRequestManager.getListUserProcess = function(scope,idProcess) {
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/listuserprocess');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie(), 
													"IdProcess": idProcess});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){

				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('GET_LIST_USER_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'noneUser'){
					scope.$broadcast('GET_LIST_USER_PROCESS_IS_END', 'Non ci sono utenti iscritti');
				
				}
				else if(requestResult['Confirmation'] === 'users'){
					
					AdminUser.setUserList(requestResult['ListUser']);
					scope.$broadcast('GET_LIST_USER_PROCESS_IS_END', requestResult['ListUser']);
				}
			
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });	
		};

		//Metodo che invia le modifiche dell'account di un admin user
		adminUserRequestManager.doModifyAccount = function(scope,dataAccount){
			var JSONObjectRequest = undefined;
			
			dataAccount['IdUser'] = LocationManager.getIdUserCookie();
			
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/shared/modifyaccount');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
            adminUserRequestManager.setRequestData(dataAccount);
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){

				if(requestResult['Confirmation'] === 'registrationOK'){
					scope.$broadcast('MODIFY_ACCOUNT_IS_END', 'Modifiche apportate correttamente');
				}
				else if(requestResult['Confirmation'] === 'userAndMailNotAvailable'){
					scope.$broadcast('MODIFY_ACCOUNT_IS_END', "Username ed email inseriti già presenti");
				}
				else if(requestResult['Confirmation'] === 'usernameNotAvailable'){
					scope.$broadcast('MODIFY_ACCOUNT_IS_END', "Username inserito già presente");
				}
				else if(requestResult['Confirmation'] === 'emailNotAvailable'){
					scope.$broadcast('MODIFY_ACCOUNT_IS_END', "Email inserito già presente");
				}
				else if(requestResult['Confirmation'] === 'wrongPassword'){
					scope.$broadcast('MODIFY_ACCOUNT_IS_END', "Password inserita non corrisponde alla tua attuale");
				}
				else if(requestResult['Confirmation'] === 'connectionError'){
		    		  scope.$broadcast('MODIFY_ACCOUNT_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
		    	}

			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};
		
		
		// Metodo che modifica un processo
		adminUserRequestManager.doModifyProcess = function(scope,dataProcess){
			var JSONObjectRequest = undefined;
			
			dataProcess['IdUser'] = Number(LocationManager.getIdUserCookie());
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/modifyprocess');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData(dataProcess);
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('MODIFY_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'receivedInfo'){
					scope.$broadcast('MODIFY_PROCESS_IS_END', 'Modifiche inserite correttamente');
				}

			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};

		// Metodo che modifica un processo
		adminUserRequestManager.viewAdminProcessNotActive = function(scope){
			var JSONObjectRequest = undefined;

			var dataToSend = new Object();
			dataToSend['IdUser'] = Number(LocationManager.getIdUserCookie());
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/viewadminprocessnotactive');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData(dataToSend);
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'noneProcess'){
					scope.$broadcast('VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END', 'Non ci sono processi terminati');
				}
				else if(requestResult['Confirmation'] === 'process'){
					scope.$broadcast('VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END', requestResult['ListProcess']);
				}
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};
		
		
		// Metodo che crea un nuovo processo
		adminUserRequestManager.doCreateNewProcess = function(scope,dataProcess){
			dataProcess['IdUser'] = LocationManager.getIdUserCookie();
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/createprocess');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData(dataProcess);

			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('CREATE_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'successCreateProcess'){
					var pDate = dataProcess['PublicationDate'].Day + '/' + dataProcess['PublicationDate'].Month + '/' + dataProcess['PublicationDate'].Year;
					scope.sendNotification("/seq/pushservice", JSON.stringify({
							username: '-1',
							message: 'Un nuovo processo sarà disponibile dal ' + pDate,
							actionType: 'createProcess',
							success: 'true'
						}));
					scope.$broadcast('CREATE_PROCESS_IS_END','Processo creato con successo');
				}
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};
		
		// Metodo che valida un passo
		adminUserRequestManager.doStepValidation = function(scope,dataToSend){
			// Variabili necessarie
			var JSONObjectRequest = undefined;

			dataToSend['IdUser'] =  LocationManager.getIdUserCookie();
			
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/stepvalidation');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData(dataToSend);
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();

			$http(JSONObjectRequest).success(function (requestResult){

				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('VALIDATION_STEP_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'successValidation'){
					 var success = dataToSend.WrongGeolocation && dataToSend.WrongPhoto && dataToSend.WrongText;
	                    
	                    var msg = undefined;
	                    
	                    if(success){
	                    	msg = 'Sei avanzato in ' + dataToSend.processName;
	                    }
	                    if(!success){
	                    	msg = "Hai fallito l'avanzamento in " + dataToSend.processName;
	                    }
	                    scope.sendNotification("/seq/pushservice", JSON.stringify({
	                        username: dataToSend['Username'],
	                        message: msg,
	                        actionType: 'stepValidation',
	                        success: success
	                    }));
					scope.$broadcast('VALIDATION_STEP_IS_END', 'Validazione eseguita');
				}
				else if(requestResult['Confirmation'] === 'wrongValidation'){
					scope.$broadcast('VALIDATION_STEP_IS_END', 'L\'utente si era già disiscritto');
				}
				else if(requestResult['Confirmation'] === 'AlreadyVerified'){
					scope.$broadcast('VALIDATION_STEP_IS_END', 'AlreadyVerified');
				}
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};
		
		adminUserRequestManager.getProcess = function(scope,idProcess){
			// Variabili necessarie
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/viewprocess');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie(), 
													"IdProcess": idProcess});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('GET_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'successGetProcess'){
					
					scope.$broadcast('GET_PROCESS_IS_END', requestResult);
				}				
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};
		
		adminUserRequestManager.getReport = function(scope,idProcess){
			// Variabili necessarie
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			adminUserRequestManager.setRequestURL(adminUserRequestManager.getRequestBaseURL() + '/admin/report');
			adminUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			adminUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie(), 
													"IdProcess": idProcess});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = adminUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				
				
				if(requestResult['Confirmation'] === 'SomebodyIsSubscribed'){
					scope.$broadcast('GET_REPORT_IS_END',requestResult);
				}
				else if(requestResult['Confirmation'] === 'NoOneIsSubscribed'){
					scope.$broadcast('GET_REPORT_IS_END',requestResult);
				}
				else if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('GET_REPORT_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				
				
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};

		return adminUserRequestManager;
		
	};
	
	return AdminUserRequestManager();
}]);
