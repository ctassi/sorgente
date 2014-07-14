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
 * Name : CtrlAdminProcessNotActive.js
 * Module : CtrlAdminProcessNotActive
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
 */

var application = angular.module('CtrlAdminProcessNotActive',['LocationManager','AdminUserRequestManager','AdminUser','LocalStorageModule']);

application.controller('CtrlAdminProcessNotActive',['$scope','LocationManager','AdminUserRequestManager','AdminUser','localStorageService',
                                          function($scope,LocationManager,AdminUserRequestManager,AdminUser,localStorageService){

	LocationManager.handleAdminProcessNotActiveLocation();

	$scope.processList = [];
	
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
    				AdminUserRequestManager.viewAdminProcessNotActive($scope);
            	}
			});
			
			$scope.$on('VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END',function(event,result){
				
				if(result === 'Non ci sono processi terminati'){
					$scope.processList = [];
					$scope.messageResult = result;
				}
				else{
					$scope.processList = result;
				}
				
				$scope.user = AdminUser.getName();
				// Paginazione del contenuto
			    $scope.currentPage = 0;
			    $scope.pageSize = 5;
			    $scope.numberOfPages= Math.ceil($scope.processList.length/$scope.pageSize);
			    
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

	$scope.goToAdminReport = function(idProcess){
		localStorageService.add('IdProcess',idProcess);
	};
}]);