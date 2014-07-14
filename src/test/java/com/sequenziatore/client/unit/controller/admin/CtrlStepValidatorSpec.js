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
 * Name : CtrlStepValidatorSpec.js
 * Target : CtrlStepValidator
 * Location : /test/unit/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-24     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlStepValidator', function (){
    var AdminUserRequestManagerMock;
    var AdminUserMock;
    var LocationManagerMock;
    var StepList=new Array();
    var index=0;
    var dataToSendMock;

    beforeEach(function (){
        AdminUserMock = jasmine.createSpyObj('AdminUser',['getStepList']);
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleStepValidatorLocation','isLogged','isAdmin']);
        AdminUserRequestManagerMock= jasmine.createSpyObj('AdminUserRequestManager',['doStepValidation','getAdminInfo','getAllStepsToBeValidatedList']);

        LocationManagerMock.isLogged.andReturn('true');
        LocationManagerMock.isAdmin.andReturn('true');

        module('CtrlStepValidator');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();

            $scope.openWebSocket = function(){};

            // Creo un controller fittizio
            ctrl = $controller('CtrlStepValidator', {
                $scope: $scope,
                AdminUserRequestManager : AdminUserRequestManagerMock,
                LocationManager : LocationManagerMock,
                AdminUser : AdminUserMock
            });
        });
    });

    it('should be call handleStepValidatorLocation function', function(){
        expect(LocationManagerMock.handleStepValidatorLocation).toHaveBeenCalled();
    });

    it('should be call getAllStepsToBeValidatedList function', function(){
        $scope.$broadcast('REQUEST_INFO_IS_END','');
        expect(AdminUserRequestManagerMock.getAllStepsToBeValidatedList).toHaveBeenCalled();
        expect($scope.mapList).toEqual(new Array());
    });

    it("should hasn't step to validate", function(){
        AdminUserMock.getStepList.andReturn([]);
        $scope.$broadcast('GET_ALL_STEPS_TO_BE_VALIDATED_IS_END','');
        expect($scope.messageResult).toEqual('Non ci sono processi con passi da validare');

        var obj= GenericStep();
        AdminUserMock.getStepList.andReturn(obj);
        $scope.$broadcast('GET_ALL_STEPS_TO_BE_VALIDATED_IS_END','');
        expect($scope.stepListToValidate).toEqual(obj);
        expect($scope.currentPage).toEqual(0);
        expect($scope.pageSize).toEqual(5);
    });

    it('should have at least one step to validate', function(){
        StepList.push('hello');
        AdminUserMock.getStepList.andReturn(StepList);
        $scope.$broadcast('GET_ALL_STEPS_TO_BE_VALIDATED_IS_END','');
        expect($scope.stepListToValidate[index]).toEqual('hello');
    });


    it('should do step validation', function(){
        var indexProcess=0;
        var indexLevel=0;
        var indexStep=0;
        var indexDataCollection=0;
        var dataCollection= {
         'photoConfirmation': undefined,
         'CheckLatitude' : 'null',
         'WrongGeolocation' : true,
         'geolocationConfirmation' : 'false',
         'CheckText' : 'null',
         'WrongText' : true,
         'textConfirmation' : 'false',
         'Photo' : 'null',
         'WrongPhoto' : false,
         'photoConfirmation' : 'false',
         'messageValidation' : "",
         'buttonVisible' : ""
        };
        var listDataColl=[];
        listDataColl.push(dataCollection);
        var Step = {
         'List' : listDataColl
        };
        var listStep=[];
        listStep.push(Step);
        var Level = {
         'List' : listStep
        };
        var listLevel=[];
        listLevel.push(Level);
        var Process = {
         'List' : listLevel
        };

        $scope.stepListToValidate= [];

        $scope.stepListToValidate.push(Process);
        $scope.doStepValidation(indexProcess,indexLevel,indexStep,indexDataCollection);
        expect(AdminUserRequestManagerMock.doStepValidation).toHaveBeenCalled();
        $scope.$broadcast('VALIDATION_STEP_IS_END','');
    });

    it('should show an error message for geolocation', function(){
        var indexProcess=0;
        var indexLevel=0;
        var indexStep=0;
        var indexDataCollection=0;
        var dataCollection= {
         'photoConfirmation': undefined,
         'CheckLatitude' : 'Notnull',
         'WrongGeolocation' : false,
         'geolocationConfirmation' : undefined,
         'messageValidation' : "",
         'buttonVisible' : ""
        };
        var listDataColl=[];
        listDataColl.push(dataCollection);
        var Step = {
         'List' : listDataColl
        };
        var listStep=[];
        listStep.push(Step);
        var Level = {
         'List' : listStep
        };
        var listLevel=[];
        listLevel.push(Level);
        var Process = {
         'List' : listLevel
        };

        $scope.stepListToValidate= [];
        $scope.stepListToValidate.push(Process);
        $scope.doStepValidation(indexProcess,indexLevel,indexStep,indexDataCollection);
    });

    it('should do step validation', function(){
        var indexProcess=0;
        var indexLevel=0;
        var indexStep=0;
        var indexDataCollection=0;
        var dataCollection= {
         'photoConfirmation': undefined,
         'CheckLatitude' : 'null',
         'WrongGeolocation' : true,
         'geolocationConfirmation' : undefined,
         'CheckText' : 'Notnull',
         'WrongText' : false,
         'textConfirmation' : undefined,
         'messageValidation' : "",
         'buttonVisible' : ""
        };
        var listDataColl=[];
        listDataColl.push(dataCollection);
        var Step = {
         'List' : listDataColl
        };
        var listStep=[];
        listStep.push(Step);
        var Level = {
         'List' : listStep
        };
        var listLevel=[];
        listLevel.push(Level);
        var Process = {
         'List' : listLevel
        };

        $scope.stepListToValidate= [];

        $scope.stepListToValidate.push(Process);
        $scope.doStepValidation(indexProcess,indexLevel,indexStep,indexDataCollection);
    });

    it('should do step validation', function(){
        var indexProcess=0;
        var indexLevel=0;
        var indexStep=0;
        var indexDataCollection=0;
        var dataCollection= {
         'photoConfirmation': undefined,
         'CheckLatitude' : 'null',
         'WrongGeolocation' : true,
         'geolocationConfirmation' : undefined,
         'CheckText' : 'null',
         'WrongText' : true,
         'textConfirmation' : undefined,
         'Photo' : 'null',
         'WrongPhoto' : false,
         'photoConfirmation' : undefined,
         'messageValidation' : "",
         'buttonVisible' : ""
        };
        var listDataColl=[];
        listDataColl.push(dataCollection);
        var Step = {
         'List' : listDataColl
        };
        var listStep=[];
        listStep.push(Step);
        var Level = {
         'List' : listStep
        };
        var listLevel=[];
        listLevel.push(Level);
        var Process = {
         'List' : listLevel
        };

        $scope.stepListToValidate= [];

        $scope.stepListToValidate.push(Process);
        $scope.doStepValidation(indexProcess,indexLevel,indexStep,indexDataCollection);
    });

    it("should do createMap function",function(){
        $scope.createMap();
    });

    it("should do showProcessContent function",function(){
        var indexProcess=0;
        var object=new Object();
        $scope.stepListToValidate=[];
        $scope.stepListToValidate.push(object);
        $scope.stepListToValidate[indexProcess].visible=undefined;
        $scope.showProcessContent(indexProcess);;
        $scope.stepListToValidate[indexProcess].visible=true;
        $scope.showProcessContent(indexProcess);
    });

    it("should do ShowLevelContent function",function(){
        var indexProcess=0;
        var indexLevel=0;
        var list=[];
        list.push(new Object());
        var process={
         List : list
        };
        $scope.stepListToValidate=[];
        $scope.stepListToValidate.push(process);
        $scope.stepListToValidate[indexProcess].visible=undefined;
        $scope.showLevelContent(indexProcess,indexLevel);;
        $scope.stepListToValidate[indexProcess].visible=true;
        $scope.showLevelContent(indexProcess,indexLevel);
    });


    it("should do ShowStepContent function",function(){
        var indexProcess=0;
        var indexLevel=0;
        var indexStep=0;
        var Step=new Object();
        var listStep=[];
        listStep.push(Step);
        var Level = {
         'List' : listStep
        };
        var listLevel=[];
        listLevel.push(Level);
        var Process = {
         'List' : listLevel
        };

        $scope.stepListToValidate=[];
        $scope.stepListToValidate.push(Process);
        $scope.stepListToValidate[indexProcess].visible=undefined;
        $scope.showStepContent(indexProcess,indexLevel,indexStep);;
        $scope.stepListToValidate[indexProcess].visible=true;
        $scope.showStepContent(indexProcess,indexLevel,indexStep);
    });

    it("should do ShowMap function",function(){
        var indexProcess=0;
        var indexLevel=0;
        var indexStep=0;
        var indexDataColl=0;
        var dataCollection= {
         'photoConfirmation': undefined,
         'CheckLatitude' : 'null',
         'WrongGeolocation' : true,
         'geolocationConfirmation' : 'false',
         'CheckText' : 'null',
         'WrongText' : true,
         'textConfirmation' : 'false',
         'Photo' : 'null',
         'WrongPhoto' : false,
         'photoConfirmation' : 'false',
         'messageValidation' : "",
         'buttonVisible' : ""
        };
        var listDataColl=[];
        listDataColl.push(dataCollection);
        var Step = {
         'List' : listDataColl
        };
        var listStep=[];
        listStep.push(Step);
        var Level = {
         'List' : listStep
        };
        var listLevel=[];
        listLevel.push(Level);
        var Process = {
         'List' : listLevel
        };

        $scope.stepListToValidate=[];
        $scope.stepListToValidate.push(Process);
        $scope.stepListToValidate[indexProcess].visible=undefined;
        $scope.showMap(indexProcess,indexLevel,indexStep,indexDataColl);;
        $scope.stepListToValidate[indexProcess].visible=true;
        $scope.showMap(indexProcess,indexLevel,indexStep,indexDataColl);
    });
});