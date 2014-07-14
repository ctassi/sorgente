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
 * Name : CtrlActiveProcessSpec.js
 * Target : CtrlActiveProcess
 * Location : /test/unit/controller/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-24     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlActiveProcess', function (){
    var LocationManagerMock;
    var StandardUserRequestManagerMock;
    var StandardUserMock;
    var localStorageServiceMock;


  beforeEach(function (){
      LocationManagerMock = jasmine.createSpyObj('LocationManager',['isLogged','isAdmin','handleActiveProcessLocation']);
      StandardUserRequestManagerMock = jasmine.createSpyObj('StandardUserRequestManager',['getUserInfo','getAllActiveProcessList','doUnsubscribeProcess']);
      StandardUserMock = jasmine.createSpyObj('StandardUser',['getName','getProcessList']);
      localStorageServiceMock = jasmine.createSpyObj('localStorageService',['add']);

      module('CtrlActiveProcess');

      inject (function($rootScope, $controller){
         $scope = $rootScope.$new();

          StandardUserMock.getName.andReturn('Test Name');
          LocationManagerMock.isLogged.andReturn('true');
          LocationManagerMock.isAdmin.andReturn('false');

          ctrl = $controller('CtrlActiveProcess', {
              $scope: $scope,
              LocationManager: LocationManagerMock,
              StandardUserRequestManager: StandardUserRequestManagerMock,
              StandardUser: StandardUserMock,
              localStorageService : localStorageServiceMock
          });
      });
  });


     it('should set be able to get the user info', function(){
         $scope.openWebSocket = function(variable){};
        $scope.$broadcast('REQUEST_INFO_IS_END','');
        expect(StandardUserRequestManagerMock.getUserInfo).toHaveBeenCalled();
        expect($scope.user).toEqual('Test Name');
     });

    it('should be able to view a message for an empty process list', function(){
        $scope.processList=[];
        $scope.messageResult="";
        inject(function(){
            StandardUserMock.getProcessList.andReturn([]);
        });
        $scope.$broadcast('REQUEST_ACTIVE_PROCESS_LIST_IS_END','');
        expect(StandardUserMock.getProcessList).toHaveBeenCalled();
        expect($scope.processList).toEqual([]);
        expect($scope.messageResult).toEqual("Non partecipi ad alcun processo");
    });

    it('should be able to return the process list', function(){
        var obj = {
            id : 42,
            steps : 10};
        inject(function(){
            StandardUserMock.getProcessList.andReturn(obj);
        });
        $scope.$broadcast('REQUEST_ACTIVE_PROCESS_LIST_IS_END','');
        expect(StandardUserMock.getProcessList).toHaveBeenCalled();
        expect($scope.processList).toEqual(obj);
        expect($scope.messageActiveProcess).toEqual("");
        expect($scope.currentPage).toEqual(0);
        expect($scope.pageSize).toEqual(5);
    });

    it('should be able to view an unsubscribe message', function(){
        $scope.scrollOnTop = function(){};
        $scope.$broadcast('UNSUBSCRIBE_PROCESS_IS_END','');
        expect(StandardUserRequestManagerMock.getAllActiveProcessList).toHaveBeenCalled();
        expect($scope.messageUnsubscribeAlert).toEqual('Ti sei disiscritto dal processo');
        expect($scope.unsubscribeAlertVisible).toEqual(true);
    });

    it('should be able to go to current step and add data in the local storage', function(){
        $scope.goToCurrentStep();
        expect(localStorageServiceMock.add).toHaveBeenCalled();
    });


    it('should be able to do CONNECTION_PROBLEM broadcast', function(){
        $scope.closeWebSocket = function(){};
        $scope.$broadcast('CONNECTION_PROBLEM','Connection problem');
    });

    it('should be able to do unsubscribeProcess function ', function(){
        spyOn(window, 'confirm').andReturn(true);
        var idProcess=0;
        $scope.unSubscribeProcess(idProcess);
        expect(StandardUserRequestManagerMock.doUnsubscribeProcess).toHaveBeenCalled();

    });
});
