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
 * Name : LocationManager.js
 * Module : LocationManager
 * Location : /applicationContent/com/sequenziatore/client/model/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-18     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 */

var application = angular.module('LocationManager',['ngCookies']);

application.factory('LocationManager',['$cookieStore','$location',function($cookieStore,$location){
	
	var LocationManager = function(){

		function isLogged(){
			return $cookieStore.get('logged');
		};
		
		function isAdmin(){
			return $cookieStore.get('isAdmin');
		};
		
		function getUserCookie(){
			return $cookieStore.get('user');
		};
		
		function getIdUserCookie(){
			return $cookieStore.get('idUser');
		};
		
		function getFacebookCookie(){
			return $cookieStore.get('facebook');
		};
		
		function navigateTo(url){
			$location.path( url );
		};

		function setCookie(username,isAdmin,idUser,facebook){
			
			$cookieStore.put('logged', 'true');
			$cookieStore.put('idUser', idUser);

			$cookieStore.put('user', username);
			
			if(facebook == true){
				$cookieStore.put('facebook', 'true');
			}
			else{
				$cookieStore.put('facebook', 'false');
			}
			
			if(isAdmin === true){
				$cookieStore.put('isAdmin', 'true');
			}
			else{
				$cookieStore.put('isAdmin', 'false');
			}
		};
		
		//HandlerLocation pagina Login
		function handleLoginLocation(){
			if($cookieStore.get('logged')==='true'){
				if($cookieStore.get('isAdmin') === 'false'){
					$location.path( "/welcome" );
				}
				else{
					$location.path( "/welcome" );
				}
					
			}
			else{
				$location.path( "/login" );

			}
		};

		//HandlerLocation pagina Registration
		function handleRegistrationLocation(){
			if($cookieStore.get('logged')==='true'){
				if($cookieStore.get('isAdmin') === 'false'){
					$location.path( "/welcome" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/registration" );
			}
		};

		//HandlerLocation pagina Welcome
		function handleWelcomeLocation(username){
			if($cookieStore.get('logged')==='true'){
				$location.path( "/welcome" );
			}
			else{
				$location.path( "/login" );
			}
		};
		
		//HandlerLocation pagina Logout
		function handleLogoutLocation(){
			if($cookieStore.get('logged')==='true'){
				$cookieStore.put('logged', 'false');
				$cookieStore.put('user', 'false');
				$cookieStore.put('isAdmin', 'false');
				$cookieStore.put('facebook', 'false');
				$location.path( "/login" );
			}
			else{
				$location.path( "/login" );
			}
		};
		
		//HandlerLocation pagina Profilo
		function handleProfileLocation(){
			if($cookieStore.get('logged')==='true'){
				$location.path( "/profile" );
			}
			else{
				$location.path( "/login" );
			}
		};
		
		//HandlerLocation pagina creazione nuovo processo
		function handleCreateProcessLocation(){
			if($cookieStore.get('logged')==='true'){
				
				//Se l'utente è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'true'){
					$location.path( "/createprocess" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};
		
		//HandlerLocation pagina registraione nuovo admin
		function handleRegistrationNewAdminLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'true'){
					$location.path( '/registrationnewadmin' );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};
		
		//HandlerLocation pagina admin process
		function handleAdminProcessLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'true'){
					$location.path( '/adminprocess' );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};
		
		// HandlerLocation pagina inserimento nuovo processo
		function handleStepValidatorLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'true'){
					$location.path( "/stepvalidator" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};
		
		// HandlerLocation pagina visualizzazione di tutti i processi presenti
		// nel database
		function handleViewAllProcessLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'true'){
					$location.path( "/viewallprocess" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};
		
		// HandlerLocation pagina dei processi attivi di un utente
		function handleActiveProcessLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente non è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'false'){
					$location.path( "/activeprocess" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};

		// HandlerLocation pagina dei processi disponibili a cui un utente pu� iscriversi
		function handleAvailableProcessLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente non è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'false'){
					$location.path( "/availableprocess" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};
		
		// HandlerLocation pagina degli utenti partecipanti ad almeno un processo
		function handleListUserProcessLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente non è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'true'){
					$location.path( "/listuserprocess" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};

		// HandlerLocation pagina modifica di un processo
		function handleModifyProcessLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente non è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'true'){
					$location.path( "/modifyprocess" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};

		// HandlerLocation pagina della modifica di un account
		function handleModifyAccountLocation(){
			if($cookieStore.get('logged')==='true'){
				$location.path( "/modifyaccount" );
			}
			else{
				$location.path( "/login" );
			}
		};		

		// HandlerLocation pagina avanzamento di un passo
		function handleCurrentStepLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente non è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'false'){
					$location.path( "/currentstep" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};

		// HandlerLocation pagina avanzamento di un passo
		function handleRecoveryPasswordLocation(){
			if($cookieStore.get('logged')==='true'){
				if($cookieStore.get('isAdmin') === 'false'){
					$location.path( "/welcome" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/recoverypassword" );
			}
		};
		
		// HandlerLocation pagina avanzamento di un passo
		function handleAdminProcessNotActiveLocation(){
			
			if($cookieStore.get('logged')==='true'){
			// Se l'utente non è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'true'){
					$location.path( "/adminprocessnotactive" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};
		
		// HandlerLocation pagina avanzamento di un passo
		function handleAdminReportLocation(){
			
			if($cookieStore.get('logged')==='true'){
			// Se l'utente non è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'true'){
					$location.path( "/adminreport" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};
		
		// HandlerLocation pagina avanzamento di un passo
		function handleCloseProcessLocation(){
			if($cookieStore.get('logged')==='true'){
				
				// Se l'utente non è un amministratore lo faccio accedere
				if($cookieStore.get('isAdmin') === 'false'){
					$location.path( "/closedprocess" );
				}
				else{
					$location.path( "/welcome" );
				}
			}
			else{
				$location.path( "/login" );
			}
		};
		
		return{
			isLogged: isLogged,
			isAdmin: isAdmin,
			getUserCookie: getUserCookie,
			setCookie: setCookie,
			handleLoginLocation: handleLoginLocation,
			handleRegistrationLocation: handleRegistrationLocation,
			handleWelcomeLocation: handleWelcomeLocation,
			handleLogoutLocation: handleLogoutLocation,
			handleProfileLocation: handleProfileLocation,
			handleCreateProcessLocation: handleCreateProcessLocation,
			handleRegistrationNewAdminLocation: handleRegistrationNewAdminLocation,
			handleAdminProcessLocation: handleAdminProcessLocation,
			handleStepValidatorLocation: handleStepValidatorLocation,
			handleViewAllProcessLocation: handleViewAllProcessLocation,
			handleActiveProcessLocation: handleActiveProcessLocation,
			handleAvailableProcessLocation: handleAvailableProcessLocation,
			handleListUserProcessLocation: handleListUserProcessLocation,
			handleModifyProcessLocation: handleModifyProcessLocation,
			handleModifyAccountLocation: handleModifyAccountLocation,
			handleCurrentStepLocation: handleCurrentStepLocation,
			handleRecoveryPasswordLocation: handleRecoveryPasswordLocation,
			getIdUserCookie: getIdUserCookie,
			handleAdminProcessNotActiveLocation: handleAdminProcessNotActiveLocation,
			handleAdminReportLocation: handleAdminReportLocation,
			handleCloseProcessLocation: handleCloseProcessLocation,
			getFacebookCookie: getFacebookCookie,
			navigateTo: navigateTo
		};
	};

	return LocationManager();
}]);	
	


	 
