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
 * Name : CtrlAdminReportSpec.js
 * Target : CtrlAdminReport
 * Location : /test/unit/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-19     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlAdminReport', function (){
    var LocationManagerMock;
    var AdminUserRequestManagerMock;
    var AdminUserMock;
    var localStorageServiceMock;
    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleAdminReportLocation','isLogged','isAdmin']);
        AdminUserMock = jasmine.createSpyObj('AdminUser',['getName']);
        AdminUserRequestManagerMock = jasmine.createSpyObj('AdminUserRequestManager',['getAdminInfo','getReport']);
        localStorageServiceMock = jasmine.createSpyObj('localStorageService',['get']);

        module('CtrlAdminReport');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();
            $scope.openWebSocket = function(){};

            LocationManagerMock.isLogged.andReturn('true');
            LocationManagerMock.isAdmin.andReturn('true');
            AdminUserMock.getName.andReturn("Admin name");


            // Creo un controller fittizio
            ctrl = $controller('CtrlAdminReport', {
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
        expect(AdminUserRequestManagerMock.getReport).toHaveBeenCalled();
    });

    it('should be able to do GET_REPORT_IS_END broadcast', function(){
        var result = {
            'ProcessName' : 'someProcess'
        };
        $scope.$broadcast("GET_REPORT_IS_END",result);
        expect($scope.nobodySubscribed).toEqual("Nessuno si è iscritto al processo");
        expect($scope.processName).toEqual("someProcess");

        var result = {
            Process: "process",
            AllSubscription : "allsubscription",
            SubscriptionComplete : "subcomplete",
            SubscriptionNotComplete : "subnotcomplete",
            Level : "level",
            Step : "step"
        };
        $scope.$broadcast("GET_REPORT_IS_END",result);
        expect($scope.processName).toEqual("process");
        expect($scope.totalSubscribed).toEqual("allsubscription");
        expect($scope.endUserProcess).toEqual("subcomplete");
        expect($scope.notEndUserProcess).toEqual("subnotcomplete");
        expect($scope.levelList).toEqual("level");
        expect($scope.stepList).toEqual("step");
    });
});