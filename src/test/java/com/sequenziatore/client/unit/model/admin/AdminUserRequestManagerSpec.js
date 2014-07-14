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
 * Name : AdminUserRequestManagerSpec.js
 * Target : AdminUserRequestManager
 * Location : /test/unit/model/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-23     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('MODEL - AdminUserRequestManager', function () {
    var factory;
    var locationManager;
    var adminUser;
    var $httpBackend;
    var $rootScope;

    // baseURL centralizzato per aggiornare i test con deploy su server diversi
    var baseURL = 'http://localhost:8080/seq/Sequenziatore';

    var connectionErrorResponse = {
        Confirmation: 'connectionError'
    };

    beforeEach(function(){
        module('AdminUserRequestManager')

        inject(function($injector){
            factory = $injector.get('AdminUserRequestManager');
            locationManager = $injector.get('LocationManager');
            adminUser = $injector.get('AdminUser');
            $httpBackend = $injector.get('$httpBackend');
            $rootScope = $injector.get('$rootScope');
            spyOn($rootScope, '$broadcast').andCallThrough();
            spyOn(adminUser, 'setUserList');
            spyOn(adminUser, 'setStepList');
            spyOn(adminUser, 'setProcessList');
        });
    });

    afterEach(function(){
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });

    it('should request admin info and handle an not received info response', function(){
        var response = {
            Confirmation: 'notReceivedInfo'
        }
        $httpBackend.when('POST', baseURL + '/shared/viewaccount').respond(201, response);
        factory.getAdminInfo($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_INFO_IS_END', 'Informazioni account non ricevute');
    });

    it('should request admin info and handle an error response', function(){
        $httpBackend.when('POST', baseURL + '/shared/viewaccount').respond(201, connectionErrorResponse);
        factory.getAdminInfo($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalled();
    });

    it('should request admin info and handle an received info response', function(){
        var response = {
            Confirmation: 'receivedInfo'
        }
        $httpBackend.when('POST', baseURL + '/shared/viewaccount').respond(201, response);
        factory.getAdminInfo($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_INFO_IS_END','Informazioni account ricevute correttamente');
    });

    it('should request an admin active process list and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/admin/viewadminprocessactive').respond(201, connectionErrorResponse);
        factory.getAllAdminCreatedActiveProcessList($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_ALL_ADMIN_CREATED_ACTIVE_PROCESS_IS_END', 'connectionError');
    });

    it('should request an admin active process list and handle a none active process response', function(){
        var response = {
            Confirmation: 'noneActiveProcess'
        }
        $httpBackend.when('POST', baseURL + '/admin/viewadminprocessactive').respond(201, response);
        factory.getAllAdminCreatedActiveProcessList($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_ALL_ADMIN_CREATED_ACTIVE_PROCESS_IS_END', 'Non ci sono processi attivi');
    });

    it('should request an admin active process list and handle a success response', function(){
        var response = {
            Confirmation: 'activeProcess'
        }
        $httpBackend.when('POST', baseURL + '/admin/viewadminprocessactive').respond(201, response);
        factory.getAllAdminCreatedActiveProcessList($rootScope);
        $httpBackend.flush();
        expect(adminUser.setProcessList).toHaveBeenCalled();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_ALL_ADMIN_CREATED_ACTIVE_PROCESS_IS_END','Processi attivi trovati correttamente');
    });

    it('should request a list of step to be validated and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/admin/steplistvalidation').respond(201, connectionErrorResponse);
        factory.getAllStepsToBeValidatedList($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_ALL_STEPS_TO_BE_VALIDATED_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should request a list of step to be validated and broadcast a message', function(){
        var response = {
            Confirmation: 'steps',
            Collections:[{
                IdCollection: '1'
            }]
        }
        $httpBackend.when('POST', baseURL + '/admin/steplistvalidation').respond(201, response);
        factory.getAllStepsToBeValidatedList($rootScope);
        $httpBackend.flush();
        expect(adminUser.setStepList).toHaveBeenCalled();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_ALL_STEPS_TO_BE_VALIDATED_IS_END', 'Passi da validare ricevuti correttamente');
    });

    it("should request the admin's processes statistics and handle a connection error response", function(){
        $httpBackend.when('POST', baseURL + '/admin/statisticsadmin').respond(201, connectionErrorResponse);
        factory.getStatisticsAdmin($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_ADMIN_STATISTICS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it("should request the admin's processes statistics", function(){
        var obj = [{
            data: 'someData'
        }];
        response = {
            Confirmation: 'receivedStatistics'
        }
        $httpBackend.when('POST', baseURL + '/admin/statisticsadmin').respond(201, response);
        factory.getStatisticsAdmin($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_ADMIN_STATISTICS_IS_END', response);
    });

    it('should request the list of users which are subscribed to a process', function(){
        response = {
            Confirmation: 'noneUser'
        }
        $httpBackend.when('POST', baseURL + '/admin/listuserprocess').respond(201, response);
        factory.getListUserProcess($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_LIST_USER_PROCESS_IS_END', 'Non ci sono utenti iscritti');
    });

    it("should request the list of user which are subscripted to any process and handle an empty list response", function(){
        $httpBackend.when('POST', baseURL + '/admin/listuserprocess').respond(201, connectionErrorResponse);
        factory.getListUserProcess($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_LIST_USER_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it("should request the list of user which are subscripted to any process", function(){
        var obj = [{
            Confirmation: 'users',
            ListUser:[
                {
                    userId: '1'
                }]
        }];
        $httpBackend.when('POST', baseURL + '/admin/listuserprocess').respond(201, obj);
        factory.getListUserProcess($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalled();
    });

    it('should send a modify account request and handle a success response', function(){
        var obj = {
            data : 'values'
        };
        var response = {
            Confirmation: 'registrationOK'
        };
        $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
        factory.doModifyAccount($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', 'Modifiche apportate correttamente');
    });

     it('should send a modify account request and handle an username error response', function(){
         var obj = {
            data : 'values'
         };
         var response = {
            Confirmation: 'usernameNotAvailable'
         };
         $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
         factory.doModifyAccount($rootScope, obj);
         $httpBackend.flush();
         expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', "Username inserito già presente");
     });

     it('should send a modify account request and handle an email error response', function(){
         var obj = {
            data : 'values'
         };
         var response = {
            Confirmation: 'emailNotAvailable'
         };
         $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
         factory.doModifyAccount($rootScope, obj);
         $httpBackend.flush();
         expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', "Email inserito già presente");
     });

     it('should send a modify account request and handle a password error response', function(){
         var obj = {
            data : 'values'
         };
         var response = {
            Confirmation: 'wrongPassword'
         };
         $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
         factory.doModifyAccount($rootScope, obj);
         $httpBackend.flush();
         expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', "Password inserita non corrisponde alla tua attuale");
     });

     it('should send a modify account request and handle a success response', function(){
         var obj = {
            data : 'values'
         };
         $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, connectionErrorResponse);
         factory.doModifyAccount($rootScope, obj);
         $httpBackend.flush();
         expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
     });

    it('should send a modify account request and handle an email error response', function(){
        var obj = {
            data : 'values'
        };
        var response = {
            Confirmation: 'userAndMailNotAvailable'
        };
        $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
        factory.doModifyAccount($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', "Username ed email inseriti già presenti");
    });


     it('should send a process modify request and handle a connection error response', function(){
         var obj = {
            data : 'values'
         };
         $httpBackend.when('POST', baseURL + '/admin/modifyprocess').respond(201, connectionErrorResponse);
         factory.doModifyProcess($rootScope, obj);
         $httpBackend.flush();
         expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
     });

     it('should send a process modify request and handle a success response', function(){
         var obj = {
             data : 'values'
         };
         var response = {
            Confirmation: 'receivedInfo'
         };
         $httpBackend.when('POST', baseURL + '/admin/modifyprocess').respond(201, response);
         factory.doModifyProcess($rootScope, obj);
         $httpBackend.flush();
         expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_PROCESS_IS_END', 'Modifiche inserite correttamente');
     });

    // viewAdminProcessNotActive

    it('should request all not active processes and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/admin/viewadminprocessnotactive').respond(201, connectionErrorResponse);
        factory.viewAdminProcessNotActive($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should request all not active processes and handle a success response', function(){
        var response = {
            Confirmation: 'noneProcess'
        };
        $httpBackend.when('POST', baseURL + '/admin/viewadminprocessnotactive').respond(201, response);
        factory.viewAdminProcessNotActive($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END', 'Non ci sono processi terminati');
    });

    it('should request all not active processes and handle a success response', function(){
        var response = {
            Confirmation: 'process',
            ListProcess:[{
                IdProcess: '42'
            }]
        };
        $httpBackend.when('POST', baseURL + '/admin/viewadminprocessnotactive').respond(201, response);
        factory.viewAdminProcessNotActive($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('VIEW_ADMIN_PROCESS_NOT_ACTIVE_IS_END', response['ListProcess']);
    });


    // doCreateNewProcess

    it('should send process creation request and handle a connection error response', function(){
        var obj = {};
        $httpBackend.when('POST', baseURL + '/admin/createprocess').respond(201, connectionErrorResponse);
        factory.doCreateNewProcess($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('CREATE_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    // doStepValidation

    it('should send a step validation request and handle a connection error response', function(){
        var obj = {
            IdUser: '42'
        };
        $httpBackend.when('POST', baseURL + '/admin/stepvalidation').respond(201, connectionErrorResponse);
        factory.doStepValidation($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('VALIDATION_STEP_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should send a step validation request and handle a success response', function(){
        var obj = {
            IdUser: '42'
        };
        var response = {
            Confirmation: 'wrongValidation'
        };
        $httpBackend.when('POST', baseURL + '/admin/stepvalidation').respond(201, response);
        factory.doStepValidation($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('VALIDATION_STEP_IS_END', 'L\'utente si era già disiscritto');
    });

    // getProcess

    it('should send a process request and handle a success response', function(){
        $httpBackend.when('POST', baseURL + '/admin/viewprocess').respond(201, connectionErrorResponse);
        factory.getProcess($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should send process request and handle a success response', function(){
        var response = {
            Confirmation: 'successGetProcess'
        };
        $httpBackend.when('POST', baseURL + '/admin/viewprocess').respond(201, response);
        factory.getProcess($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_PROCESS_IS_END', response);
    });

    // getReport

    it('should send a report request and handle a success response', function(){
        var response = {
            Confirmation: 'SomebodyIsSubscribed'
        };
        $httpBackend.when('POST', baseURL + '/admin/report').respond(201, response);
        factory.getReport($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_REPORT_IS_END',response);
    });

    it('should send a report request and handle an error response', function(){
        var response = {
            Confirmation: 'NoOneIsSubscribed'
        };
        $httpBackend.when('POST', baseURL + '/admin/report').respond(201, response);
        factory.getReport($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalled();
    });

    it('should send a report request and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/admin/report').respond(201, connectionErrorResponse);
        factory.getReport($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_REPORT_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });
});