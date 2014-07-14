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
 * Name : CtrlRegistration.js
 * Module : CtrlRegistration
 * Location : /applicationContent/com/sequenziatore/client/controller/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-14     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-15     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiornamento di alcuni controlli non funzionanti
 * ======================================================
 */

var application = angular.module('CtrlRegistration',['PreAuthenticationRequestManager','LocationManager']);

application.controller('CtrlRegistration',['$scope','PreAuthenticationRequestManager','LocationManager','RegularExpressions','DistrictList',
                                                 function($scope,PreAuthenticationRequestManager,LocationManager,RegularExpressions,DistrictList){

	LocationManager.handleRegistrationLocation();

    $scope.closeAlert = function(){
        $scope.showAlert = 'false';
    };

    $scope.closeAlert();
    $scope.emailOk == false;
    $scope.passwordOk == false;
    $scope.passwordReinsertedOk = false;
    $scope.usernameOk = true;
	$scope.districtList = DistrictList.DistrictList;
	$scope.selectedDistrict = new Object();
	
    //$scope.districtList = DistrictList.DistrictList.districtList;
	// Metodo che invoca la procedura per la registrazione di un utente
	$scope.doRegistration = function(){

		try{
			if($scope.email == undefined || $scope.email.length == 0){
				throw 'Email obbligatoria';
			}
			if($scope.password == undefined || $scope.password.length == 0){
				throw 'Password obbligatoria';
			}
			if($scope.reinsertedPassword == undefined || $scope.reinsertedPassword.length == 0){
				throw 'Password da reinserire obbligatoria';
			}
			if($scope.emailOk == true && $scope.passwordOk == true && 
					$scope.passwordReinsertedOk == true && $scope.usernameOk == true){

				var dataToSend = {
		                'Username': $scope.username,
		                'Password': $scope.password,
		                'Email': $scope.email,
		                'Name': $scope.name,
		                'Surname': $scope.surname,
		                'City': $scope.city,
		                'District': $scope.selectedDistrict['abbrev'],
		                'IsAdmin': false
					};
					
					if($scope.username === undefined ||
							$scope.username === ''){
						dataToSend['Username'] = $scope.email;
					}
					
					if($scope.name === undefined){
						dataToSend['Name'] = '';
					}
						
					if($scope.surname === undefined){
						dataToSend['Surname'] = '';
					}
						
					if($scope.city === undefined){
						dataToSend['City'] = '';
					}
					
					if($scope.selectedDistrict['abbrev'] === undefined){
						dataToSend['District'] = '';
					}

					PreAuthenticationRequestManager.doRegistration($scope,dataToSend);
					//$scope.message = 'Attendi...';
			}
			else{
				if($scope.emailOk == false || $scope.passwordOk == false || $scope.usernameOk == false){
					throw 'Sono presenti degli errori nei dati inserti. Ricontrolla i dati e prova a registrarti nuovamente.';
				}
			}
		}
		catch(error){
			$scope.message = error;
            $scope.showAlert = 'true';
            $scope.scrollOnTop();
		}
	};

	$scope.checkUsername = function(){
		try{
			if($scope.username.length == 0){
				$scope.errorUsername = undefined;
				$scope.usernameOk = false;
			}
			// L'utente ha inserito un input di lunghezza minore di 4 caratteri
			else if(!($scope.username == undefined || $scope.username == '') && $scope.username.length < 4){
				$scope.errorUsername = 'Lo Username deve essere almeno 4 caratteri';
				$scope.usernameOk = false;
			}
			
			// L'input inserito non rispetta il pattern
			else if(!RegularExpressions.regularExpressionUsername.test($scope.username)){
					$scope.errorUsername = 'Lo Username non può contenere caratteri speciali o spazi';
					$scope.usernameOk = false;
			}
				
			else{
				$scope.errorUsername = undefined;
				$scope.usernameOk = true;
			}
		}
		catch(error){
			throw error;
		}
	};

	$scope.checkPassword = function(){
		try{
			// L'utente non ha inserito alcun input
			if(($scope.password === undefined || $scope.password === '')){
				$scope.errorPassword = '';
				$scope.errorReinsertedPassword = '';
				$scope.passwordOk = false;
			}
			
			// L'utente ha inserito un input di lunghezza minore di 6 caratteri
			else if($scope.password.length < 6){
				$scope.errorPassword = 'La Password deve essere compresa tra 6 e 12 caratteri';
				$scope.okPassword = '';
				$scope.passwordOk = false;
			}
			else{
				// Le due password inserite non coincidono
				if($scope.password !== $scope.reinsertedPassword){

					// L'utente non ha inserito alcun input nel second input password
					if($scope.reinsertedPassword === undefined || $scope.reinsertedPassword === ''){
						$scope.errorReinsertedPassword = 'Per sicurezza reinserisci la password';
						$scope.okPassword = '';
						$scope.errorPassword = '';
						$scope.passwordOk = false;
					}
					else{
						$scope.errorReinsertedPassword = 'La Password non coincide';
						$scope.okPassword = '';
						$scope.passwordOk = false;
						$scope.passwordReinsertedOk = false;
					}
				}
				else{
					$scope.errorPassword = '';
					$scope.errorReinsertedPassword = '';
					$scope.passwordOk = true;
					$scope.passwordReinsertedOk = true;
				}
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
				$scope.errorEmail = undefined;
				$scope.emailOk = false;
			}
			else if (!RegularExpressions.regularExpressionEmail.test($scope.email)){
				$scope.errorEmail = 'La Email non rispetta il pattern';
				$scope.okEmail = undefined;
				$scope.emailOk = false;
			}
			else{
				$scope.errorEmail = undefined;
				$scope.emailOk = true;
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
					// $scope.okName = 'OK';
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
	
	$scope.$on('REQUEST_REGISTRATION_IS_END',function(event, data){
		if(!(data == 'Username registrato correttamente')){
			$scope.showAlert = 'true';
			$scope.message = data;
		}
	});
}]);