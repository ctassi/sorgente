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
 * Name : CtrlAdminProcessSpec.js
 * Target : CtrlAdminProcess
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

describe('CONTROLLER - CtrlAdminProcess', function (){
    var LocationManagerMock;
    var AdminUserRequestManagerMock;
    var AdminUserMock;
    var localStorageServiceMock;
    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleAdminProcessLocation','handleAdminProcessLocation','isLogged','isAdmin']);
        AdminUserMock = jasmine.createSpyObj('AdminUser',['getName','getProcessList']);
        AdminUserRequestManagerMock = jasmine.createSpyObj('AdminUserRequestManager',['getAllAdminCreatedActiveProcessList','getAdminInfo']);
        localStorageServiceMock = jasmine.createSpyObj('localStorageService',['add']);

        module('CtrlAdminProcess');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();

            AdminUserMock.getName.andReturn('Test Admin Name');
            LocationManagerMock.isLogged.andReturn('true');
            LocationManagerMock.isAdmin.andReturn('true');

            // Creo un controller fittizio
            ctrl = $controller('CtrlAdminProcess', {
                $scope : $scope,
                LocationManager : LocationManagerMock,
                AdminUser : AdminUserMock,
                AdminUserRequestManager : AdminUserRequestManagerMock,
                localStorageService : localStorageServiceMock
            });
        });
    });

    it('should set be able to get the admin info', function(){
        $scope.openWebSocket = function(variable){};
        expect(LocationManagerMock.handleAdminProcessLocation).toHaveBeenCalled();
        $scope.$broadcast('REQUEST_INFO_IS_END','');
        expect(AdminUserRequestManagerMock.getAdminInfo).toHaveBeenCalled();
        expect(AdminUserRequestManagerMock.getAllAdminCreatedActiveProcessList).toHaveBeenCalled();
        expect($scope.user).toEqual('Test Admin Name');
    });

    it('should set be able get an empty list during find process that have been created', function(){
        var result="Non ci sono processi attivi";
        $scope.$broadcast('GET_ALL_ADMIN_CREATED_ACTIVE_PROCESS_IS_END', result);
        expect($scope.processList).toEqual([]);
        expect($scope.messageResult).toEqual(result);
    });

    it('should set be able get process have been created by the admin', function(){
        var processList={};
        inject(function(){
            AdminUserMock.getProcessList.andReturn(processList);
        });
        var result="Hai processo attivi";
        $scope.$broadcast('GET_ALL_ADMIN_CREATED_ACTIVE_PROCESS_IS_END','', result);
        expect($scope.processList).toEqual(processList);
        expect($scope.user).toEqual('Test Admin Name');
        expect($scope.currentPage).toEqual(0);
        expect($scope.pageSize).toEqual(5);

    });

    it('should set be able do goToModifyProcess', function(){
       $scope.goToModifyProcess();
        expect(localStorageServiceMock.add).toHaveBeenCalled();
    });

    it('should set be able do goToViewAllListUserProcess', function(){
        $scope.goToViewAllListUserProcess();
        expect(localStorageServiceMock.add).toHaveBeenCalled();
    });

    it('should be able to do CONNECTION_PROBLEM broadcast', function(){
        $scope.closeWebSocket = function(){};
        $scope.$broadcast('CONNECTION_PROBLEM','Connection problem');
    });
});