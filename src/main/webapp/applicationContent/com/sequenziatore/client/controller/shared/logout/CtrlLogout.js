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
 * Name : CtrlLogout.js
 * Module : CtrlLogout
 * Location : /applicationContent/com/sequenziatore/client/controller/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-01     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-14     Pavanello Mirko
 * ------------------------------------------------------
 * Aggiornata gestione cookies
 * ======================================================
 */

var application = angular.module('CtrlLogout',['LocationManager','StandardUser','StandardUserRequestManager','AdminUser','AdminUserRequestManager','ezfb']);

application.controller('CtrlLogout',['$scope','LocationManager','StandardUser','StandardUserRequestManager','AdminUser','AdminUserRequestManager','ezfb',
                                          function($scope,LocationManager,StandardUser,StandardUserRequestManager,AdminUser,AdminUserRequestManager,ezfb){

    $scope.facebookLogout = function () {
      ezfb.logout(function () {
          $scope.updateLoginStatus($scope.updateApiMe);
      });
    }

	// Eseguo il logout nella servlet, ovvero metto a false il token nella sessione
	// solo se il cookie relativo all'utente loggato è settato a true
	if(LocationManager.isLogged() === 'true'){

		// Cancello tutti i campi dato dello user
		if(StandardUser.getIsAdmin() === false){
			StandardUser.reset();
			$scope.closeWebSocket();
            if($scope.loginStatus == 'facebook')
            {
                $scope.facebookLogout();
            }
            StandardUserRequestManager.doLogout();
		}
		else{
			AdminUser.reset();
			$scope.closeWebSocket();
			AdminUserRequestManager.doLogout();
		}
        $scope.loginStatus = 'disconnected';
	}

	LocationManager.handleLogoutLocation(LocationManager.getUserCookie());
	$scope.handlerMenu();
}]);