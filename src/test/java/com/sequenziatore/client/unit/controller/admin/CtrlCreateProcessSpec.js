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
 * Name : CtrlCreateProcessSpec.js
 * Target : CtrlCreateProcess
 * Location : /test/unit/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-20     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 * 0.0.2           2014-03-25     Pavanello Mirko
 * ------------------------------------------------------
 * Integrazione
 * ======================================================
 */
 
    describe('CONTROLLER -  CtrlCreateProcess', function (){
        var LocationManagerMock;
        var AdminUserRequestManagerMock;
        var index=0;

        beforeEach(function (){
         LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleCreateProcessLocation','isLogged','isAdmin']);
         AdminUserRequestManagerMock = jasmine.createSpyObj('AdminUserRequestManager',['doCreateNewProcess','getAdminInfo']);

         module('CtrlCreateProcess');

         inject (function($rootScope, $controller){
             // crea uno scope fittizio
             $scope = $rootScope.$new();
             $scope.closeAlert = function () {};
             $scope.scrollOnTop = function () {};

             LocationManagerMock.isLogged.andReturn("true");
             LocationManagerMock.isAdmin.andReturn("true");
             // crea un controller fittizio
             ctrl = $controller('CtrlCreateProcess', {
                 $scope : $scope,
                 LocationManager : LocationManagerMock,
                 AdminUserRequestManager : AdminUserRequestManagerMock
             });
         });
        });

        it('should handleCreateProcessLocation have been called and set variables"', function(){
         expect(LocationManagerMock.handleCreateProcessLocation).toHaveBeenCalled();
         expect($scope.processDivVisible).toEqual("true");
         expect($scope.levelDivVisible).toEqual("false");
         expect($scope.stepDivVisible).toEqual("false");
         expect($scope.summaryProcessDivVisible).toEqual("false");
         expect($scope.newStepList).toEqual([]);
         expect($scope.levelList).toEqual([]);
         expect(AdminUserRequestManagerMock.getAdminInfo).toHaveBeenCalled();
        });

        it('should add a step',function(){
         var emptyLevel = [];
         $scope.levelList.push(emptyLevel);
         $scope.addStep(index);
         expect($scope.levelList[index].messageStepOk).toEqual('');
         expect($scope.levelList[index].messageStepError).toEqual('Descrizione del passo non valida');
         $scope.levelList.slice(index,1);

         emptyLevel.stepDescription="DescriptionTest";
         $scope.levelList.push(emptyLevel);
         $scope.addStep(index);
         expect($scope.levelList[index].messageStepOk).toEqual('');
         expect($scope.levelList[index].messageStepError).toEqual('Verifica amministratore non impostata');
         $scope.levelList.slice(index,1);

         emptyLevel.stepDescription="DescriptionTest";
         emptyLevel.isAdmin="'AdminVerifyTest";
         $scope.levelList.push(emptyLevel);
         $scope.addStep(index);
         expect($scope.levelList[index].messageStepOk).toEqual('');
         expect($scope.levelList[index].messageStepError).toEqual('Verifica foto non impostata');
         $scope.levelList.slice(index,1);

         emptyLevel.stepDescription="DescriptionTest";
         emptyLevel.isAdmin="'AdminVerifyTest";
         emptyLevel.isPhoto="true";
         $scope.levelList.push(emptyLevel);
         $scope.addStep(index);
         expect($scope.levelList[index].messageStepOk).toEqual('');
         expect($scope.levelList[index].messageStepError).toEqual('Verifica testo non impostata');
         $scope.levelList.slice(index,1);

         emptyLevel.stepDescription="DescriptionTest";
         emptyLevel.isAdmin="'AdminVerifyTest";
         emptyLevel.isPhoto="true";
         emptyLevel.isText="true";
         $scope.levelList.push(emptyLevel);
         $scope.addStep(index);
         expect($scope.levelList[index].messageStepOk).toEqual('');
         expect($scope.levelList[index].messageStepError).toEqual('Testo di verifica non impostato');
         $scope.levelList.slice(index,1);

         emptyLevel.stepDescription="DescriptionTest";
         emptyLevel.isAdmin="'AdminVerifyTest";
         emptyLevel.isPhoto="true";
         emptyLevel.isText="true";
         emptyLevel.textCheck="true";
         $scope.levelList.push(emptyLevel);
         $scope.addStep(index);
         expect($scope.levelList[index].messageStepOk).toEqual('');
         expect($scope.levelList[index].messageStepError).toEqual('Verifica geolocalizzazione non impostata');
         $scope.levelList.slice(index,1);

         emptyLevel.stepDescription="DescriptionTest";
         emptyLevel.isAdmin="'AdminVerifyTest";
         emptyLevel.isPhoto="true";
         emptyLevel.isText="true";
         emptyLevel.textCheck="true";
         emptyLevel.isGeolocation="true";
         $scope.levelList.push(emptyLevel);
         $scope.addStep(index);
         expect($scope.levelList[index].messageStepOk).toEqual('');
         expect($scope.levelList[index].messageStepError).toEqual('Latitudine di verifica non impostata');
         $scope.levelList.slice(index,1);

         emptyLevel.stepDescription="DescriptionTest";
         emptyLevel.isAdmin="'AdminVerifyTest";
         emptyLevel.isPhoto="true";
         emptyLevel.isText="true";
         emptyLevel.textCheck="true";
         emptyLevel.isGeolocation="true";
         emptyLevel.latitudeCheck="true";
         $scope.levelList.push(emptyLevel);
         $scope.addStep(index);
         expect($scope.levelList[index].messageStepOk).toEqual('');
         expect($scope.levelList[index].messageStepError).toEqual('Longitudine di verifica non impostata');
         $scope.levelList.slice(index,1);

         emptyLevel.stepDescription="DescriptionTest";
         emptyLevel.isAdmin="'AdminVerifyTest";
         emptyLevel.isPhoto="false";
         emptyLevel.isText="false";
         emptyLevel.isGeolocation="false";
         $scope.levelList.push(emptyLevel);
         $scope.addStep(index);
         expect($scope.levelList[index].messageStepOk).toEqual('');
         expect($scope.levelList[index].messageStepError).toEqual('Non puoi inserire un passo senza alcun task richiesto');
         $scope.levelList.slice(index,1);

         emptyLevel.stepDescription="DescriptionTest";
         emptyLevel.isAdmin="'AdminVerifyTest";
         emptyLevel.isPhoto="true";
         emptyLevel.isText="true";
         emptyLevel.textCheck="true";
         emptyLevel.isGeolocation="true";
         emptyLevel.latitudeCheck="true";
         emptyLevel.longitudeCheck="true";


         $scope.levelList = [];
         $scope.levelList.push(emptyLevel);
         var newAdminUserStep = AdminUserStep();
         newAdminUserStep.setIndexStep(emptyLevel.length);	            //Set Index
         newAdminUserStep.setLevel(index);			                    //Set Level
         newAdminUserStep.setDescription(emptyLevel.stepDescription);	//Set Description
         newAdminUserStep.setAdminVerify(emptyLevel.isAdmin);			//Set IsAdmin
         newAdminUserStep.setIsPhoto(emptyLevel.isPhoto);				//Set IsPhoto
         newAdminUserStep.setIsText(emptyLevel.isText);					//Set IsText
         newAdminUserStep.setIsGeolocation(emptyLevel.isGeolocation);	//Set Geo


         if(newAdminUserStep.getIsText() === "true"){
             newAdminUserStep.setCheckText(emptyLevel.textCheck);
         }
         else{
             newAdminUserStep.setCheckText('null');
         }

         if(newAdminUserStep.getIsGeolocation() === "true"){
             newAdminUserStep.setCheckLatitude(emptyLevel.latitudeCheck);
             newAdminUserStep.setCheckLongitude(emptyLevel.longitudeCheck);
         }
         else{
             newAdminUserStep.setCheckLatitude('null');
             newAdminUserStep.setCheckLongitude('null');
         }

         $scope.addStep(index);
         expect($scope.levelList[index]).toEqual(emptyLevel);
         expect($scope.levelList[index].messageStepOk).toEqual("Passo: \"DescriptionTest\" inserito correttamente");
         expect($scope.levelList[index].parallelism).toEqual("NOT");
         expect($scope.levelList[index].messageStepError).toEqual("");
         expect($scope.messageSummaryProcessError).toEqual("");

        });

        it("should delete a step",function(){
         var indexLevel=0;
         var indexStep=0;
         var level = [];
         var step = AdminUserStep();
         step.setDescription("testDescription");
         level.push(step);
         $scope.levelList.push(level);
         spyOn(window, 'confirm').andReturn(true);
         $scope.deleteStep(indexStep,indexLevel);
         expect($scope.levelList[indexLevel][indexStep]).toEqual(undefined);
         expect($scope.levelList[indexLevel].buttonAddStepVisible).toEqual("true");
         expect($scope.levelList[indexLevel].buttonApplyModifyStepVisible).toEqual("false");
         expect($scope.levelList[indexLevel].buttonIgnoreModifyStepVisible).toEqual("false");
         expect($scope.levelList[indexLevel].creatingOrModifingStep).toEqual("Creazione passo:");
         expect($scope.levelList[indexLevel].messageStepOk).toEqual("");
         expect($scope.levelList[indexLevel].messageStepError).toEqual("");
        });

        it("should create an entire process",function(){

         var level=[];
         var level1=[];
         var step = AdminUserStep();
         var step1 = AdminUserStep();

         $scope.levelList=[];
         $scope.doCreateNewProcess();
         expect($scope.messageSummaryProcessError).toEqual("Un processo deve avere almeno un livello");
         expect($scope.messageSummaryProcessOk).toEqual("");


         $scope.levelList.push(level);
         $scope.doCreateNewProcess();
         expect($scope.messageSummaryProcessError).toEqual("Il livello 1 non ha alcun passo. Ogni livello deve avere almeno un passo");
         expect($scope.messageSummaryProcessOk).toEqual("");

         level.push(step);
         level.push(step1);
         $scope.levelList=[];
         $scope.levelList.push(level);
         $scope.levelList[0].parallelism="NOT";
         $scope.doCreateNewProcess();
         expect($scope.messageSummaryProcessError).toEqual("Il livello 1 non ha il tipo di parallelismo impostato");
         expect($scope.messageSummaryProcessOk).toEqual("");

         $scope.levelList=[];
         level=[];
         level1=[];
         level1.push(step1);
         level.push(step);
         $scope.levelList.push(level);
         $scope.levelList.push(level1);
         $scope.doCreateNewProcess();
         expect(AdminUserRequestManagerMock.doCreateNewProcess).toHaveBeenCalled();
         $scope.$broadcast("CREATE_PROCESS_IS_END",'Processo creato correttamente');
         expect($scope.messageSummaryProcessOk).toEqual("");

        });

        it('should verify that message error is  equal to "Nome processo non inserito"', function(){
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('Nome del processo non inserito');
         expect($scope.showAlertDetailsError).toEqual('true');
        });

        it('should verify that message error is  equal to "Descrizione processo non inserito"', function(){
         $scope.processName="Test Name Process";
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('Descrizione del processo non inserita');
         expect($scope.showAlertDetailsError).toEqual('true');
        });

        it('should verify that message error is  equal to "Data di inizio non selezionata"', function(){
         $scope.processName="Test Name Process";
         $scope.processDescription="Test Description Process";
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('Data di inizio non selezionata');
         expect($scope.showAlertDetailsError).toEqual('true');
        });

        it('should verify that message error is equal to "Data di chiusura non selezionata"', function(){
         $scope.processName="Test Name Process";
         $scope.processDescription="Test Description Process";
         $scope.dateStartToSend = new Date(new Date().setHours(0, 0, 0, 0));
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('Data di chiusura non selezionata');
         expect($scope.showAlertDetailsError).toEqual('true');
        });

        it('should verify that message error is  equal to "Data di fine iscrizione non selezionata"', function(){
         $scope.processName="Test Name Process";
         $scope.processDescription="Test Description Process";
         $scope.dateStartToSend = new Date(new Date().setHours(0, 0, 0, 0));
         $scope.dateCloseToSend= new Date(new Date().setHours(0, 0, 0, 0));
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('Data di fine iscrizione non selezionata');
         expect($scope.showAlertDetailsError).toEqual('true');
        });

        it('should have selected a wrong startDate', function(){
         $scope.processName="Test Name Process";
         $scope.processDescription="Test Description Process";
         $scope.dateStartToSend = new Date().setHours(0, 0, 0, 0)-2;
         $scope.dateCloseToSend = new Date().setHours(0, 0, 0, 0);
         $scope.dateEndSubscriptionToSend = new Date().setHours(0, 0, 0, 0);
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('La data di chiusura iscrizione deve essere compresa fra quella di inizio e quella fine processo');
         expect($scope.showAlertDetailsError).toEqual('true');
        });

        it('should have selected a wrong closeDate', function(){
         $scope.processName="Test Name Process";
         $scope.processDescription="Test Description Process";
         $scope.dateStartToSend = new Date().setHours(0, 0, 0, 0)+2;
         $scope.dateCloseToSend = new Date().setHours(0, 0, 0, 0)-1;
         $scope.dateEndSubscriptionToSend = new Date().setHours(0, 0, 0, 0);
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('La data di chiusura iscrizione deve essere compresa fra quella di inizio e quella fine processo');
         expect($scope.showAlertDetailsError).toEqual('true');
        });

        it('should have selected a wrong dateEndSubscription', function(){
         $scope.processName="Test Name Process";
         $scope.processDescription="Test Description Process";
         $scope.dateStartToSend = new Date().setHours(0, 0, 0, 0)+2;
         $scope.dateCloseToSend = new Date().setHours(0, 0, 0, 0)+4;
         $scope.dateEndSubscriptionToSend = new Date().setHours(0, 0, 0, 0)-1;
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('La data di chiusura iscrizione deve essere compresa fra quella di inizio e quella fine processo');
         expect($scope.showAlertDetailsError).toEqual('true');
        });


        it('should date have not a correct dateStartJSFormat', function(){
         $scope.processName="Test Name Process";
         $scope.processDescription="Test Description Process";
         $scope.dateStartToSend = new Date().setHours(0, 0, 0, 0)+2;
         $scope.dateCloseToSend = new Date().setHours(0, 0, 0, 0)+4;
         $scope.dateEndSubscriptionToSend = new Date().setHours(0, 0, 0, 0)+3;
         var date = new Date();
         date.setDate(date.getDate()-1);
         $scope.dateStartJSFormat=$scope.yearMonthDayToJSDate(date.getFullYear(),date.getMonth(),date.getDate());
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('La data di chiusura iscrizione deve essere compresa fra quella di inizio e quella fine processo');
         expect($scope.showAlertDetailsError).toEqual('true');
        });

        it('should date have not a correct dateCloseJSFormat', function(){
         $scope.processName="Test Name Process";
         $scope.processDescription="Test Description Process";
         $scope.dateStartToSend = new Date().setHours(0, 0, 0, 0)+2;
         $scope.dateCloseToSend = new Date().setHours(0, 0, 0, 0)+4;
         $scope.dateEndSubscriptionToSend = new Date().setHours(0, 0, 0, 0)+3;
         var date = new Date();
         date.setDate(date.getDate()+2);
         $scope.dateStartJSFormat=$scope.yearMonthDayToJSDate(date.getFullYear(),date.getMonth(),date.getDate());
         date.setDate(date.getDate()-1);
         $scope.dateCloseJSFormat=$scope.yearMonthDayToJSDate(date.getFullYear(),date.getMonth(),date.getDate());
         $scope.createNewProcess();
         expect($scope.messageDetailsProcessError).toEqual('La data di chiusura deve essere una data successiva a quella di inizio');
         expect($scope.showAlertDetailsError).toEqual('true');
        });


        it('should have inserted the right information', function(){
         $scope.processName="Test Name Process";
         $scope.processDescription="Test Description Process";
         $scope.dateStartToSend = new Date().setHours(0, 0, 0, 0)+2;
         $scope.dateCloseToSend = new Date().setHours(0, 0, 0, 0)+4;
         $scope.dateEndSubscriptionToSend = new Date().setHours(0, 0, 0, 0)+3;
         var date = new Date();
         date.setDate(date.getDate()-2);
         $scope.dateStartJSFormat=$scope.yearMonthDayToJSDate(date.getFullYear(),date.getMonth(),date.getDate());
         date.setDate(date.getDate()+10);
         $scope.dateCloseJSFormat=$scope.yearMonthDayToJSDate(date.getFullYear(),date.getMonth(),date.getDate());
         date.setDate(date.getDate()+4);
         $scope.dateEndSubscriptionJSFormat=$scope.yearMonthDayToJSDate(date.getFullYear(),date.getMonth(),date.getDate());
         $scope.createNewProcess();
         expect($scope.processDivVisible).toEqual('true');
         expect($scope.summaryProcessDivVisible).toEqual('false');
        });


        it('should insert a new empty level ', function(){
         var emptyLevel=[];
         emptyLevel.stepDivVisible = 'false';
         emptyLevel.symbol = '+';
         $scope.insertNewLevel();
         expect($scope.levelDivVisible).toEqual('true');

        });

        it('should not show elements in the view for insertNewStep function',function(){
         var emptyLevel=[];
         emptyLevel.stepDivVisible = 'notFalse';
         emptyLevel.symbol = '+';
         $scope.levelList.push(emptyLevel);
         $scope.insertNewStep(index);
         expect($scope.levelList[index].stepDivVisible).toEqual('false');
         expect($scope.levelList[index].symbol).toEqual('+');
        });


        it('should show elements in the view for insertNewStep function without show buttonAddStepVisibile',function(){
         var emptyLevel=[];
         emptyLevel.stepDivVisible = 'false';
         emptyLevel.symbol = '+';
         emptyLevel.buttonAddStepVisible = 'false';
         $scope.levelList.push(emptyLevel);
         $scope.insertNewStep(index);
         expect($scope.levelList[index].buttonApplyModifyStepVisible).toEqual('true');
         expect($scope.levelList[index].buttonIgnoreModifyStepVisible).toEqual('true');
         expect($scope.levelList[index].creatingOrModifingStep).toEqual('Modifica passo:');
         expect($scope.levelList[index].symbol).toEqual('-');

        });


        it('should show elements in the view for insertNewStep function and show buttonAddStepVisibile',function(){
         var emptyLevel=[];
         emptyLevel.stepDivVisible = 'false';
         emptyLevel.symbol = '+';
         emptyLevel.buttonAddStepVisible = 'false';
         $scope.levelList.push(emptyLevel);
         $scope.levelList[index].stepDivVisible='false';
         $scope.levelList[index].buttonAddStepVisible = 'true';
         $scope.insertNewStep(index);
         expect($scope.levelList[index].buttonAddStepVisible).toEqual('true');
         expect($scope.levelList[index].buttonApplyModifyStepVisible).toEqual('false');
         expect($scope.levelList[index].buttonIgnoreModifyStepVisible).toEqual('false');
         expect($scope.levelList[index].creatingOrModifingStep).toEqual('Creazione passo:');
         expect($scope.levelList[index].symbol).toEqual('-');

        });

        it('should delete a level',function(){
         var emptyLevel = [];
         $scope.levelList.push(emptyLevel);
         spyOn(window, 'confirm').andReturn(true);
         $scope.deleteLevel(index);
         expect($scope.levelList).toEqual([]);
         expect($scope.messageSummaryProcessError).toEqual("");
        });

        it("should do a step modify",function(){
         var indexLevel = 0;
         var indexStep = 0;
         var level = [];
         var step = AdminUserStep();

         spyOn(window, 'confirm').andReturn(true);
         step.setIsText("true");
         step.setIsGeolocation("true");
         step.setDescription("testDescription");
         step.setAdminVerify("true");
         step.setIsPhoto("true");
         step.setCheckText("testCheckText");
         step.setCheckLongitude("testCheckLongitude");
         step.setCheckLatitude("testCheckLatitude");
         level.push(step);
         $scope.levelList.push(level);
         $scope.modifyStep(indexStep,indexLevel);

         expect($scope.levelList[indexLevel].stepDivVisible).toEqual("true");
         expect($scope.levelList[indexLevel].buttonAddStepVisible).toEqual("false");
         expect($scope.levelList[indexLevel].buttonApplyModifyStepVisible).toEqual("true");
         expect($scope.levelList[indexLevel].buttonIgnoreModifyStepVisible).toEqual("true");
         expect($scope.levelList[indexLevel].creatingOrModifingStep).toEqual("Modifica passo:");
         expect($scope.levelList[indexLevel].stepDescription).toEqual("testDescription");
         expect($scope.levelList[indexLevel].isAdmin).toEqual("true");
         expect($scope.levelList[indexLevel].isPhoto).toEqual("true");
         expect($scope.levelList[indexLevel].isText).toEqual("true");
         expect($scope.levelList[indexLevel].isGeolocation).toEqual("true");
         expect($scope.levelList[indexLevel].textCheck).toEqual("testCheckText");
         expect($scope.levelList[indexLevel].longitudeCheck).toEqual("testCheckLongitude");
         expect($scope.levelList[indexLevel].latitudeCheck).toEqual("testCheckLatitude");
         expect($scope.levelList[indexLevel].messageStepOk).toEqual("");
         expect($scope.levelList[indexLevel].messageStepError).toEqual("");


         /*test private method applyModifyStep*/
         step = AdminUserStep();
         level = [];
         $scope.levelList = [];
         level.push(step);
         $scope.levelList.push(level);
         $scope.levelList[indexLevel].isText = 'false';
         $scope.levelList[indexLevel].isGeolocation = 'false';
         $scope.levelList[indexLevel].isPhoto = 'false';
         $scope.levelList[indexLevel].stepDescription = "stepDescriptionApplyModify";
         $scope.levelList[indexLevel].isAdmin = "true";
         $scope.levelList[indexLevel].textCheck = "textCheckTest";
         $scope.levelList[indexLevel].longitudeCheck = "longitudeCheckTest";
         $scope.levelList[indexLevel].latitudeCheck = "latitudeCheckTest";

         /*throw the exception*/
         $scope.applyModifyStep(indexLevel);
         expect($scope.levelList[indexLevel].messageStepOk).toEqual("");
         expect($scope.levelList[indexLevel].messageStepError).toEqual("Un passo deve richiedere almeno una task.");
         /*don't throw the exception and do the entire applyModifyStep*/
         $scope.levelList[indexLevel].isText = 'true';
         $scope.levelList[indexLevel].isGeolocation = 'true';
         $scope.applyModifyStep(indexLevel);
         expect($scope.levelList[indexLevel][indexStep].getDescription()).toEqual("stepDescriptionApplyModify");
         expect($scope.levelList[indexLevel][indexStep].getAdminVerify()).toEqual("true");
         expect($scope.levelList[indexLevel][indexStep].getIsPhoto()).toEqual("false");
         expect($scope.levelList[indexLevel][indexStep].getIsText()).toEqual("true");
         expect($scope.levelList[indexLevel][indexStep].getIsGeolocation()).toEqual("true");
         expect($scope.levelList[indexLevel][indexStep].getCheckText()).toEqual("textCheckTest");
         expect($scope.levelList[indexLevel][indexStep].getCheckLatitude()).toEqual("latitudeCheckTest");
         expect($scope.levelList[indexLevel][indexStep].getCheckLongitude()).toEqual("longitudeCheckTest");
         expect($scope.levelList[indexLevel].creatingOrModifingStep).toEqual("Aggiungi passo:");
         expect($scope.levelList[indexLevel].buttonAddStepVisible).toEqual("true");
         expect($scope.levelList[indexLevel].buttonApplyModifyStepVisible).toEqual("false");
         expect($scope.levelList[indexLevel].buttonIgnoreModifyStepVisible).toEqual("false");
         expect($scope.levelList[indexLevel].messageStepOk).toEqual("Passo modificato correttamente");
         expect($scope.levelList[indexLevel].messageStepError).toEqual("");

         /*test private method ignoreModifyStep*/
         level = [];
         $scope.levelList = [];
         $scope.levelList.push(level);
         $scope.ignoreModifyStep(indexLevel);
         expect($scope.levelList[indexLevel].buttonAddStepVisible).toEqual("true");
         expect($scope.levelList[indexLevel].buttonApplyModifyStepVisible).toEqual("false");
         expect($scope.levelList[indexLevel].buttonIgnoreModifyStepVisible).toEqual("false");
         expect($scope.levelList[indexLevel].creatingOrModifingStep).toEqual("Creazione passo:");
         expect($scope.messageStepOk).toEqual("");
         expect($scope.messageStepError).toEqual("");
        });

        it("should reset the fields of a level",function(){
         var level = [];
         var indexLevel = 0;
         level.stepDescription = "stepDescriptionTest";
         level.isAdmin = "true";
         level.isPhoto = "true";
         level.isText = "true";
         level.isGeolocation = "true";
         level.textCheck = "textCheckTest";
         level.latitudeCheck = "latitudeCheckTest";
         level.longitudeCheck = "longitudeCheckTest";
         $scope.levelList.push(level);
         $scope.resetFields(indexLevel);
         expect($scope.levelList[indexLevel].stepDescription).toEqual(undefined);
         expect($scope.levelList[indexLevel].isAdmin).toEqual(undefined);
         expect($scope.levelList[indexLevel].isPhoto).toEqual(undefined);
         expect($scope.levelList[indexLevel].isText).toEqual(undefined);
         expect($scope.levelList[indexLevel].isGeolocation).toEqual(undefined);
         expect($scope.levelList[indexLevel].textCheck).toEqual(undefined);
         expect($scope.levelList[indexLevel].latitudeCheck).toEqual(undefined);
         expect($scope.levelList[indexLevel].longitudeCheck).toEqual(undefined);
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
         expect($scope.dateStartView).toEqual("03/07/2014");
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
         expect($scope.dateCloseView).toEqual("03/07/2014");
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
         expect($scope.dateEndSubscriptionView).toEqual("03/07/2014");
         expect($scope.dateEndSubscriptionToSend).toEqual(toSend);
         expect($scope.dateEndSubscriptionVisible).toEqual(undefined);
        });
    });