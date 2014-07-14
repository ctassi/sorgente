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
 * Name : CtrlRecoveryPassword.js
 * Module : CtrlRecoveryPassword
 * Location : /applicationContent/com/sequenziatore/client/controller/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-04-04     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica Modulo
 * ======================================================
 * 0.0.2           2014-04-14     Romagnosi Nicolò
 * ------------------------------------------------------
 * Aggiornamento messaggio di $broadcast
 * ======================================================
 */

var application = angular.module('CtrlRecoveryPassword',['PreAuthenticationRequestManager','LocationManager']);

application.controller('CtrlRecoveryPassword',['$scope','PreAuthenticationRequestManager','LocationManager',
                                          function($scope,PreAuthenticationRequestManager,LocationManager){

	// Metodo che rimanda alla pagina di Welcome se l'utente è già autenticato
	LocationManager.handleRecoveryPasswordLocation();

    $scope.closeAlert = function(){
        $scope.showAlert = 'false';
        $scope.showAlertError = 'false';
    };

    $scope.closeAlert();

	$scope.doRecoveryPassword = function(){
        $scope.closeAlert();
        if(($scope.username === undefined || $scope.username === '')){
            $scope.messageResult = 'Username o email non valida';
            $scope.showAlertError = 'true';
        }
        else{
            var dataForm = {
                'Username': $scope.username,
                'Email': $scope.username
            };

            PreAuthenticationRequestManager.doRecoveryPassword($scope,dataForm);

            $scope.$on('REQUEST_RECOVERY_PASSWORD_IS_END',function(event,result){
                $scope.closeAlert();
                $scope.messageResult = result;
                if(result === 'Utente non trovato, controllare lo username inserito')
                    $scope.showAlertError = 'true';
                else
                    $scope.showAlert = 'true';
            });
        }
	};
}]);