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
 * Name : CtrlListUserProcessSpec.js
 * Target : CtrlListUserProcess
 * Location : /test/unit/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-22     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */
 
describe('CONTROLLER - CtrlListUserProcess', function (){
    var AdminUserRequestManagerMock;
    var LocationManagerMock;

    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleListUserProcessLocation','isLogged','isAdmin']);
        AdminUserRequestManagerMock= jasmine.createSpyObj('AdminUserRequestManager',['getListUserProcess','getAdminInfo']);

        LocationManagerMock.isLogged.andReturn('true');
        LocationManagerMock.isAdmin.andReturn('true');
        module('CtrlListUserProcess');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();

            // Creo un controller fittizio
            ctrl = $controller('CtrlListUserProcess', {
             $scope: $scope,
             AdminUserRequestManager : AdminUserRequestManagerMock,
             LocationManager : LocationManagerMock
            });
        });
    });

    it('should be able to do collapse function with undefined value', function(){
     var element={
         visible:undefined
     };
     $scope.collapse(element);
     expect(element['visible']).toEqual(true);
    });

    it('should be able to do collapse function with true value', function(){
     var element={
         visible:true
     };
     $scope.collapse(element);
     expect(element['visible']).toEqual(undefined);
    });

    it ("should handleRegistrationNewAdminLocation,isLogged,isAdmin have been called",function(){
     expect(LocationManagerMock.handleListUserProcessLocation).toHaveBeenCalled();
     expect(LocationManagerMock.isLogged).toHaveBeenCalled();
     expect(LocationManagerMock.isAdmin).toHaveBeenCalled();
     expect(AdminUserRequestManagerMock.getAdminInfo).toHaveBeenCalled();
    });

    it ("should do get list user process is end",function(){
     var result="Non ci sono utenti iscritti";
     $scope.$broadcast("GET_LIST_USER_PROCESS_IS_END",result);
     expect($scope.userList).toEqual([]);
     expect($scope.messageResult).toEqual(result + " a questo processo");

     result="Ci sono utenti";
     $scope.$broadcast("GET_LIST_USER_PROCESS_IS_END",result);
     expect($scope.userList).toEqual(result);
     expect($scope.currentPage).toEqual(0);
     expect($scope.pageSize).toEqual(5);
    });

    it ("should getAdminInfo have been called",function(){
     $scope.openWebSocket = function(variable){};
     $scope.$broadcast("REQUEST_INFO_IS_END","");
     expect(AdminUserRequestManagerMock.getAdminInfo).toHaveBeenCalled();
    });
});