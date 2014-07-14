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
 * Name : CtrlRecoveryPasswordSpec.js
 * Target : CtrlRecoveryPassword
 * Location : /test/unit/controller/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-23     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlRecoveryPassword', function (){
    var PreAuthenticationRequestManagerMock;
    var LocationManagerMock;

    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleRecoveryPasswordLocation']);
        PreAuthenticationRequestManagerMock= jasmine.createSpyObj('PreAuthenticationRequestManager',['doRecoveryPassword']);

        module('CtrlRecoveryPassword');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();
            $scope.closeAlert = function(){};

            // Creo un controller fittizio
            ctrl = $controller('CtrlRecoveryPassword', {
                $scope: $scope,
                PreAuthenticationRequestManager : PreAuthenticationRequestManagerMock,
                LocationManager : LocationManagerMock
            });
        });
    });

    it ("should handleRecoveryPasswordLocation have been called",function(){
        expect(LocationManagerMock.handleRecoveryPasswordLocation).toHaveBeenCalled();
    });

    it ("should doRecoveryPassword have been called with no username",function(){
        $scope.username="";
        $scope.doRecoveryPassword();
        expect($scope.messageResult).toEqual("Username o email non valida");
        expect($scope.showAlertError).toEqual("true");
    });

    it ("should doRecoveryPassword without find the user",function(){
        $scope.username="TestName";
        $scope.doRecoveryPassword();
        expect(PreAuthenticationRequestManagerMock.doRecoveryPassword).toHaveBeenCalled();
        $scope.$broadcast('REQUEST_RECOVERY_PASSWORD_IS_END','Utente non trovato, controllare lo username inserito');
        expect($scope.messageResult).toEqual("Utente non trovato, controllare lo username inserito");
        expect($scope.showAlertError).toEqual("true");
        expect($scope.showAlert).toEqual("false");
    });


    it ("should doRecoveryPassword and find the user",function(){
        $scope.username="TestName";
        $scope.doRecoveryPassword();
        expect(PreAuthenticationRequestManagerMock.doRecoveryPassword).toHaveBeenCalled();
        $scope.$broadcast('REQUEST_RECOVERY_PASSWORD_IS_END','Utente trovato');
        expect($scope.messageResult).toEqual("Utente trovato");
        expect($scope.showAlertError).toEqual("false");
        expect($scope.showAlert).toEqual("true");
    });

});