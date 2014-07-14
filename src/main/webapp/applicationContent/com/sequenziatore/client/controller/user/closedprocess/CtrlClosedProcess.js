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
 * Name : CtrlClosedProcess.js
 * Module : CtrlClosedProcess
 * Location : /applicationContent/com/sequenziatore/client/controller/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-10     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 */

var application = angular.module('CtrlClosedProcess',['LocationManager','StandardUserRequestManager','StandardUser']);

application.controller('CtrlClosedProcess',['$scope','LocationManager','StandardUserRequestManager','StandardUser',
                                          function($scope,LocationManager,StandardUserRequestManager,StandardUser){
	
	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleCloseProcessLocation();

	if(LocationManager.isLogged() === 'true'){
		
		if(LocationManager.isAdmin() === 'false'){
			
			StandardUserRequestManager.getUserInfo($scope);
			StandardUserRequestManager.getCloseProcess($scope);

			$scope.$on('GET_CLOSED_PROCESS_IS_END',function(event,result){
				$scope.openWebSocket("/seq/Sequenziatore/pushservice");
				
				if(result === 'Non ci sono processi chiusi'){
					$scope.processList = [];
					$scope.messageResult = result;
				}
				else if (result == 'connectionError'){
                    $scope.messageVisible = true;
                    $scope.messageData = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
				}
				else{
					$scope.processList = result;
					$scope.messageEmptyList = '';
					// Paginazione del contenuto
				    $scope.currentPage = 0;
				    $scope.pageSize = 5;
				    $scope.numberOfPages= Math.ceil($scope.processList.length/$scope.pageSize);
				    
					$scope.changePage=function(page){
						$scope.currentPage = page;
					};
				}
				$scope.user = StandardUser.getName();
			});
			
			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				alert(result);
			});
		}
	}
}]);







