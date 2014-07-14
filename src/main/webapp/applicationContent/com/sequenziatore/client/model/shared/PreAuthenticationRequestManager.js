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
 * Name : PreAuthenticationRequestManager.js
 * Module : PreAuthenticationRequestManager
 * Location : /applicationContent/com/sequenziatore/client/model/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-19     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-03-20     Pavanello Mirko
 * ------------------------------------------------------
 * Aggiunta gestione errori recovery password
 * ======================================================
 */

var application = angular.module('PreAuthenticationRequestManager',['ngCookies','LocationManager','StandardUser','AdminUser']);

application.factory('PreAuthenticationRequestManager',['$http','LocationManager','StandardUser','AdminUser',
                                                       function($http,LocationManager,StandardUser,AdminUser){

	var PreAuthenticationRequestManager = function(){
		
		var preAuthenticationRequestManager = {};
		
		// Memorizzo il sotto oggetto della superclasse
		preAuthenticationRequestManager.__proto__ = RequestManager();
		
		// Metodo che ritorna tutti i processi a cui un utente sta partecipando
		preAuthenticationRequestManager.doRegistration = function(scope,dataToSend){

			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			preAuthenticationRequestManager.setRequestURL(preAuthenticationRequestManager.getRequestBaseURL()+ '/shared/userregistration');
			preAuthenticationRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			preAuthenticationRequestManager.setRequestData(dataToSend);
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = preAuthenticationRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('REQUEST_REGISTRATION_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'registrationOK'){					 
					scope.$broadcast('REQUEST_REGISTRATION_IS_END', 'Username registrato correttamente');
					
					if(dataToSend['IsAdmin'] === false){
						preAuthenticationRequestManager.doLogin(scope,dataToSend);
					}
					
				}
				else if(requestResult['Confirmation'] === 'userAndMailNotAvailable'){
					scope.$broadcast('REQUEST_REGISTRATION_IS_END', "Username ed email già presenti");
				}
				else if(requestResult['Confirmation'] === 'usernameNotAvailable'){
					scope.$broadcast('REQUEST_REGISTRATION_IS_END', "Username già presente");
				}
				else if(requestResult['Confirmation'] === 'emailNotAvailable'){
					scope.$broadcast('REQUEST_REGISTRATION_IS_END', "Email già presente");
				}
			
				
			}).
			error(function(data, status, headers, config) {
				alert('Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		      });
		};

        // Metodo che ritorna tutti i processi a cui un utente sta partecipando
        preAuthenticationRequestManager.doFacebookLogin = function(scope,dataToSend){
             var JSONObjectRequest = undefined;

            // Preparo Indirizzo, Header e Contenuto della HTTP Request
            preAuthenticationRequestManager.setRequestURL(preAuthenticationRequestManager.getRequestBaseURL()+ '/shared/facebooklogin');
            preAuthenticationRequestManager.setRequestHeader({'Content-Type': 'application/json'});
            preAuthenticationRequestManager.setRequestData(dataToSend);

            // Creo un JSONObject con i dati necessari per la HTTP Request
            JSONObjectRequest = preAuthenticationRequestManager.makeJSONObject();
            $http(JSONObjectRequest).success(function (requestResult){

                if(requestResult['Confirmation'] === 'connectionError'){
                    scope.$broadcast('REQUEST_LOGIN_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
                }
                else if(requestResult['Confirmation'] === 'wrongAuthentication'){
                    scope.$broadcast('REQUEST_LOGIN_IS_END', 'Username o Password inseriti non corretti');
                }
                //Se l'utente inserito è corretto
                else if(requestResult['Confirmation'] === 'successAuthentication'){

                    LocationManager.setCookie(requestResult["Username"],requestResult["IsAdmin"],requestResult["IdUser"],true);
                    LocationManager.handleLoginLocation(requestResult["Username"]);

                    if(requestResult["IsAdmin"] === false){
                        StandardUser.build(requestResult);
                    }
                    if(requestResult["IsAdmin"] === true){
                        AdminUser.build(requestResult);
                    }
                }
            }).
                error(function(data, status, headers, config) {
                    alert('Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
                });
        };

		// Metodo che serve ad ottenere le informazioni di un utente
		preAuthenticationRequestManager.doLogin = function(scope,dataToSend){
			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			preAuthenticationRequestManager.setRequestURL(preAuthenticationRequestManager.getRequestBaseURL() + '/shared/login');
			preAuthenticationRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			preAuthenticationRequestManager.setRequestData(dataToSend);

			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = preAuthenticationRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){

				if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('REQUEST_LOGIN_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
				else if(requestResult['Confirmation'] === 'wrongAuthentication'){
					scope.$broadcast('REQUEST_LOGIN_IS_END', 'Username o Password inseriti non corretti');
				}
				// Se l'utente inserito è corretto
				else if(requestResult['Confirmation'] === 'successAuthentication'){
					
					LocationManager.setCookie(requestResult["Username"],requestResult["IsAdmin"],requestResult["IdUser"],false);
					LocationManager.handleLoginLocation(requestResult["Username"]);
					
					if(requestResult["IsAdmin"] === false){
						StandardUser.build(requestResult);

					}
					
					if(requestResult["IsAdmin"] === true){
						AdminUser.build(requestResult);
					}
				}
			}).
			error(function(data, status, headers, config) {
				alert('Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		      });		
		};

		// Metodo che ritorna tutti i processi a cui un utente sta partecipando
		preAuthenticationRequestManager.doRecoveryPassword = function(scope,dataToSend){

			var JSONObjectRequest = undefined;

			// Preparo Indirizzo, Header e Contenuto della HTTP Request
			preAuthenticationRequestManager.setRequestURL(preAuthenticationRequestManager.getRequestBaseURL()+ '/shared/recoverypassword');
			preAuthenticationRequestManager.setRequestHeader({'Content-Type': 'application/json'});
			preAuthenticationRequestManager.setRequestData(dataToSend);
			
			// Creo un JSONObject con i dati necessari per la HTTP Request
			JSONObjectRequest = preAuthenticationRequestManager.makeJSONObject();
			
			$http(JSONObjectRequest).success(function (requestResult){
				if(requestResult['Confirmation'] === 'successRecovery'){
					scope.$broadcast('REQUEST_RECOVERY_PASSWORD_IS_END', 'Richiesta di reset password inviata con successo. Controlla la tua casella mail');
				}
				else if(requestResult['Confirmation'] === 'wrongRecovery'){
					scope.$broadcast('REQUEST_RECOVERY_PASSWORD_IS_END', 'Utente non trovato, controllare lo username inserito');
				}
				else if(requestResult['Confirmation'] === 'internetConnectionError'){
					scope.$broadcast('REQUEST_RECOVERY_PASSWORD_IS_END', 'Errore nell\'invio dell\'e-mail con la nuova password, riprovare più tardi');
				}
				else if(requestResult['Confirmation'] === 'connectionError'){
					scope.$broadcast('REQUEST_RECOVERY_PASSWORD_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
				}
			}).
			error(function(data, status, headers, config) {
				alert('Problemi di connessione. Assicurati di essere connesso ad internet e riprova');
		      });	
		};
		
		return preAuthenticationRequestManager;
	};
	
	return PreAuthenticationRequestManager();
}]);