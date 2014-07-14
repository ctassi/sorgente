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
 * Name : CtrlListUserProcess.js
 * Module : CtrlListUserProcess
 * Location : /applicationContent/com/sequenziatore/client/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-27     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-03-29     Romagnosi Nicolò
 * ------------------------------------------------------
 * Messaggio di problemi di connessione
 * ======================================================
 */

var application = angular.module('CtrlListUserProcess',['LocationManager','AdminUserRequestManager','LocalStorageModule']);

application.controller('CtrlListUserProcess',['$scope','LocationManager','AdminUserRequestManager','localStorageService',
                                          function($scope,LocationManager,AdminUserRequestManager,localStorageService){

	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleListUserProcessLocation();
	
	$scope.collapse = function(element){
		if(element.visible === undefined)
			element.visible = true;
		else
			element.visible = undefined;
	};
	
	if(LocationManager.isLogged() === 'true'){
		
		if(LocationManager.isAdmin() === 'true'){
			AdminUserRequestManager.getAdminInfo($scope);
			
			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
				if(result == 'connectionError'){
	                 $scope.messageVisible = true;
	                 $scope.messageData = 'Problemi di connessione al server, contattare l\'amministratore';
	                 $scope.scrollOnTop();
				}
				else{
				$scope.openWebSocket("/seq/Sequenziatore/pushservice");
				$scope.processName = localStorageService.get('ProcessName');
				AdminUserRequestManager.getListUserProcess($scope,Number(localStorageService.get('IdProcess')));
				}
			});
			
			$scope.$on('GET_LIST_USER_PROCESS_IS_END',function(event,result){
				if(result === 'Non ci sono utenti iscritti'){
					$scope.userList = [];
					$scope.messageResult = result + ' a questo processo';
				}
				else{
					$scope.userList = result;
				}

				// Paginazione del contenuto
			    $scope.currentPage = 0;
			    $scope.pageSize = 5;
			    $scope.numberOfPages= Math.ceil($scope.userList.length/$scope.pageSize);
			    
				$scope.changePage=function(page){
					$scope.currentPage = page;
				};
			});
			
			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				alert(result);
			});
		}
	}
}]);