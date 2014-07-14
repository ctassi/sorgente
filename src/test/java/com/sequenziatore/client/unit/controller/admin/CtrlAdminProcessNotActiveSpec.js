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
 * Name : CtrlAdminProcessNotActiveSpec.js
 * Target : CtrlAdminProcessNotActive
 * Location : /test/unit/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-18     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlAdminProcessNotActive', function (){
    var LocationManagerMock;
    var AdminUserRequestManagerMock;
    var AdminUserMock;
    var localStorageServiceMock;
    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleAdminProcessNotActiveLocation','isLogged','isAdmin']);
        AdminUserMock = jasmine.createSpyObj('AdminUser',['getName']);
        AdminUserRequestManagerMock = jasmine.createSpyObj('AdminUserRequestManager',['getAdminInfo','viewAdminProcessNotActive']);
        localStorageServiceMock = jasmine.createSpyObj('localStorageService',['add']);

        module('CtrlAdminProcessNotActive');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();
            $scope.openWebSocket = function(){};

            LocationManagerMock.isLogged.andReturn('true');
            LocationManagerMock.isAdmin.andReturn('true');
            AdminUserMock.getName.andReturn("Admin name");


            // Creo un controller fittizio
            ctrl = $controller('CtrlAdminProcessNotActive', {
                $scope : $scope,
                LocationManager : LocationManagerMock,
                AdminUser : AdminUserMock,
                AdminUserRequestManager : AdminUserRequestManagerMock,
                localStorageService : localStorageServiceMock
            });
        });
    });

    it('should be able to do REQUEST_INFO_IS_END broadcast', function(){
       $scope.$broadcast("REQUEST_INFO_IS_END",'');
        expect(AdminUserRequestManagerMock.viewAdminProcessNotActive).toHaveBeenCalled();

    });

    it('should be able to do VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END broadcast', function(){
        $scope.$broadcast("VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END",'Non ci sono processi terminati');
        expect($scope.processList).toEqual([]);
        expect($scope.messageResult).toEqual("Non ci sono processi terminati");

        $scope.$broadcast("VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END",'Processi terminati');
        expect($scope.processList).toEqual("Processi terminati");
        expect($scope.user).toEqual("Admin name");
        expect($scope.currentPage).toEqual(0);
        expect($scope.pageSize).toEqual(5);

    });

    it('should be able to do goToAdminReport', function(){
       $scope.goToAdminReport();
        expect(localStorageServiceMock.add).toHaveBeenCalled();
    });

    it('should be able to do CONNECTION_PROBLEM broadcast', function(){
        $scope.closeWebSocket = function(){};
        $scope.$broadcast('CONNECTION_PROBLEM','Connection problem');
    });
});