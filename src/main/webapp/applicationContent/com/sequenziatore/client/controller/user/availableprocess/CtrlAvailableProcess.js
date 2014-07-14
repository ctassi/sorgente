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
 * Name : CtrlAvailableProcess.js
 * Module : CtrlAvailableProcess
 * Location : /applicationContent/com/sequenziatore/client/controller/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-10     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-11     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiunta paginazione contenuto
 * ======================================================
 * 0.0.3           2014-04-12     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiornamento collapse grafico
 * ======================================================
 */

var application = angular.module('CtrlAvailableProcess',['LocationManager','StandardUserRequestManager','StandardUser']);

application.controller('CtrlAvailableProcess',['$scope','LocationManager','StandardUserRequestManager','StandardUser',
                                          function($scope,LocationManager,StandardUserRequestManager,StandardUser){
	
	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleAvailableProcessLocation();

    $scope.closeAlert = function() {
      $scope.showSuccessAlert = 'false';
      $scope.showDangerAlert = 'false';
    };

    $scope.closeAlert();

	if(LocationManager.isLogged() === 'true'){

		if(LocationManager.isAdmin() === 'false'){
			
			// StandardUserRequestManager.getUserInfo($scope);
			StandardUserRequestManager.getAllAvailableProcessList($scope);

			$scope.$on('REQUEST_AVAILABLE_PROCESS_IS_END',function(event,result){
				$scope.openWebSocket("/seq/Sequenziatore/pushservice");
				
				if(result === 'Non ci sono processi disponibili'){
					$scope.processList = [];
					$scope.messageResult = result;
				}
				else if(result == 'connectionError'){
                    $scope.showSuccessAlert = true;
                    $scope.message = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
				}
				else{
					$scope.processList = StandardUser.getProcessList();
					$scope.messageEmptyList = '';	
					$scope.messageResult = undefined;
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

	// Metodo che permette ad un utente di iscriversi ad un processo
	$scope.doSubscribeProcess = function(idProcess){
		StandardUserRequestManager.doSubscribeProcess($scope,idProcess);
		$scope.$on('REQUEST_SUBSCRIBE_PROCESS_IS_END',function(event,obj){
			
			for(var i=0; i < $scope.processList.length; i++){
				if($scope.processList[i]['IdProcess'] === idProcess){
					$scope.processList[i].messageSubscription = obj['msg'];
					$scope.processList[i].buttonVisible = false;
					break;
				}
			}

            $scope.message = obj['msg'];

			if(obj['success'] === 'true') {
                $scope.showSuccessAlert = 'true';
            }
            else {
                $scope.showDangerAlert = 'true';
            }
            $scope.scrollOnTop();

			StandardUserRequestManager.getAllAvailableProcessList($scope);
		});
	};

	$scope.collapse = function(element){
		if(element['visible'] === undefined)
			element['visible'] = true;
		else
			element['visible'] = undefined;
	};
}]);







