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
 * Name : CtrlModifyProcessSpec.js
 * Target : CtrlModifyProcess
 * Location : /test/unit/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-21     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe("CONTROLLER - CtrlModifyProcess", function(){
    var LocationManagerMock;
    var AdminUserRequestManagerMock;
    var AdminUserMock;
    var localStorageServiceMock;
    var processList = [];

    beforeEach(function(){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleModifyProcessLocation','isLogged','isAdmin']);
        AdminUserMock = jasmine.createSpyObj('AdminUser',['getName','getProcessList']);
        AdminUserRequestManagerMock = jasmine.createSpyObj('AdminUserRequestManager',['getProcess','getAdminInfo','doModifyProcess']);
        localStorageServiceMock = jasmine.createSpyObj('localStorageService',['get']);

        AdminUserMock.getProcessList.andReturn(processList);
        AdminUserMock.getName.andReturn("adminTestName");
        LocationManagerMock.isLogged.andReturn('true');
        LocationManagerMock.isAdmin.andReturn('true');

        module('CtrlModifyProcess');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();
            $scope.scrollOnTop = function (){};

            // Creo un controller fittizio
            ctrl = $controller('CtrlModifyProcess', {
                $scope: $scope,
                AdminUserRequestManager : AdminUserRequestManagerMock,
                LocationManager : LocationManagerMock,
                AdminUser : AdminUserMock
            });
        });
    });


    it("should handleModifyProcessLocation have been called",function(){
        expect(LocationManagerMock.handleModifyProcessLocation).toHaveBeenCalled();
    });

    it("should AdminUserRequestManager functions have been called",function(){
        expect(AdminUserRequestManagerMock.getAdminInfo).toHaveBeenCalled();
    });

    it("should AdminUser functions have been called",function(){
        $scope.openWebSocket = function(variable){};
        $scope.$broadcast('REQUEST_INFO_IS_END','');
        expect(AdminUserRequestManagerMock.getProcess).toHaveBeenCalled();
        expect(AdminUserMock.getName).toHaveBeenCalled();
        expect($scope.user).toEqual("adminTestName");
    });


    it("should get process",function(){
        var step= GenericStep();
        var result={
            Available : false,
            Name : "Nome processo",
            Description : "Descrizione processo",
            PublicationDate : "13/05/2014",
            ClosingDate : "31/05/2014",
            EndSubscriptionDate : "17/05/2014",
            StepList : step
        };
        $scope.$broadcast('GET_PROCESS_IS_END',result);
        expect($scope.isActive).toEqual("Non attivo");
        expect($scope.buttonIsActiveStatus).toEqual("Attiva processo");


        result={
            Available : true,
            Name : "Nome processo",
            Description : "Descrizione processo",
            PublicationDate : "13/05/2014",
            ClosingDate : "31/05/2014",
            EndSubscriptionDate : "17/05/2014",
            StepList : step
        };
        $scope.$broadcast('GET_PROCESS_IS_END',result);
        expect($scope.result).toEqual(result);
        expect($scope.isActive).toEqual("Attivo");
        expect($scope.buttonIsActiveStatus).toEqual("Disattiva processo");
        expect($scope.available).toEqual(true);
        expect($scope.processName).toEqual("Nome processo");
        expect($scope.processDescription).toEqual("Descrizione processo");
        expect($scope.dateStartView).toEqual("13/05/2014");
        expect($scope.dateCloseView).toEqual("31/05/2014");
        expect($scope.dateEndSubscriptionView).toEqual("17/05/2014");
        expect($scope.stepList).toEqual(step);
    });

    it("should do isActiveClicked",function(){
        $scope.available=false;
        spyOn(window, 'confirm').andReturn(true);
        $scope.isActiveClicked();
        expect($scope.available).toEqual(true);
        expect($scope.isActive).toEqual("Attivo");
        expect($scope.buttonIsActiveStatus).toEqual("Disattiva processo");

        $scope.available=true;
        $scope.isActiveClicked();
        expect($scope.available).toEqual(false);
        expect($scope.isActive).toEqual("Non Attivo");
        expect($scope.buttonIsActiveStatus).toEqual("Attiva processo");
    });


    it("should do showCalendarStart",function(){
        $scope.dateStartVisible = undefined;
        $scope.showCalendarStart();
        expect($scope.dateStartVisible).toEqual("true");

        $scope.dateStartVisible = true;
        $scope.showCalendarStart();
        expect($scope.dateStartVisible).toEqual(undefined);
    });

    it("should do showCalendarClose",function(){
        $scope.dateCloseVisible = undefined;
        $scope.showCalendarClose();
        expect($scope.dateCloseVisible ).toEqual("true");

        $scope.dateCloseVisible  = true;
        $scope.showCalendarClose();
        expect($scope.dateCloseVisible ).toEqual(undefined);
    });

    it("should do showCalendarEndSubScription",function(){
        $scope.dateEndSubscriptionVisible = undefined;
        $scope.showCalendarEndSubscription();
        expect($scope.dateEndSubscriptionVisible ).toEqual("true");

        $scope.dateEndSubscriptionVisible  = true;
        $scope.showCalendarEndSubscription();
        expect($scope.dateEndSubscriptionVisible ).toEqual(undefined);
    });

    it("should save the dateStartSelected",function(){
        var date="2014/07/03";
        var toSend = {
            'Day' : 03,
            'Month' : 07,
            'Year' : 2014
        };
        $scope.dateStartVisible = "visible";
        $scope.dateStartSelected(date);
        expect($scope.dateStartView).toEqual("03-07-2014");
        expect($scope.dateStartToSend).toEqual(toSend);
        expect($scope.dateStartVisible).toEqual(undefined);
    });

    it("should save the dateCloseSelected",function(){
        var date="2014/07/03";
        var toSend = {
            'Day' : 03,
            'Month' : 07,
            'Year' : 2014
        };
        $scope.dateCloseVisible = "visible";
        $scope.dateCloseSelected(date);
        expect($scope.dateCloseView).toEqual("03-07-2014");
        expect($scope.dateCloseToSend).toEqual(toSend);
        expect($scope.dateCloseVisible).toEqual(undefined);
    });

    it("should save the dateEndSubscriptionSelected",function(){
        var date="2014/07/03";
        var toSend = {
            'Day' : 03,
            'Month' : 07,
            'Year' : 2014
        };
        $scope.dateEndSubscriptionVisible = "visible";
        $scope.dateEndSubscriptionSelected(date);
        expect($scope.dateEndSubscriptionView).toEqual("03-07-2014");
        expect($scope.dateEndSubscriptionToSend).toEqual(toSend);
        expect($scope.dateEndSubscriptionVisible).toEqual(undefined);
    });

    it("should do stringDateTOJsonDate",function(){
        var data="1-06-2014";
        var dateReturn={
            'Day' : 1,
            'Month' : 06,
            'Year' : 2014
        };
        var val=$scope.stringDateToJSONdate(data);
        expect(val).toEqual(dateReturn);

        data="13-05-2014";
        dateReturn={
            'Day' : 13,
            'Month' : 05,
            'Year' : 2014
        };
        var val=$scope.stringDateToJSONdate(data);
        expect(val).toEqual(dateReturn);
    });

    it("should test the modify of a process are correct",function(){
        $scope.processName="";
        $scope.doModifyProcess();
        expect($scope.messageError).toEqual("Nome processo non inserito");
        expect($scope.showAlertError).toEqual("true");

        $scope.processName="Nome processo";
        $scope.processDescription="";
        $scope.doModifyProcess();
        expect($scope.messageError).toEqual("Descrizione processo non inserita");
        expect($scope.showAlertError).toEqual("true");

        var step=GenericStep();
        step.setIsPhoto('true');
        step.setIsText('true');
        step.setIsGeolocation('true');
        var result={
            Available : false,
            Name : "Nome processo",
            Description : "Descrizione processo",
            PublicationDate : "13/05/2014",
            ClosingDate : "31/05/2014",
            EndSubscriptionDate : "17/05/2014",
            StepList : step,
            IdStep : '1',
            Level : '1',
            IdProcess : '1'
        };
        $scope.processName="Nome processo";
        $scope.processDescription="Descrizione processo";
        $scope.dateStartView="14-05-2014";
        $scope.dateCloseView="30-05-2014";
        $scope.dateEndSubscriptionView="12-05-2014";
        $scope.result=result;
        $scope.changedPublicationDate=undefined;
        $scope.changedCloseDate=undefined;
        $scope.changedEndSubDate=undefined;
        $scope.changedPublicationDate=undefined;
        $scope.doModifyProcess();

        $scope.dateStartToSend={
            Year : 2014,
            Month:05,
            Day : 13
        };

        $scope.dateCloseToSend={
            Year : 2014,
            Month:05,
            Day : 30
        };

        $scope.dateEndSubscriptionToSend={
            Year : 2014,
            Month:05,
            Day : 17
        };

        $scope.changedPublicationDate=true;
        $scope.changedCloseDate=true;
        $scope.changedEndSubDate=true;
        $scope.doModifyProcess();
        expect($scope.messageError).toEqual("La data di inizio deve essere la stessa di oggi o una maggiore");
        expect($scope.showAlertError).toEqual("true");

        var date = new Date();
        date.setDate(date.getDate());
        $scope.dateStartToSend={
            Year : date.getFullYear()+1,
            Month:date.getMonth(),
            Day : date.getDate()
        };
        $scope.doModifyProcess();
        expect($scope.messageError).toEqual("La data di chiusura deve essere successiva a quella di inizio");
        expect($scope.showAlertError).toEqual("true");


        $scope.changedPublicationDate=false;
        $scope.available=true;
        $scope.doModifyProcess();
        expect(AdminUserRequestManagerMock.doModifyProcess).toHaveBeenCalled();

        $scope.$broadcast("MODIFY_PROCESS_IS_END",'MessageOk');
        expect($scope.messageOk).toEqual("MessageOk");
        expect($scope.showAlertOK).toEqual("true");
    });
});