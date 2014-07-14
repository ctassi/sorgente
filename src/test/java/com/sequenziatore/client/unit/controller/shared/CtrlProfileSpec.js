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
 * Name : CtrlProfileSpec.js
 * Target : CtrlProfile
 * Location : /test/unit/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-26     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlProfile', function (){
    var StandardUserMock;
    var LocationManagerMock;
    var StandardUserRequestManagerMock;
    var AdminUserMock;
    var AdminUserRequestManagerMock;

    describe("CONTROLLER - CtrlProfile an admin",function(){

        beforeEach(function(){
            LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleProfileLocation','isLogged','isAdmin']);
            AdminUserMock = jasmine.createSpyObj('AdminUser',['getUsername','getName','getSurname','getEmail','getIsAdmin','getDistrict','getCity']);
            AdminUserRequestManagerMock = jasmine.createSpyObj('AdminUserRequestManager',['getAdminInfo']);


            module("CtrlProfile");

            inject (function($rootScope, $controller){
                // Creo uno scope fittizio
                $scope = $rootScope.$new();

                $scope.openWebSocket = function(variable){};

                AdminUserMock.getUsername.andReturn("testAdminUsername");
                AdminUserMock.getName.andReturn("testAdminName");
                AdminUserMock.getEmail.andReturn("testAdmin@email.com");
                AdminUserMock.getIsAdmin.andReturn("testAdminIsAdmin");
                AdminUserMock.getDistrict.andReturn("testAdminDistrict");
                AdminUserMock.getCity.andReturn("testAdminCity");

                LocationManagerMock.isLogged.andReturn('true');
                LocationManagerMock.isAdmin.andReturn('true');

                // Creo un controller fittizio
                ctrl = $controller('CtrlProfile', {
                    $scope: $scope,
                    AdminUser : AdminUserMock,
                    LocationManager : LocationManagerMock,
                    AdminUserRequestManager : AdminUserRequestManagerMock
                });
            });
        });

        it ("should has logged as Admin",function(){
            expect(AdminUserRequestManagerMock.getAdminInfo).toHaveBeenCalled();
            $scope.$broadcast('REQUEST_INFO_IS_END','');
            expect($scope.username).toEqual("testAdminUsername");
            expect($scope.name).toEqual("testAdminName");
            expect($scope.email).toEqual("testAdmin@email.com");
            expect($scope.isAdmin).toEqual("testAdminIsAdmin");
            expect($scope.district).toEqual("testAdminDistrict");
            expect($scope.city).toEqual("testAdminCity");
            expect($scope.user).toEqual("testAdminName");
        });
    });

    describe("CONTROLLER - CtrlProfile an user",function(){
        beforeEach(function(){
            LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleProfileLocation','isLogged','isAdmin']);
            StandardUserMock = jasmine.createSpyObj('StandardUser',['getUsername','getName','getSurname','getEmail','getIsAdmin','getDistrict','getCity']);
            StandardUserRequestManagerMock =  jasmine.createSpyObj('StandardUserRequestManager',['getUserInfo']);

            module("CtrlProfile");

            inject (function($rootScope, $controller){
                // Creo uno scope fittizio
                $scope = $rootScope.$new();

                $scope.openWebSocket = function(variable){};

                StandardUserMock.getUsername.andReturn("testStandardUserUsername");
                StandardUserMock.getName.andReturn("testStandardUserName");
                StandardUserMock.getEmail.andReturn("testStandardUser@email.com");
                StandardUserMock.getIsAdmin.andReturn("testStandardUserIsAdmin");
                StandardUserMock.getDistrict.andReturn("testStandardUserDistrict");
                StandardUserMock.getCity.andReturn("testStandardUserCity");

                LocationManagerMock.isLogged.andReturn('true');
                LocationManagerMock.isAdmin.andReturn('false');

                // Creo un controller fittizio
                ctrl = $controller('CtrlProfile', {
                    $scope: $scope,
                    StandardUser : StandardUserMock,
                    LocationManager : LocationManagerMock,
                    StandardUserRequestManager : StandardUserRequestManagerMock
                });
            });
        });

        it ("should has logged as User",function(){
            expect(StandardUserRequestManagerMock.getUserInfo).toHaveBeenCalled();
            $scope.$broadcast('REQUEST_INFO_IS_END','');
            expect($scope.username).toEqual("testStandardUserUsername");
            expect($scope.name).toEqual("testStandardUserName");
            expect($scope.email).toEqual("testStandardUser@email.com");
            expect($scope.isAdmin).toEqual("testStandardUserIsAdmin");
            expect($scope.district).toEqual("testStandardUserDistrict");
            expect($scope.city).toEqual("testStandardUserCity");
            expect($scope.user).toEqual("testStandardUserName");
        });
    });
});