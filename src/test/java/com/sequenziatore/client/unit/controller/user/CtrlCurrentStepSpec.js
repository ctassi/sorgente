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
 * Name : CtrlCurrentStepSpec.js
 * Target : CtrlCurrentStep
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

describe('CONTROLLER - CtrlCurrentStep', function (){
    var LocationManagerMock;
    var StandardUserRequestManagerMock;
    var StandardUserMock;
    var localStorageServiceMock;

    beforeEach(function (){
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['isLogged','isAdmin','handleCurrentStepLocation','getIdUserCookie']);
        StandardUserRequestManagerMock = jasmine.createSpyObj('StandardUserRequestManager',['getUserInfo','doGetNextStep','doSendUserStep']);
        StandardUserMock = jasmine.createSpyObj('StandardUser',['getStepList','getGeolocation']);
        localStorageServiceMock = jasmine.createSpyObj('localStorageService',['keys','get','remove','set']);

        module('CtrlCurrentStep');

        inject (function($rootScope, $controller){
            $scope = $rootScope.$new();
            $scope.openWebSocket = function(variable){};
            $scope.closeWebSocket = function(variable){};

            LocationManagerMock.isLogged.andReturn('true');
            LocationManagerMock.isAdmin.andReturn('false');

            ctrl = $controller('CtrlCurrentStep', {
                $scope: $scope,
                LocationManager: LocationManagerMock,
                StandardUserRequestManager: StandardUserRequestManagerMock,
                StandardUser: StandardUserMock,
                localStorageService : localStorageServiceMock
            });
        });
    });

    it("should handleCurrentStepLocation have been called",function(){
        expect(LocationManagerMock.handleCurrentStepLocation).toHaveBeenCalled()
    });

    it('should handle a next step response', function(){
        $scope.$broadcast('REQUEST_INFO_IS_END','');
        expect(StandardUserRequestManagerMock.doGetNextStep).toHaveBeenCalled();
    });

    it('should notify a process complete message', function(){
        var step = GenericStep();
        step.setWrongGeolocation("wrongGeolocation");
        step.setParallelism("AND");
        step.setIdStep(1);
        var list=[];
        list.push(step);
        var result={
            'ProcessName' : 'someProcess'
        };

        inject(function(){
            StandardUserMock.getStepList.andReturn([]);
        });
        expect(StandardUserRequestManagerMock.getUserInfo).toHaveBeenCalled();
        $scope.scrollOnTop = function(){};
        $scope.$broadcast('REQUEST_GET_NEXT_STEP_IS_END',result);
        expect($scope.messageData).toEqual('Processo completato con SUCCESSO');
        expect($scope.processName).toEqual('someProcess');


        inject(function(){
            StandardUserMock.getStepList.andReturn(list);
        });
        $scope.$broadcast('REQUEST_GET_NEXT_STEP_IS_END','');
        expect($scope.parallelism).toEqual("Per procedere al livello successivo dovrai completare TUTTI i passi presenti in un ordine a tua scelta");

        step.setParallelism("OR");
        list=[];
        list.push(step);
        inject(function(){
            StandardUserMock.getStepList.andReturn(list);
        });
        $scope.$broadcast('REQUEST_GET_NEXT_STEP_IS_END','');
        expect($scope.parallelism).toEqual("Per procedere al livello successivo dovrai completare UNO dei passi a tua scelta");

        step.setParallelism("NOT");
        list=[];
        list.push(step);
        inject(function(){
            StandardUserMock.getStepList.andReturn(list);
        });
        $scope.$broadcast('REQUEST_GET_NEXT_STEP_IS_END','');
        expect($scope.parallelism).toEqual("Per procedere al livello successivo dovrai completare il passo richiesto");

        var key="ID:1_STEP:1";
        inject(function(){
            localStorageServiceMock.keys.andReturn(key);
            LocationManagerMock.getIdUserCookie.andReturn(1);
        });
    });

    it("should do getGeolocationData function",function(){
        var step = GenericStep();
        var idStep = 0;
        var indexStep = 0;
        step.setIdStep(idStep);
        $scope.stepList = [];
        $scope.stepList.push(step);
        $scope.getGeolocationData(idStep);
        expect($scope.stepList[indexStep].messageInfoResultGeo).toEqual("Geolocalizzazione in corso...");

        var geoLocation = {
            'Latitude' : "latitudeTest",
            'Longitude' : "longitudeTest"
        };

        var dataToSend = StandardUserStep();
        dataToSend.setInsertedLatitude("latitudeTest");
        dataToSend.setInsertedLongitude("longitudeTest");
        dataToSend.setInsertedText("null");
        dataToSend.setInsertedPhoto("null");
        dataToSend.setIdStep(idStep);

        $scope.$broadcast("GEOLOCATION_IS_END",geoLocation);
        expect($scope.stepList[indexStep].messageInfoResultGeo).toEqual("La tua posizione attuale è stata rilevata");
        expect($scope.stepList[indexStep].messageInfoLatitude).toEqual("latitudeTest");
        expect($scope.stepList[indexStep].messageInfoLongitude).toEqual("longitudeTest");
        expect($scope.stepList[indexStep].elementGeoVisible).toEqual(false);
        expect($scope.stepList[indexStep].dataGeolocation).toEqual(dataToSend.makeJSONObject());

        var res = {
            'Confirmation' : true
        };
    });

    it("should do doStep function",function(){
        var indexStep=0;
        var insertedText = "";
        var step = StandardUserStep();
        var stepList = [];
        step.setIdStep(indexStep);
        step.setWrongGeolocation(false);
        step.setIsGeolocation('true');
        step.dataGeolocation=undefined;
        stepList.push(step);
        inject(function(){
            StandardUserMock.getStepList.andReturn(stepList);
        });
        $scope.stepList = [];
        $scope.stepList.push(step);

        $scope.doStep(indexStep,insertedText);
        expect($scope.stepList[indexStep].messageNotification).toEqual("Geolocalizzazione obbligatoria");

        step.dataGeolocation="dataGeolocationTest";
        step.setWrongText(false);
        step.setIsText('true');
        $scope.stepList = [];
        $scope.stepList.push(step);
        $scope.doStep(indexStep,insertedText);
        expect($scope.stepList[indexStep].messageNotification).toEqual("Testo obbligatorio");

        insertedText = "insertedTextTest";
        step.setWrongPhoto(false);
        step.setIsPhoto('true');
        $scope.stepList = [];
        $scope.stepList.push(step);
        $scope.doStep(indexStep,insertedText);
        expect($scope.stepList[indexStep].messageNotification).toEqual("Foto obbligatoria");

        $scope.file = "notNull";
        $scope.doStep(indexStep,insertedText);
        expect($scope.stepList[indexStep].messageNotification).toEqual("");
        expect(StandardUserRequestManagerMock.doSendUserStep).toHaveBeenCalled();

        var res = {
            'Data' : true
        };
        $scope.scrollOnTop = function() {};
        $scope.$broadcast('SEND_USER_STEP_IS_END','Dati inviati correttamente');
        expect($scope.stepList[indexStep].messageNotification).toEqual("");

    });

    it("should do onFileSelect function",function(){
        var aFileParts = ['<a id="a"><b id="b">hey!</b></a>'];
        var file=new Blob(aFileParts,{type : "image/jpeg"});
        var files=[];
        files.push(file);
        var idStep=0;
        var step= GenericStep();
        step.setIdStep(idStep);
        $scope.stepList=[];
        $scope.stepList.push(step);
        $scope.onFileSelect(files,idStep);
    });

    it("should do onFileSelect error on image format ",function(){
        var aFileParts = ['<a id="a"><b id="b">hey!</b></a>'];
        var file=new Blob(aFileParts,{type : "image/fig"});
        var files=[];
        files.push(file);
        var idStep=0;
        var step= GenericStep();
        step.setIdStep(idStep);
        $scope.stepList=[];
        $scope.stepList.push(step);
        $scope.onFileSelect(files,idStep);
    });

    it("should tell a connection problem",function(){
        $scope.base64Photo='Base Photo';
        var result={
            PhotoStored : true,
            Photo : true
        };
        $scope.$broadcast("CONNECTION_PROBLEM",result);
        expect(LocationManagerMock.getIdUserCookie).toHaveBeenCalled();
        expect(localStorageServiceMock.set).toHaveBeenCalled();
        expect(result["PhotoStored"]).toEqual('Base Photo');
        expect(result["Photo"]).toEqual('Base Photo');
    });

    it("should send content of local storage",function(){
        $scope.scrollOnTop = function(){};
        var idStep=0;
        var step=GenericStep();
        inject(function(){
            LocationManagerMock.getIdUserCookie.andReturn(idStep);
        });

        step.setIdStep(idStep);
        step.setState("TOVERIFY");
        step.setWrongGeolocation(false);
        step.contentLocalStorageData="";
        step.contentLocalStorageData.Latitude=true;
        $scope.stepList=[];
        $scope.stepList.push(step);
        $scope.sendContentLocalStorageData(idStep);
        expect(StandardUserRequestManagerMock.doSendUserStep).toHaveBeenCalled();
        expect($scope.messageData).toEqual("Dati locali del passo inviati con successo");

        expect($scope.stepList[0].localIsDefined).toEqual(undefined);
        expect($scope.stepList[0].messageNotification).toEqual(undefined);
        expect($scope.stepList[0].insertedText).toEqual(undefined);
        expect($scope.stepList[0].image).toEqual(undefined);
        expect($scope.stepList[0].mapList).toEqual(undefined);
        expect($scope.stepList[0].mapVisible).toEqual(undefined);
        expect($scope.stepList[0].contentLocalStorageData).toEqual(undefined);
        expect(localStorageServiceMock.remove).toHaveBeenCalled();
    });

    it("should send delete of local storage",function(){
        $scope.scrollOnTop = function(){};
        var idStep=0;
        var step=GenericStep();
        inject(function(){
            LocationManagerMock.getIdUserCookie.andReturn(idStep);
        });

        step.setIdStep(idStep);
        step.setState("TOVERIFY");
        step.setWrongGeolocation(false);
        step.contentLocalStorageData="";
        step.contentLocalStorageData.Latitude=true;
        $scope.stepList=[];
        $scope.stepList.push(step);
        $scope.deleteContentLocalStorageData(idStep);
        expect($scope.messageData).toEqual("Dati locali del passo eliminati con successo");

        expect(localStorageServiceMock.remove).toHaveBeenCalled();
    });

    it("should do geolocation not available broadcast",function(){
        var idStep=0;
        var step=GenericStep();
        var indexStep = 0;
        step.setIdStep(idStep);
        $scope.stepList = [];
        $scope.stepList.push(step);
        $scope.getGeolocationData(idStep);
        expect($scope.stepList[indexStep].messageInfoResultGeo).toEqual("Geolocalizzazione in corso...");
        step.setIdStep(idStep);
        $scope.getGeolocationData(idStep);
        $scope.$broadcast("GEOLOCATION_NOT_AVAILABLE",'');
    });
});