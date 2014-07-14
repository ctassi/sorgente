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
 * Name : CtrlAdminReport.js
 * Module : CtrlAdminReport
 * Location : /applicationContent/com/sequenziatore/client/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-25     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 */

var application = angular.module('CtrlAdminReport',['LocationManager','AdminUserRequestManager','AdminUser','LocalStorageModule']);

application.controller('CtrlAdminReport',['$scope','LocationManager','AdminUserRequestManager','AdminUser','localStorageService',
                                          function($scope,LocationManager,AdminUserRequestManager,AdminUser,localStorageService){
	
	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleAdminReportLocation();
	
	if(LocationManager.isLogged() === 'true'){
		
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
					AdminUserRequestManager.getReport($scope,Number(localStorageService.get('IdProcess')));
				}
				
			});

			$scope.$on('GET_REPORT_IS_END',function(event,result){
				
				if(result['ProcessName'] != undefined){
					$scope.nobodySubscribed = 'Nessuno si è iscritto al processo';
					$scope.processName = result['ProcessName'];
				}
				else{
					$scope.getReportEnd = true;
                    $scope.processName = result['Process'];
					$scope.totalSubscribed = result['AllSubscription'];
					$scope.endUserProcess = result['SubscriptionComplete'];
					$scope.notEndUserProcess = result['SubscriptionNotComplete'];
					$scope.levelList = result['Level'];
					$scope.stepList = result['Step'];
				}
			});
			
			$scope.$on('CONNECTION_PROBLEM',function(event,result){
				$scope.closeWebSocket();
				alert(result);
			});
		}
	}
}]);