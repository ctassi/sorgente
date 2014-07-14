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
 * Name : CtrlAdminProcess.js
 * Module : CtrlAdminProcess
 * Location : /applicationContent/com/sequenziatore/client/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-24     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-02     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiunta gestione local storage
 * ======================================================
 */

var application = angular.module('CtrlAdminProcess',['LocationManager','AdminUserRequestManager','AdminUser','LocalStorageModule']);

application.controller('CtrlAdminProcess',['$scope','LocationManager','AdminUserRequestManager','AdminUser','localStorageService',
                                          function($scope,LocationManager,AdminUserRequestManager,AdminUser,localStorageService){

	LocationManager.handleAdminProcessLocation();

	$scope.processList = [];
	
	if(LocationManager.isLogged() === 'true'){
		
		if(LocationManager.isAdmin() === 'true'){

			AdminUserRequestManager.getAdminInfo($scope);

			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
				$scope.openWebSocket("/seq/Sequenziatore/pushservice");
				AdminUserRequestManager.getAllAdminCreatedActiveProcessList($scope);
				$scope.user = AdminUser.getName();
			});
			
			$scope.$on('GET_ALL_ADMIN_CREATED_ACTIVE_PROCESS_IS_END',function(event,result){

				if(result === 'Non ci sono processi attivi'){
					$scope.processList = [];
					$scope.messageResult = result;
				}

            	else if(result == 'connectionError'){
                    $scope.messageVisible = true;
                    $scope.messageData = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
            	}
				else{
					$scope.processList = AdminUser.getProcessList();
					$scope.user = AdminUser.getName();
					// Paginazione del contenuto
				    $scope.currentPage = 0;
				    $scope.pageSize = 5;
				    $scope.numberOfPages= Math.ceil($scope.processList.length/$scope.pageSize);
				    
					$scope.changePage=function(page){
						$scope.currentPage = page;
					};
				}
			});
			
			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				alert(result);
			});
		}
	}

	$scope.goToModifyProcess = function(idProcess){
		localStorageService.add('IdProcess',idProcess);
	};

	$scope.goToViewAllListUserProcess = function(idProcess,processName){
		localStorageService.add('IdProcess',idProcess);
		localStorageService.add('ProcessName',processName);
	};
}]);