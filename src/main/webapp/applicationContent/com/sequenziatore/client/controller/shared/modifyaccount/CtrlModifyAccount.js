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
 * Name : CtrlModifyAccount.js
 * Module : CtrlModifyAccount
 * Location : /applicationContent/com/sequenziatore/client/controller/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-05     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-11     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiornamento controlli email e username
 * ======================================================
 */

var application = angular.module('CtrlModifyAccount',['LocationManager','StandardUser','StandardUserRequestManager','AdminUser','AdminUserRequestManager']);

application.controller('CtrlModifyAccount',['$scope','LocationManager','StandardUser','StandardUserRequestManager','AdminUser','AdminUserRequestManager','RegularExpressions','DistrictList',
                                          function($scope,LocationManager,StandardUser,StandardUserRequestManager,AdminUser,AdminUserRequestManager,RegularExpressions,DistrictList){

	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleModifyAccountLocation();

    $scope.closeAlert = function(){
        $scope.showAlertS = 'false';
        $scope.showAlertD = 'false';
    };

    $scope.closeAlert();
	$scope.districtList = DistrictList.DistrictList;
	$scope.selectedDistrict = new Object();
	if(LocationManager.isLogged() === 'true'){

		if(LocationManager.isAdmin() === 'false'){

			StandardUserRequestManager.getUserInfo($scope);
			
			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
				if(result == 'connectionError'){
					 $scope.showAlertS = true;
	                 $scope.messageInfoResult = 'Problemi di connessione al server, contattare l\'amministratore';
	                 $scope.scrollOnTop();
				}
				else{
					$scope.openWebSocket("/seq/Sequenziatore/pushservice");
					$scope.username = StandardUser.getUsername();
					$scope.name = StandardUser.getName();
					$scope.surname = StandardUser.getSurname();
					$scope.email = StandardUser.getEmail();
					$scope.isAdmin = StandardUser.getIsAdmin();
					$scope.district = StandardUser.getDistrict();
					$scope.city = StandardUser.getCity();
					$scope.user = StandardUser.getName();
					$scope.facebook = LocationManager.getFacebookCookie();	
				}
	
			});

			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				alert(result);
			});
		}
		
		if(LocationManager.isAdmin() === 'true'){

			AdminUserRequestManager.getAdminInfo($scope);
			
			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
				if(result == 'connectionError'){
					 $scope.showAlertS = true;
	                 $scope.messageInfoResult = 'Problemi di connessione al server, contattare l\'amministratore';
	                 $scope.scrollOnTop();
				}
				else{
					$scope.openWebSocket("/seq/Sequenziatore/pushservice");
					$scope.username = AdminUser.getUsername();
					$scope.name = AdminUser.getName();
					$scope.surname = AdminUser.getSurname();
					$scope.email = AdminUser.getEmail();
					$scope.isAdmin = AdminUser.getIsAdmin();
					$scope.district = AdminUser.getDistrict();
					$scope.city = AdminUser.getCity();
					$scope.user = AdminUser.getName();
				}
			});
				
			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				alert(result);
			});
		}
	}
	
	$scope.doModifyAccount = function(){
		try{
            $scope.closeAlert();
            $scope.checkUsername();
			$scope.checkEmail();
            $scope.checkPassword();
            $scope.checkName();
			$scope.checkSurname();
			
			var dataToSend = undefined;
			
			if(($scope.password === undefined || $scope.password === '') &&
					$scope.oldPassword === undefined || $scope.oldPassword === ''){
				dataToSend = {
						'Username': $scope.username,
						'Email': $scope.email,
						'Name': $scope.name,
						'Surname': $scope.surname,
						'City': $scope.city,
						'District': $scope.selectedDistrict['abbrev'] ||$scope.district,
						'IsAdmin': new Boolean(LocationManager.isAdmin() === 'true')
				};
			}
			else{
				dataToSend = {
						'Username': $scope.username,
						'Password': $scope.password,
						'Password2': $scope.oldPassword,
						'Email': $scope.email,
						'Name': $scope.name,
						'Surname': $scope.surname,
						'City': $scope.city,
						'District': $scope.selectedDistrict['abbrev']|| $scope.district,
						'IsAdmin': new Boolean(LocationManager.isAdmin() === 'true')
				};
			}
			
			StandardUserRequestManager.doModifyAccount($scope,dataToSend);
			$scope.$on('MODIFY_ACCOUNT_IS_END',function(event,result){
				$scope.messageInfoResult = result;
                if(result === 'Username inserito già presente'){
                    $scope.messageError = result;
                    $scope.showAlertD = 'true';
                }
                else{
                    $scope.messageError = '';
                    $scope.showAlertS = 'true';
                }
                $scope.scrollOnTop();
                if(LocationManager.isAdmin() === 'false'){
                    StandardUserRequestManager.getUserInfo($scope);
                }
                else{
                	AdminUserRequestManager.getAdminInfo($scope);	
                }
			});
		}
		catch(error){
			$scope.messageInfoResult = '';
			$scope.messageError = error;
            $scope.showAlertD = 'true';
            $scope.scrollOnTop();
		}
	};

	$scope.checkUsername = function(){
		try{

			if(($scope.username === undefined || $scope.username === '')){
				throw 'Username obbligatorio';
			}

			if($scope.username.length < 4){
				$scope.errorUsername = 'Lo username deve essere compreso tra 4 e 10 caratteri';
				$scope.okUsername = '';
				throw 'Errore inserimento username';
			}
			else{
				if(!RegularExpressions.regularExpressionUsername.test($scope.username) &&
						!RegularExpressions.regularExpressionEmail.test($scope.username)){
					$scope.errorUsername = "L'username non può contenere caratteri speciali";
					$scope.okUsername = '';
					throw 'L\'username non può contenere caratteri speciali';
				}
				else{
					$scope.errorUsername = '';
				}
			}
		}
		catch(error){
			throw error;
		}
	};

	$scope.checkPassword = function(){
		try{
			// L'utente ha inserito un input di lunghezza minore di 6 caratteri
			if(!($scope.password === undefined || $scope.password === '') && $scope.password.length < 6){
				$scope.errorPassword = 'La password deve essere compresa tra 6 e 12 caratteri';
				$scope.okPassword = '';
				throw 'La password deve essere compresa tra 6 e 12 caratteri';
			}
			else{
				// Le due password inserite non coincidono
				if($scope.password !== $scope.reinsertedPassword){
					
					// L'utente non ha inserito alcun input nel second input password
					if($scope.reinsertedPassword === undefined){
						$scope.errorPassword = 'Per sicurezza reinserisci la password';
						$scope.okPassword = '';
						throw 'Per sicurezza reinserisci la password';
					}
					else{
						$scope.errorPassword = 'Le password non coincidono';
						$scope.okPassword = '';
						throw 'Le password non coincidono';
					}
				}
				else{
					$scope.errorPassword = '';
				}
			}

			// L'utente ha inserito un input di lunghezza minore di 6 caratteri
			if(!($scope.oldPassword === undefined || $scope.oldPassword === '') && $scope.oldPassword.length < 6){
				$scope.errorPassword = 'La password deve avere almeno 6 caratteri';
				$scope.okPassword = '';
				throw 'Errore inserimento password';
			}

			if(($scope.oldPassword === undefined || $scope.oldPassword === '') &&
					!($scope.password === undefined || $scope.password === '')){

				throw 'Vecchia password non inserita';
			}

			if(!($scope.oldPassword === undefined || $scope.oldPassword === '') &&
                    ($scope.password === undefined || $scope.password === '')){

                throw 'Nuova password non inserita';
			}
		}
		catch(error){
			throw error;
		}
	};

	$scope.checkEmail = function(){
		try{
			// L'utente non ha inserito alcun input
			if(($scope.email === undefined || $scope.email === '')){
				throw 'Email obbligatoria';
			}
	
			if(!RegularExpressions.regularExpressionEmail.test($scope.email)){
				$scope.errorEmail = "L'email non rispetta il pattern";
				$scope.okEmail = '';
				throw 'Errore inserimento email';
			}
			else{
				$scope.errorEmail = '';
			}
		}
		catch(error){
			throw error;
		}
	};

	$scope.checkName = function(){
		try{
			if(!$scope.name === undefined){
				if(!RegularExpressions.regularExpressionName.test($scope.name)){
					$scope.errorName = 'Nome non valido';
					$scope.okName = '';
					throw 'Errore inserimento nome';
				}
				else{
					$scope.errorName = '';
				}
			}
		}
		catch(error){
			throw error;
		}
	};

	$scope.checkSurname = function(){
		try{
			if(!$scope.surname === undefined){
				if(!RegularExpressions.regularExpressionSurname.test($scope.surname)){
					$scope.errorSurname = 'Cognome non valido';
					$scope.okSurname = '';
					throw 'Errore inserimento cognome';
				}
				else{
					$scope.errorSurname = '';
				}
			}
		}
		catch(error){
			throw error;
		}
	};
}]);