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
 * Name : CtrlClosedProcessSpec.js
 * Target : CtrlClosedProcess
 * Location : /test/unit/controller/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-27     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlClosedProcess', function (){
    var LocationManagerMock;
    var StandardUserRequestManagerMock;
    var StandardUserMock;

    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['isLogged','isAdmin','handleCloseProcessLocation']);
        StandardUserRequestManagerMock = jasmine.createSpyObj('StandardUserRequestManager',['getUserInfo','getCloseProcess']);
        StandardUserMock = jasmine.createSpyObj('StandardUser',['getName']);

        module('CtrlClosedProcess');

        inject (function($rootScope, $controller){
            // crea uno scope fittizio
            $scope = $rootScope.$new();
            $scope.openWebSocket = function(){};

            LocationManagerMock.isLogged.andReturn('true');
            LocationManagerMock.isAdmin.andReturn('false');
            StandardUserMock.getName.andReturn("Standard name");

            // crea un controller fittizio
            ctrl = $controller('CtrlClosedProcess', {
                $scope: $scope,
                LocationManager: LocationManagerMock,
                StandardUserRequestManager: StandardUserRequestManagerMock,
                StandardUser: StandardUserMock
            });
        });
    });

    it('should set be able to find closed processes', function(){
        $scope.$broadcast("GET_CLOSED_PROCESS_IS_END",'Non ci sono processi chiusi');
        expect($scope.processList).toEqual([]);
        expect($scope.messageResult).toEqual('Non ci sono processi chiusi');

        $scope.$broadcast("GET_CLOSED_PROCESS_IS_END",'Processi chiusi');
        expect($scope.processList).toEqual('Processi chiusi');
        expect($scope.messageEmptyList).toEqual('');
        expect($scope.user).toEqual('Standard name');
        expect($scope.currentPage).toEqual(0);
        expect($scope.pageSize).toEqual(5);

    });
});