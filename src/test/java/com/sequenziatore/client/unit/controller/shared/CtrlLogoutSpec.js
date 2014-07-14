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
 * Name : CtrlLogoutSpec.js
 * Target : CtrlLogout
 * Location : /test/unit/controller/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-25     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlLogout', function (){
    var StandardUserMock;
    var LocationManagerMock;
    var StandardUserRequestManagerMock;
    var AdminUserMock;
    var AdminUserRequestManagerMock;
    var EzfbMock;

    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleLogoutLocation','isLogged','getUserCookie']);
        StandardUserMock = jasmine.createSpyObj('StandardUser',['reset','getIsAdmin']);
        StandardUserRequestManagerMock =  jasmine.createSpyObj('StandardUserRequestManager',['doLogout']);
        AdminUserMock = jasmine.createSpyObj('AdminUser',['reset']);
        EzfbMock = jasmine.createSpyObj('ezfb',['logout']);

        module('CtrlLogout');

        inject (function($rootScope, $controller){
            // crea uno scope fittizio
            $scope = $rootScope.$new();
            $scope.handlerMenu = function(){};

            $scope.closeWebSocket = function(){};
            $scope.loginStatus="facebook";

            LocationManagerMock.isLogged.andReturn("true");
            StandardUserMock.getIsAdmin.andReturn(false);

            ctrl = $controller('CtrlLogout', {
                $scope: $scope,
                LocationManager : LocationManagerMock,
                AdminUser : AdminUserMock,
                StandardUser : StandardUserMock,
                StandardUserRequestManager : StandardUserRequestManagerMock,
                ezfb : EzfbMock
            });
        });

    });

    it("should isLogged, handleLogoutLocation, getUserCookie, doLogout have been called",function(){
        expect(LocationManagerMock.isLogged).toHaveBeenCalled();
        expect(LocationManagerMock.handleLogoutLocation).toHaveBeenCalled();
        expect(LocationManagerMock.getUserCookie).toHaveBeenCalled();
        expect(StandardUserRequestManagerMock.doLogout).toHaveBeenCalled();
        expect($scope.loginStatus).toEqual('disconnected');
    });

    it("should logged out from facebook",function(){
        $scope.facebookLogout();
    });

    describe('CONTROLLER - CtrlLogout', function (){
        beforeEach(function (){

            inject (function($rootScope, $controller){
                // crea uno scope fittizio
                $scope = $rootScope.$new();

                $scope.handlerMenu = function(){};
                $scope.closeWebSocket = function(){};

                LocationManagerMock.isLogged.andReturn("true");
                StandardUserMock.getIsAdmin.andReturn(true);

                ctrl = $controller('CtrlLogout', {
                    $scope: $scope,
                    LocationManager : LocationManagerMock,
                    AdminUser : AdminUserMock,
                    StandardUser : StandardUserMock,
                    StandardUserRequestManager : StandardUserRequestManagerMock,
                    ezfb : EzfbMock
                });
            });

        });

        it("should isLogged, handleLogoutLocation, getUserCookie, doLogout have been called",function(){
            expect(AdminUserMock.reset).toHaveBeenCalled();
            expect($scope.loginStatus).toEqual('disconnected');
        });
    });
});