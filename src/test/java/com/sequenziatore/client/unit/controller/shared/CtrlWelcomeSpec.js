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
 * Name : CtrlWelcomeSpec.js
 * Target : CtrlWelcome
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

describe('CONTROLLER - CtrlWelcome', function (){

    var LocationManagerMock;
    var StandardUserMock;
    var StandardUserRequestManagerMock;
    var AdminUserMock;
    var AdminUserRequestManagerMock;

    beforeEach(function (){

        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleWelcomeLocation','isLogged','isAdmin']);
        StandardUserMock = jasmine.createSpyObj('StandardUser',['getName']);
        StandardUserRequestManagerMock = jasmine.createSpyObj('StandardUserRequestManager',['getUserInfo','getStatisticsUser']);
        AdminUserMock = jasmine.createSpyObj('AdminUser',['getName']);
        AdminUserRequestManagerMock = jasmine.createSpyObj('AdminUserRequestManager',['getAdminInfo','getStatisticsAdmin']);

        LocationManagerMock.isLogged.andReturn("true");

        module('CtrlWelcome');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();
            $scope.openWebSocket = function(){};
            $scope.handlerMenu = function(){
                if(LocationManagerMock.isLogged() === 'true'){
                    $scope.logged = 'true';
                    if(LocationManagerMock.isAdmin() === 'true'){
                        $scope.admin = 'true';
                    }
                    else{
                        $scope.admin = undefined;
                    }
                }
                else{
                    $scope.logged = undefined;
                    $scope.admin = undefined;
                }
            };
            // Creo un controller fittizio
            ctrl = $controller('CtrlWelcome', {
                $scope: $scope,
                LocationManager: LocationManagerMock,
                StandardUser : StandardUserMock,
                StandardUserRequestManager : StandardUserRequestManagerMock,
                AdminUser : AdminUserMock,
                AdminUserRequestManager : AdminUserRequestManagerMock
            });
        });
    });

    it("should hadleWelcomeLocation, isLogged have been called",function(){
        expect(LocationManagerMock.handleWelcomeLocation).toHaveBeenCalled();
        expect(LocationManagerMock.isLogged).toHaveBeenCalled();
    });

    describe("Welcome to a StandardUser",function(){
        beforeEach(function(){
            LocationManagerMock.isAdmin.andReturn("false");
            inject(function($rootScope, $controller){
                ctrl = $controller('CtrlWelcome', {
                    $scope: $scope,
                    LocationManager: LocationManagerMock,
                    StandardUser : StandardUserMock,
                    StandardUserRequestManager : StandardUserRequestManagerMock
                });
            });
        });

        it("should say welcome to a StandardUser without name",function(){
            expect(LocationManagerMock.isAdmin).toHaveBeenCalled();
            StandardUserMock.getName.andReturn("NoName");
            expect(StandardUserRequestManagerMock.getUserInfo).toHaveBeenCalled();
            $scope.$broadcast("REQUEST_INFO_IS_END");
            expect(StandardUserMock.getName).toHaveBeenCalled();
            expect($scope.user).toEqual("NoName");
            expect($scope.admin).toEqual(undefined);
            expect($scope.logged).toEqual("true");
        });


        it("should say welcome to a StandardUser with StandardName",function(){
            expect(LocationManagerMock.isAdmin).toHaveBeenCalled();
            StandardUserMock.getName.andReturn("StandardName");
            expect(StandardUserRequestManagerMock.getUserInfo).toHaveBeenCalled();
            $scope.$broadcast("REQUEST_INFO_IS_END");
            expect(StandardUserMock.getName).toHaveBeenCalled();
            expect($scope.user).toEqual("StandardName");
            expect($scope.admin).toEqual(undefined);
            expect($scope.logged).toEqual("true");
        });

        it("should do GET_USER_STATISTICS_IS_END",function(){
            var result={
                Close : "close",
                CloseAll : "closeAll",
                ActiveProcess : "activeProcess",
                AvailableProcess : "availableProcess",
                CloseSuccess : "closeSuccess"
            };
            $scope.$broadcast("GET_USER_STATISTICS_IS_END",result);
            expect($scope.userIsAdmin).toEqual("false");
            expect($scope.processesClosedNotCompleted).toEqual("close");
            expect($scope.processesClosedNotCompletedAndCompleted).toEqual("closeAll");
            expect($scope.processesActive).toEqual("activeProcess");
            expect($scope.processesAvailable).toEqual("availableProcess");
            expect($scope.processesCompleted).toEqual("closeSuccess");
        });
    });

    describe("Welcome to an AdminUser",function(){
        beforeEach(function(){
            LocationManagerMock.isAdmin.andReturn("true");
            inject(function($rootScope, $controller){
                ctrl = $controller('CtrlWelcome', {
                    $scope: $scope,
                    LocationManager: LocationManagerMock,
                    StandardUser : StandardUserMock,
                    StandardUserRequestManager : StandardUserRequestManagerMock,
                    AdminUser : AdminUserMock,
                    AdminUserRequestManager : AdminUserRequestManagerMock
                });
            });
        });

        it("should say welcome to a AdminUser without name",function(){
            expect(LocationManagerMock.isAdmin).toHaveBeenCalled();
            AdminUserMock.getName.andReturn("NoName");
            expect(AdminUserRequestManagerMock.getAdminInfo).toHaveBeenCalled();
            $scope.$broadcast("REQUEST_INFO_IS_END");
            expect(AdminUserMock.getName).toHaveBeenCalled();
            expect($scope.user).toEqual("NoName");
            expect($scope.admin).toEqual("true");
            expect($scope.logged).toEqual("true");
        });

        it("should say welcome to a AdminUser with AdminName",function(){
            expect(LocationManagerMock.isAdmin).toHaveBeenCalled();
            AdminUserMock.getName.andReturn("AdminName");
            expect(AdminUserRequestManagerMock.getAdminInfo).toHaveBeenCalled();
            $scope.$broadcast("REQUEST_INFO_IS_END");
            expect(AdminUserMock.getName).toHaveBeenCalled();
            expect($scope.user).toEqual("AdminName");
            expect($scope.admin).toEqual("true");
            expect($scope.logged).toEqual("true");
        });

        it("should do GET_ADMIN_STATISTICS_IS_END",function(){
            var result={
                AllProcess : "All",
                CloseProcess : "Close",
                ToVerify : "ToVerify",
                ActiveProcess: "ActiveProcess",
                AllUser : "AllUser"
            };
            $scope.$broadcast("GET_ADMIN_STATISTICS_IS_END",result);
            expect($scope.userIsAdmin).toEqual("true");
            expect($scope.processesTotal).toEqual("All");
            expect($scope.processesClosed).toEqual("Close");
            expect($scope.stepsToVerify).toEqual("ToVerify");
            expect($scope.processesActive).toEqual("ActiveProcess");
            expect($scope.usersTotal).toEqual("AllUser");
        });
    });
});