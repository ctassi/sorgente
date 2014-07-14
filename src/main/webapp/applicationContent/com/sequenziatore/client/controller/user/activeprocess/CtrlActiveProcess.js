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
 * Name : CtrlActiveProcess.js
 * Module : CtrlActiveProcess
 * Location : /applicationContent/com/sequenziatore/client/controller/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-08     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-11     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiunta paginazione contenuto
 * ======================================================
 */

var application = angular.module('CtrlActiveProcess',['LocationManager','StandardUserRequestManager','StandardUser','LocalStorageModule']);

application.controller('CtrlActiveProcess',['$scope','LocationManager','StandardUserRequestManager','StandardUser','$location','localStorageService',
                                          function($scope,LocationManager,StandardUserRequestManager,StandardUser,$location,localStorageService){
	
	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleActiveProcessLocation();

    $scope.closeAlert = function() {
      $scope.unsubscribeAlertVisible = 'false';
    };

    $scope.closeAlert();

	if(LocationManager.isLogged() === 'true'){
		
		if(LocationManager.isAdmin() === 'false'){
			
			StandardUserRequestManager.getUserInfo($scope);
			
			$scope.$on('REQUEST_INFO_IS_END',function(event,result){
            	if(result == 'connectionError'){
            		//StandardUserRequestManager.doGetNextStep($scope,Number(localStorageService.get('IdProcess')));
                    $scope.unsubscribeAlertVisible = true;
                    $scope.messageUnsubscribeAlert = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
            	}
            	else{
            	      
    				$scope.openWebSocket("/seq/Sequenziatore/pushservice");
    				$scope.user = StandardUser.getName();
    				StandardUserRequestManager.getAllActiveProcessList($scope);		
            	}
			});
			
			$scope.$on('REQUEST_ACTIVE_PROCESS_LIST_IS_END',function(event,result){
				
				$scope.processList = StandardUser.getProcessList(); 
				if(StandardUser.getProcessList().length == 0){
					$scope.messageResult = 'Non partecipi ad alcun processo';

				}
				else{
					$scope.messageActiveProcess = '';
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
			
			$scope.$on('UNSUBSCRIBE_PROCESS_IS_END',function(event){
				StandardUserRequestManager.getAllActiveProcessList($scope);
				$scope.messageUnsubscribeAlert = 'Ti sei disiscritto dal processo';
				$scope.unsubscribeAlertVisible = true;
                $scope.scrollOnTop();
			});
		}
	}

	$scope.goToCurrentStep = function(idProcess){
		localStorageService.add('IdProcess',idProcess);
	};

	// Metodo che disiscrive un utente da un processo
	$scope.unSubscribeProcess = function(idProcess){
		var confirmUnsubscribe = confirm("Sei sicuro di volerti disiscrivere?");
		if(confirmUnsubscribe === true){
			StandardUserRequestManager.doUnsubscribeProcess($scope,idProcess);
		}
	};
}]);