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
 * Name : CtrlLogin.js
 * Module : CtrlLogin
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
 * 0.0.2           2014-04-05     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Aggiornata chiusura websocket
 * ======================================================
 */

var application = angular.module('CtrlLogin',['PreAuthenticationRequestManager','LocationManager','ezfb']);

application.config(function (ezfbProvider) {
    ezfbProvider.setInitParams({
        appId: '854287504585300'
    });
});

application.controller('CtrlLogin',['$scope','PreAuthenticationRequestManager','LocationManager','ezfb',
                                          function($scope,PreAuthenticationRequestManager,LocationManager,ezfb){

	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleLoginLocation();

    //  Per controllare subito se l'utente è già autenticato con Facebook decommentare
    //  updateLoginStatus(updateApiMe);

    $scope.facebookLogin = function () {
        ezfb.login(function(res) {
            if (res.authResponse) {
                $scope.updateLoginStatus($scope.updateApiMe);
                $scope.$on('REQUEST_FB_API_IS_END', function(){
                    var res = $scope.apiMe;
                    var dataToSend = {
                      'Username': res.email,
                      'Password': 'facebook',
                      'Email': res.email,
                      'Name': res.first_name,
                      'Surname': res.last_name,
                      'City': '',
                      'District': '',
                      'IsAdmin': false
                    };
                    try{
                      PreAuthenticationRequestManager.doFacebookLogin($scope, dataToSend);
                      $scope.loginStatus = 'facebook';
                      $scope.message = 'Login in corso...';
                    }
                    catch(error){
                      $scope.loginStatus = 'disconnected';
                      $scope.message = error;
                    }
                });
            }
        }, {scope: 'email,user_likes'});
    }

	$scope.doLogin = function(){
		try{
			$scope.username = $("#username").val();
			$scope.password = $("#password").val();
			
			if(($scope.username === undefined || $scope.username === '')){
				throw 'Username non inserito';
			}

			if(($scope.password === undefined || $scope.password === '')){
				throw 'Password non inserita';
			}
	
			var dataToSend = {
					'Username': $scope.username,
					'Password': $scope.password
			};

            $scope.message = 'Login in corso...';
			PreAuthenticationRequestManager.doLogin($scope,dataToSend);
            $scope.loginStatus = 'connected';
        }
		catch(error){
            $scope.loginStatus = 'disconnected';
			$scope.message = error;
		}
	};

	$scope.$on('REQUEST_LOGIN_IS_END',function(event, data){
		$scope.message = data;
        $scope.loginStatus = 'disconnected';
	});
	
	$scope.$on('CONNECTION_PROBLEM',function(event,result){
		$scope.closeWebSocket();
		alert(result);
	});
}]);