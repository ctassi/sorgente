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
 * Location : /applicationContent/com/sequenziatore/client/controller/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-04     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-11     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiunte dipendenze mancanti
 * ======================================================
 */

var application = angular.module('CtrlProfile',['LocationManager','StandardUser','StandardUserRequestManager',
                                                'AdminUser','AdminUserRequestManager']);

application.controller('CtrlProfile',['$scope','LocationManager','StandardUser','StandardUserRequestManager',
                                      'AdminUser','AdminUserRequestManager',
                                          function($scope,LocationManager,StandardUser,StandardUserRequestManager,AdminUser,AdminUserRequestManager){

	LocationManager.handleProfileLocation();
	
	if(LocationManager.isLogged() === 'true'){
		$scope.$on('CONNECTION_PROBLEM',function(event,result){
			$scope.closeWebSocket();
			alert(result);
		});
	
		if(LocationManager.isAdmin() === 'false'){
            StandardUserRequestManager.getUserInfo($scope);

            $scope.$on('REQUEST_INFO_IS_END',function(event,result){

                if(result == 'connectionError'){
                    $scope.messageVisible = true;
                    $scope.messageData = 'Problemi di connessione al server, contattare l\'amministratore';
                    $scope.scrollOnTop();
                }
                else{
                    $scope.openWebSocket("/seq/Sequenziatore/pushservice");
                    $scope.username = StandardUser.getUsername();
                    $scope.name = StandardUser.getName();
                    $scope.surname = StandardUser.getSurname();
                    $scope.email = StandardUser.getEmail();
                    $scope.isAdmin = StandardUser.getIsAdmin();
                    $scope.district = StandardUser.getDistrict();
                    $scope.city = StandardUser.getCity();
                    $scope.user = StandardUser.getName();
                 }
            });
		}

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
                $scope.username = AdminUser.getUsername();
                $scope.name = AdminUser.getName();
                $scope.surname = AdminUser.getSurname();
                $scope.email = AdminUser.getEmail();
                $scope.isAdmin = AdminUser.getIsAdmin();
                $scope.district = AdminUser.getDistrict();
                $scope.city = AdminUser.getCity();
                $scope.user = AdminUser.getName();
                 }

            });
		}
	}	
}]);