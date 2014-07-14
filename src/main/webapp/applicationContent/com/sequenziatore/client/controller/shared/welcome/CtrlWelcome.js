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
 * Name : CtrlWelcome.js
 * Module : CtrlWelcome
 * Location : /applicationContent/com/sequenziatore/client/controller/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-02     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-11     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiunto alert in caso di problemi di connessione
 * ======================================================
 */

var application = angular.module('CtrlWelcome',['LocationManager','StandardUser','StandardUserRequestManager','AdminUser','AdminUserRequestManager']);

application.controller('CtrlWelcome',['$scope','LocationManager','StandardUser','StandardUserRequestManager','AdminUser','AdminUserRequestManager',
                                          function($scope,LocationManager,StandardUser,StandardUserRequestManager,AdminUser,AdminUserRequestManager){

	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleWelcomeLocation();
	$scope.handlerMenu();
	
	if(LocationManager.isLogged() === 'true'){

		if(LocationManager.isAdmin() === 'false'){
			
			StandardUserRequestManager.getUserInfo($scope);
			
			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				alert(result);
			});
			
			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
				if(result == 'connectionError'){
            		//StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
                    $scope.messageVisible = true;
                    $scope.messageData = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
            	}
				else{
					$scope.openWebSocket("/seq/Sequenziatore/pushservice");
					StandardUserRequestManager.getStatisticsUser($scope);
					$scope.user =  StandardUser.getName();		
				}
			});
			
			$scope.$on('GET_USER_STATISTICS_IS_END',function(event,result){
				$scope.userIsAdmin = LocationManager.isAdmin();
				$scope.processesClosedNotCompleted = result['Close'];
				$scope.processesClosedNotCompletedAndCompleted = result['CloseAll'];
				$scope.processesActive = result['ActiveProcess'];
				$scope.processesAvailable = result['AvailableProcess'];
				$scope.processesCompleted = result['CloseSuccess'];
            });
        }

		if(LocationManager.isAdmin() === 'true'){
			AdminUserRequestManager.getAdminInfo($scope);
			
			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
				if(result == 'connectionError'){
            		//StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
                    $scope.messageVisible = true;
                    $scope.messageData = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
            	}
				else{
					$scope.openWebSocket("/seq/Sequenziatore/pushservice");
					AdminUserRequestManager.getStatisticsAdmin($scope);
					$scope.user =  AdminUser.getName();					
					
				}
			});
			
			$scope.$on('GET_ADMIN_STATISTICS_IS_END',function(event,result){
				$scope.userIsAdmin = LocationManager.isAdmin();
				
				$scope.processesTotal = result['AllProcess'];
				$scope.processesClosed = result['CloseProcess'];
				$scope.stepsToVerify = result['ToVerify'];
				$scope.processesActive = result['ActiveProcess'];
				$scope.usersTotal = result['AllUser'];

			});
		}
	}
}]);