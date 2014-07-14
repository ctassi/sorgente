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
 * Name : CtrlLoginSpec.js
 * Target : CtrlLogin
 * Location : /test/unit/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-24     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlLogin', function (){
    var PreAuthenticationRequestManagerMock;
    var LocationManagerMock;
    var EzfbMock;

    beforeEach(function (){
        PreAuthenticationRequestManagerMock = jasmine.createSpyObj('PreAuthenticationRequestManager',['doLogin','doFacebookLogin']);
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleLoginLocation']);
        EzfbMock=jasmine.createSpyObj('ezfb',['login']);
        module('CtrlLogin');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();
            $scope.closeWebSocket = function(){};

            // Creo un controller fittizio
            ctrl = $controller('CtrlLogin', {
                $scope: $scope,
                PreAuthenticationRequestManager: PreAuthenticationRequestManagerMock,
                LocationManager: LocationManagerMock,
                ezfb : EzfbMock
            });
        });
    });

    it('should do facebook login', function(){
        $scope.facebookLogin();
        $scope.$broadcast('REQUEST_FB_API_IS_END');
        
    });

    it('should do REQUEST_LOGIN_IS_END broadcast', function(){
        $scope.$broadcast('REQUEST_LOGIN_IS_END','LoginIsEnd');
        expect($scope.message).toEqual('LoginIsEnd');
        expect($scope.loginStatus).toEqual('disconnected');
    });

    it('should do CONNECTION_PROBLEM broadcast', function(){
        $scope.$broadcast('CONNECTION_PROBLEM','');
    });
});