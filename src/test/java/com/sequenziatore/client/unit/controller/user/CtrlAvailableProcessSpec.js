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
 * Name : CtrlAvailableProcessSpec.js
 * Target : CtrlAvailableProcess
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

describe('CONTROLLER - CtrlAvailableProcess', function (){
    var LocationManagerMock;
    var StandardUserRequestManagerMock;
    var StandardUserMock;

    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['isLogged','isAdmin','handleAvailableProcessLocation']);
        StandardUserRequestManagerMock = jasmine.createSpyObj('StandardUserRequestManager',['getUserInfo','getAllAvailableProcessList','doSubscribeProcess']);
        StandardUserMock = jasmine.createSpyObj('StandardUser',['getName','getProcessList']);

        module('CtrlAvailableProcess');

        inject (function($rootScope, $controller){
            $scope = $rootScope.$new();
            $scope.openWebSocket = function(){};

            StandardUserMock.getName.andReturn('Test Name');
            LocationManagerMock.isLogged.andReturn('true');
            LocationManagerMock.isAdmin.andReturn('false');

            ctrl = $controller('CtrlAvailableProcess', {
                $scope: $scope,
                LocationManager: LocationManagerMock,
                StandardUserRequestManager: StandardUserRequestManagerMock,
                StandardUser: StandardUserMock
            });
        });
    });

    it('should be able to do collapse function with undefined', function(){
        var element={
            visible:undefined
        };
        $scope.collapse(element);
        expect(element['visible']).toEqual(true);
    });

    it('should be able to do collapse function with true', function(){
        var element={
            visible:true
        };
        $scope.collapse(element);
        expect(element['visible']).toEqual(undefined);
    });

    it('should set be able to show that there aren\'t any process', function(){
        var result="Non ci sono processi disponibili";
        $scope.$broadcast('REQUEST_AVAILABLE_PROCESS_IS_END',result);
        expect($scope.processList).toEqual([]);
        expect($scope.messageResult).toEqual(result);
    });



    it('should set be able to get the user info', function(){
        var obj = [{
            id : 42,
            steps : 10
        }];
        inject(function(){
            StandardUserMock.getProcessList.andReturn(obj);
        });
        $scope.$broadcast('REQUEST_AVAILABLE_PROCESS_IS_END','');
        expect($scope.processList).toEqual(obj);
        expect($scope.messageEmptyList).toEqual('');
        expect($scope.messageResult).toEqual(undefined);
        expect($scope.user).toEqual('Test Name');
        expect($scope.currentPage).toEqual(0);
        expect($scope.pageSize).toEqual(5);
    });

    it('should be able to do getAllAvailableProcessList', function(){
        expect(StandardUserRequestManagerMock.getAllAvailableProcessList).toHaveBeenCalled();
    });

    it('should be able to do doSubscribeProcess', function(){
        $scope.scrollOnTop = function(){};
        var idProcess=0;
        var process={
            IdProcess : idProcess
        };
        var obj={
            msg:"Test msg",
            success : 'true'
        };
        $scope.processList=[];
        $scope.processList[idProcess]=process;
        $scope.processList[idProcess].messageSubscription="";
        $scope.processList[idProcess].buttonVisible="";
        $scope.doSubscribeProcess(idProcess);
        expect(StandardUserRequestManagerMock.doSubscribeProcess).toHaveBeenCalled();
        $scope.$broadcast('REQUEST_SUBSCRIBE_PROCESS_IS_END',obj);
        expect($scope.processList[idProcess].messageSubscription).toEqual("Test msg");
        expect($scope.processList[idProcess].buttonVisible).toEqual(false);
        expect($scope.message).toEqual("Test msg");
        expect($scope.showSuccessAlert).toEqual('true');
        obj={
            msg:"Test msg",
            success : 'false'
        };
        $scope.$broadcast('REQUEST_SUBSCRIBE_PROCESS_IS_END',obj);
        expect($scope.showDangerAlert).toEqual('true');
        expect(StandardUserRequestManagerMock.getAllAvailableProcessList).toHaveBeenCalled();
    });

    it('should be able to do CONNECTION_PROBLEM broadcast', function(){
        $scope.closeWebSocket = function(){};
        $scope.$broadcast('CONNECTION_PROBLEM','Connection problem');
    });
});