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
 * Name : StandardUserRequestManager.js
 * Module : StandardUserRequestManager
 * Location : /applicationContent/com/sequenziatore/client/model/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-18     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-03-21     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiunta gestione errori di connessione
 * ======================================================
 * 0.0.3           2014-03-22     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiornato metodo statistiche utente
 * ======================================================
 */

var application = angular.module('StandardUserRequestManager',['ngCookies','LocationManager','StandardUser','angularFileUpload']);

application.factory('StandardUserRequestManager',['$http','LocationManager','StandardUser','$upload',
                                                  function($http,LocationManager,StandardUser,$upload){

	var StandardUserRequestManager = function(){

		var standardUserRequestManager = {};

		// Memorizzo il sotto oggetto della superclasse
		standardUserRequestManager.__proto__ = RequestManager();

		// Metodo che ritorna tutti i processi a cui un utente sta partecipando
		standardUserRequestManager.getAllActiveProcessList = function(scope){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL()+ '/user/viewactiveprocesses');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){

				 if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('REQUEST_ACTIVE_PROCESS_LIST_IS_END', 'connectionError');
				 }
				 else if(requestResult['Confirmation'] === 'noneSubscriptions'){
					 StandardUser.setProcessList(requestResult['ListSubscriptions']);
					scope.$broadcast('REQUEST_ACTIVE_PROCESS_LIST_IS_END', 'Non sei iscritto ad alcun processo');
				 }
				 else if(requestResult['Confirmation'] === 'subscriptions'){
					StandardUser.setProcessList(requestResult['ListSubscriptions']);
					scope.$broadcast('REQUEST_ACTIVE_PROCESS_LIST_IS_END','Ricevuta la lista processi');
				 }
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};

		// Metodo che serve ad eseguire una query per ottenere le info di un utente
		standardUserRequestManager.getUserInfo = function(scope){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/shared/viewaccount');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){

				 if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('REQUEST_INFO_IS_END', 'connectionError');
				 }
				 else if(requestResult['Confirmation'] === 'notReceivedInfo'){
					scope.$broadcast('REQUEST_INFO_IS_END', 'Informazio account non ricevute');
				 }
				 else if(requestResult['Confirmation'] === 'receivedInfo'){
					StandardUser.build(requestResult);
					scope.$broadcast('REQUEST_INFO_IS_END','Informazioni account ricevute correttamente');
				 }
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });	
		};

		// Metodo che serve ad eseguire il logout di un utente normale
		standardUserRequestManager.doLogout = function(){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/shared/logout');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData({'IdUser':LocationManager.getUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){

			});
		};


		standardUserRequestManager.getAllAvailableProcessList = function(scope){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/user/availableprocess');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('REQUEST_AVAILABLE_PROCESS_IS_END', 'connectionError');
				}
				else if(requestResult['Confirmation'] === 'noneProcesses'){
					scope.$broadcast('REQUEST_AVAILABLE_PROCESS_IS_END', 'Non ci sono processi disponibili');
				}
				else if(requestResult['Confirmation'] === 'processes'){
					StandardUser.setProcessList(requestResult['ListProcesses']);
					scope.$broadcast('REQUEST_AVAILABLE_PROCESS_IS_END', 'Lista processi ricevuta correttamente');
				}
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });	
		};
		
		// Metodo che iscrive un utente ad un processo
		standardUserRequestManager.doSubscribeProcess = function(scope,idProcess){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/user/subscribetoprocess');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData({"IdUser": LocationManager.getIdUserCookie(),
														'IdProcess': idProcess});

			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				if(requestResult['Confirmation'] === 'connectionError'){
                    var obj = {
                        msg: "Problemi di connessione al server, contattare l\'amministratore'",
                        success: 'false'
                    };
					scope.$broadcast('REQUEST_SUBSCRIBE_PROCESS_IS_END', obj);
				}
				else if(requestResult['Confirmation'] === 'wrongSubscription'){
                    var obj = {
                        msg: 'Spiacenti, si è verificato un errore',
                        success: 'false'
                    };
					scope.$broadcast('REQUEST_SUBSCRIBE_PROCESS_IS_END', obj);
				}
				else if(requestResult['Confirmation'] === 'successSubscription'){
                    var obj = {
                        msg: 'Iscrizione al processo avvenuta con successo',
                        success: 'true'
                    };
					scope.$broadcast('REQUEST_SUBSCRIBE_PROCESS_IS_END', obj);
				}
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};
		
		
		// Metodo che iscrive un utente ad un processo
		standardUserRequestManager.doGetNextStep = function(scope,idProcess){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/user/viewstep');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData({'IdUser': LocationManager.getIdUserCookie(),
														'IdProcess': idProcess});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			$http(JSONObjectRequest).success(function (requestResult){
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('REQUEST_GET_NEXT_STEP_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'step'){
					StandardUser.setStepList(requestResult['StepList']);
					scope.$broadcast('REQUEST_GET_NEXT_STEP_IS_END', requestResult);
				}

			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};
		
		// Metodo che invia al server i dati di un passo
		standardUserRequestManager.doSendUserStep = function(scope,dataStep){
			var JSONObjectRequest = undefined;

			dataStep['IdUser'] = LocationManager.getIdUserCookie();
	
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/user/savedatastep');

			if(typeof dataStep['Photo'] =='string' || String(dataStep['WrongPhoto']) === 'true' || dataStep['Photo'] === undefined){

				standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});

				standardUserRequestManager.setRequestData(dataStep);
				
				// Creo un JSONObject con i dati necessari per la HTTP Request
				JSONObjectRequest = standardUserRequestManager.makeJSONObject();
				
				$http(JSONObjectRequest).success(function (requestResult){
			
					if(requestResult['Confirmation'] === 'connectionError'){
						scope.$broadcast('SEND_USER_STEP_IS_END', 'connectionError');
					}
					else if(requestResult['Confirmation'] === 'notSendData'){
						scope.$broadcast('SEND_USER_STEP_IS_END', 'Dati non inviati');
					}
					else if(requestResult['Confirmation'] === 'successSendData'){
			    		scope.$broadcast('SEND_USER_STEP_IS_END', 'successSendData');
			    	}
					else if(requestResult['Confirmation'] === 'successAutoVerify'){
						scope.$broadcast('SEND_USER_STEP_IS_END', 'successAutoVerify');
					}
					else if(requestResult['Confirmation'] === 'notSuccessAutoVerify'){
						scope.$broadcast('SEND_USER_STEP_IS_END', 'notSuccessAutoVerify');
					}
			    	else if(requestResult['Confirmation'] === 'processNotActive'){
			    		  scope.$broadcast('SEND_USER_STEP_IS_END', 'processNotActive');
			    	}
				}).
				error(function(data, status, headers, config) {
					scope.$broadcast('CONNECTION_PROBLEM', dataStep);
			      });
			}
			else{
				$upload.upload({
                    url: standardUserRequestManager.getRequestBaseURL() + '/user/savedatastep',
                    headers: {'Content-Type': 'multipart/form-data'},
                    data: dataStep,
                    file: dataStep['Photo']
                    }).success(function(requestResult, status, headers, config) {

                      if(requestResult['Confirmation'] === 'connectionError'){
                          scope.$broadcast('SEND_USER_STEP_IS_END', 'connectionError');
                      }
                      else if(requestResult['Confirmation'] === 'notSendData'){
                          scope.$broadcast('SEND_USER_STEP_IS_END', 'Dati non inviati');
                      }
                      else if(requestResult['Confirmation'] === 'successSendData'){
                          scope.$broadcast('SEND_USER_STEP_IS_END', 'successSendData');
                      }
                      else if(requestResult['Confirmation'] === 'successAutoVerify'){
                          scope.$broadcast('SEND_USER_STEP_IS_END', 'successAutoVerify');
                      }
                      else if(requestResult['Confirmation'] === 'notSuccessAutoVerify'){
                          scope.$broadcast('SEND_USER_STEP_IS_END', 'notSuccessAutoVerify');
                      }
                      else if(requestResult['Confirmation'] === 'processNotActive'){
                          scope.$broadcast('SEND_USER_STEP_IS_END', 'processNotActive');
                      }
                    }).
                    error(function(data, status, headers, config) {
                        scope.$broadcast('CONNECTION_PROBLEM', dataStep);
                    });
			}
		};

		// Metodo che invia le modifiche dell'account di uno standard user
		standardUserRequestManager.doModifyAccount = function(scope,dataAccount){
			var JSONObjectRequest = undefined;
			
			dataAccount['IdUser'] = LocationManager.getIdUserCookie();
			
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/shared/modifyaccount');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData(dataAccount);
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			
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
		
		// Metodo che invia le modifiche dell'account di uno standard user
		standardUserRequestManager.doUnsubscribeProcess = function(scope,idProcess){

			var JSONObjectRequest = undefined;
			var dataToSend = new Object();

			dataToSend['IdUser'] = LocationManager.getIdUserCookie();
			dataToSend['IdProcess'] = idProcess;
			
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/user/unsubscribeprocess');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData(dataToSend);
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){

				if(requestResult['Confirmation'] === 'connectionError'){
		    		  scope.$broadcast('UNSUBSCRIBE_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
		    	}
				else if(requestResult['Confirmation'] === 'successUnsubscribe'){
					scope.$broadcast('UNSUBSCRIBE_PROCESS_IS_END', "Disiscrizione non avvenuta con successo");
				}
				else if(requestResult['Confirmation'] === 'notSuccessUnsubscribe'){
					scope.$broadcast('UNSUBSCRIBE_PROCESS_IS_END', "Disiscrizione avvenuta con successo");
				}

			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });	
		};
		
		// Metodo che invia ottiene le statistiche di un utente
		standardUserRequestManager.getStatisticsUser = function(scope){
			var JSONObjectRequest = undefined;
			
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/user/statisticsuser');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData({'IdUser': LocationManager.getIdUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){

				if(requestResult['Confirmation'] === 'connectionError'){
		    		  scope.$broadcast('GET_USER_STATISTICS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
		    	}
				else if(requestResult['Confirmation'] === 'receivedStatistics'){
					scope.$broadcast('GET_USER_STATISTICS_IS_END', requestResult);
				}
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });	
		};
		
		// Metodo che invia ottiene le statistiche di un utente
		standardUserRequestManager.getCloseProcess = function(scope){
			var JSONObjectRequest = undefined;
			
			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			standardUserRequestManager.setRequestURL(standardUserRequestManager.getRequestBaseURL() + '/user/viewprocessnotactive');
			standardUserRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			standardUserRequestManager.setRequestData({'IdUser': LocationManager.getIdUserCookie()});
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = standardUserRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){

				if(requestResult['Confirmation'] === 'connectionError'){
		    		  scope.$broadcast('GET_CLOSED_PROCESS_IS_END', 'connectionError');
		    	}
				else if(requestResult['Confirmation'] === 'receivedCloseProcess'){
					scope.$broadcast('GET_CLOSED_PROCESS_IS_END', requestResult['ListProcess']);
				}
				else if(requestResult['Confirmation'] === 'notReceivedCloseProcess'){
					scope.$broadcast('GET_CLOSED_PROCESS_IS_END', 'Non ci sono processi chiusi');
				}
			}).
			error(function(data, status, headers, config) {
				scope.$broadcast('CONNECTION_PROBLEM', 'Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		    });		
		};
		return standardUserRequestManager;
	};
	return StandardUserRequestManager();
}]);
