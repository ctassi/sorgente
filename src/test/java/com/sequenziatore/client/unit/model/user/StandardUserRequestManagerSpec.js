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
 * Name : StandardUserRequestManagerSpec.js
 * Target : StandardUserRequestManager
 * Location : /test/unit/model/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-22     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 * 0.0.2           2014-03-24     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Integrazione test
 * ======================================================
 * 0.0.3           2014-03-25     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Integrazione test
 * ======================================================
 */

describe('MODEL - StandardUserRequestManager', function () {
    var factory;
    var locationManager;
    var standardUser;
    var $httpBackend;
    var $rootScope;
    var $upload;

    // baseURL centralizzato per aggiornare i test con deploy su server diversi
    var baseURL = 'http://localhost:8080/seq/Sequenziatore';
    var response = {
        Data: 'someData',
        Confirmation: 'true',
        Processes: [{
           IdProcess: '1'
        },{
            IdProcess: '2'
        }]
    };

    var connectionErrorResponse = {
        Confirmation: 'connectionError'
    }

    beforeEach(function(){
        module('StandardUserRequestManager')

        inject(function($injector){
            factory = $injector.get('StandardUserRequestManager');
            locationManager = $injector.get('LocationManager');
            standardUser = $injector.get('StandardUser');
            $httpBackend = $injector.get('$httpBackend');
            $rootScope = $injector.get('$rootScope');
            $upload = $injector.get('$upload');
            spyOn($rootScope, '$broadcast').andCallThrough();
            spyOn(locationManager, 'getIdUserCookie').andReturn('5');
            spyOn(standardUser, 'setProcessList');
            spyOn(standardUser, 'setStepList');
        });
    });

    afterEach(function(){
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });


    it('should request a list of all active process and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/user/viewactiveprocesses').respond(201, connectionErrorResponse);
        factory.getAllActiveProcessList($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_ACTIVE_PROCESS_LIST_IS_END', 'connectionError');
    });

    it('should request a list of all active process and handle an error response', function(){
        var response = {
            Confirmation: 'noneSubscriptions',
            ListSubscriptions: [{
                IdProcess: '1'
            },{
                IdProcess: '2'
            }]
        }
        $httpBackend.when('POST', baseURL + '/user/viewactiveprocesses').respond(201, response);
        factory.getAllActiveProcessList($rootScope);
        $httpBackend.flush();
        expect(standardUser.setProcessList).toHaveBeenCalledWith(response['ListSubscriptions']);
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_ACTIVE_PROCESS_LIST_IS_END','Non sei iscritto ad alcun processo');
    });

    it('should get a list of all active process and broadcast a message', function(){
        var response = {
            Confirmation: 'subscriptions',
            ListSubscriptions: [{
                IdProcess: '1'
            },{
                IdProcess: '2'
            }]
        }
        $httpBackend.when('POST', baseURL + '/user/viewactiveprocesses').respond(201, response);
        factory.getAllActiveProcessList($rootScope);
        $httpBackend.flush();
        expect(standardUser.setProcessList).toHaveBeenCalledWith(response['ListSubscriptions']);
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_ACTIVE_PROCESS_LIST_IS_END','Ricevuta la lista processi');
    });

    // getUserInfo

    it('should get a list of user info and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/shared/viewaccount').respond(201, connectionErrorResponse);
        factory.getUserInfo($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_INFO_IS_END', 'connectionError');
    });

    it('should get a list of user info and handle an error response', function(){
        var response = {
            Confirmation: 'notReceivedInfo'
        }
        $httpBackend.when('POST', baseURL + '/shared/viewaccount').respond(201, response);
        factory.getUserInfo($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_INFO_IS_END', 'Informazio account non ricevute');
    });

    it('should get a list of user info and handle a success response', function(){
        var response = {
            Confirmation: 'receivedInfo'
        }
        $httpBackend.when('POST', baseURL + '/shared/viewaccount').respond(201, response);
        factory.getUserInfo($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_INFO_IS_END','Informazioni account ricevute correttamente');
    });

    // doLogout

    it('should reset user fields after logout and broadcast a message', function(){
        $httpBackend.when('POST', baseURL + '/shared/logout').respond(201, response);
        factory.doLogout();
        $httpBackend.flush();
    });

    // getAllAvailableProcessList

    it('should request a list of all available process and handle a connectionError response', function(){
        $httpBackend.when('POST', baseURL + '/user/availableprocess').respond(201, connectionErrorResponse);
        factory.getAllAvailableProcessList($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_AVAILABLE_PROCESS_IS_END', 'connectionError');
    });

    it('should request a list of all available process and handle an error response', function(){
        var response = {
            Confirmation: 'noneProcesses'
        }
        $httpBackend.when('POST', baseURL + '/user/availableprocess').respond(201, response);
        factory.getAllAvailableProcessList($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_AVAILABLE_PROCESS_IS_END', 'Non ci sono processi disponibili');
    });

    it('should get a list of all available process and broadcast a message', function(){
        var response = {
            Confirmation: 'processes',
            ListProcesses: [{
                IdProcess: '1'
            },{
                IdProcess: '2'
            }]
        }
        $httpBackend.when('POST', baseURL + '/user/availableprocess').respond(201, response);
        factory.getAllAvailableProcessList($rootScope);
        $httpBackend.flush();
        expect(standardUser.setProcessList).toHaveBeenCalledWith(response['ListProcesses']);
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_AVAILABLE_PROCESS_IS_END', 'Lista processi ricevuta correttamente');
    });

    // doSubscribeProcess

    it('should try to subscribe a user to a process and handle a connection error response', function(){
        var obj = {
            msg: "Problemi di connessione al server, contattare l\'amministratore'",
            success: 'false'
        }
        $httpBackend.when('POST', baseURL + '/user/subscribetoprocess').respond(201, connectionErrorResponse);
        factory.doSubscribeProcess($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_SUBSCRIBE_PROCESS_IS_END', obj);
    });

    it('should try to subscribe a user to a process and handle an error response', function(){
        var response = {
            Confirmation: 'wrongSubscription'
        };
        var obj = {
            msg: 'Spiacenti, si è verificato un errore',
            success: 'false'
        }
        $httpBackend.when('POST', baseURL + '/user/subscribetoprocess').respond(201, response);
        factory.doSubscribeProcess($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_SUBSCRIBE_PROCESS_IS_END', obj);
    });

    it('should try to subscribe a user to a process and handle a success response', function(){
        var response = {
            Confirmation: 'successSubscription'
        };
        var obj = {
            msg: 'Iscrizione al processo avvenuta con successo',
            success: 'true'
        }
        $httpBackend.when('POST', baseURL + '/user/subscribetoprocess').respond(201, response);
        factory.doSubscribeProcess($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_SUBSCRIBE_PROCESS_IS_END', obj);
    });

    // doGetNextStep

    it('should send a step request and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/user/viewstep').respond(201, connectionErrorResponse);
        factory.doGetNextStep($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_GET_NEXT_STEP_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should send a step request and handle a success response', function(){
        var response = {
            Confirmation: 'step',
            ProcessName: 'Galaxy guide',
            StepList: [{
                IdStep: '42'
            }]
        };
        $httpBackend.when('POST', baseURL + '/user/viewstep').respond(201, response);
        factory.doGetNextStep($rootScope, '42');
        $httpBackend.flush();
        expect(standardUser.setStepList).toHaveBeenCalledWith(response['StepList']);
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_GET_NEXT_STEP_IS_END', response);
    });

    // doSendUserStep

    it('should send a user step and handle a connection error response', function(){
        var obj = {
            Photo: 'dhbcksbckjbds',
            WrongPhoto: 'true'
        };
        $httpBackend.when('POST', baseURL + '/user/savedatastep').respond(201, connectionErrorResponse);
        factory.doSendUserStep($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('SEND_USER_STEP_IS_END', 'connectionError');
    });

    it('should send a user step and handle an error response', function(){
        var obj = {
            IdStep: '42'
        }
        var response = {
            Confirmation: 'notSendData'
        };
        $httpBackend.when('POST', baseURL + '/user/savedatastep').respond(201, response);
        factory.doSendUserStep($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('SEND_USER_STEP_IS_END', 'Dati non inviati');
    });

    it('should send a user step and handle a success response', function(){
        var obj = {
            IdStep: '42'
        }
        var response = {
            Confirmation: 'successSendData'
        };
        $httpBackend.when('POST', baseURL + '/user/savedatastep').respond(201, response);
        factory.doSendUserStep($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('SEND_USER_STEP_IS_END', 'successSendData');
    });

    it('should send a user step and handle a success autoVerify response', function(){
        var obj = {
            IdStep: '42'
        }
        var response = {
            Confirmation: 'successAutoVerify'
        };
        $httpBackend.when('POST', baseURL + '/user/savedatastep').respond(201, response);
        factory.doSendUserStep($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('SEND_USER_STEP_IS_END', 'successAutoVerify');
    });

    it('should send a user step and handle a success not auto verify response', function(){
        var obj = {
            IdStep: '42'
        }
        var response = {
            Confirmation: 'notSuccessAutoVerify'
        };
        $httpBackend.when('POST', baseURL + '/user/savedatastep').respond(201, response);
        factory.doSendUserStep($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('SEND_USER_STEP_IS_END', 'notSuccessAutoVerify');
    });

    // doModifyAccount

    it('should send a modify account request and handle a success response', function(){
        var obj = {};
        var response = {
            Confirmation: 'registrationOK'
        };
        $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
        factory.doModifyAccount($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', 'Modifiche apportate correttamente');
    });

    it('should send a modify account request and handle an email error response', function(){
        var obj = {};
        var response = {
            Confirmation: 'userAndMailNotAvailable'
        };
        $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
        factory.doModifyAccount($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', "Username ed email inseriti già presenti");
    });

    it('should send a modify account request and handle an username error response', function(){
        var obj = {};
        var response = {
            Confirmation: 'usernameNotAvailable'
        };
        $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
        factory.doModifyAccount($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', "Username inserito già presente");
    });

    it('should send a modify account request and handle an user/mail error response', function(){
        var obj = {};
        var response = {
            Confirmation: 'emailNotAvailable'
        };
        $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
        factory.doModifyAccount($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', "Email inserito già presente");
    });

    it('should send a modify account request and handle a password response', function(){
        var obj = {};
        var response = {
            Confirmation: 'wrongPassword'
        };
        $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, response);
        factory.doModifyAccount($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', "Password inserita non corrisponde alla tua attuale");
    });

    it('should send a modify account request and handle a connection error response', function(){
        var obj = {};
        $httpBackend.when('POST', baseURL + '/shared/modifyaccount').respond(201, connectionErrorResponse);
        factory.doModifyAccount($rootScope, obj);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('MODIFY_ACCOUNT_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    // doUnsubscribeProcess

    it('should send an unsubscribe request and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/user/unsubscribeprocess').respond(201, connectionErrorResponse);
        factory.doUnsubscribeProcess($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('UNSUBSCRIBE_PROCESS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should send an unsubscribe request and handle a success response', function(){
        var response = {
            Confirmation: 'successUnsubscribe'
        };
        $httpBackend.when('POST', baseURL + '/user/unsubscribeprocess').respond(201, response);
        factory.doUnsubscribeProcess($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('UNSUBSCRIBE_PROCESS_IS_END', "Disiscrizione non avvenuta con successo");
    });

    it('should send an unsubscribe request and handle an unsuccess response', function(){
        var response = {
            Confirmation: 'notSuccessUnsubscribe'
        };
        $httpBackend.when('POST', baseURL + '/user/unsubscribeprocess').respond(201, response);
        factory.doUnsubscribeProcess($rootScope, '42');
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('UNSUBSCRIBE_PROCESS_IS_END', "Disiscrizione avvenuta con successo");
    });

    // getStatisticsUser

    it('should send a statistics request and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/user/statisticsuser').respond(201, connectionErrorResponse);
        factory.getStatisticsUser($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_USER_STATISTICS_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should send a statistics request and handle a success response', function(){
        var response = {
            Confirmation: 'receivedStatistics'
        };
        $httpBackend.when('POST', baseURL + '/user/statisticsuser').respond(201, response);
        factory.getStatisticsUser($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_USER_STATISTICS_IS_END', response);
    });

    // getCloseProcess

    it('should send a closed process request and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/user/viewprocessnotactive').respond(201, connectionErrorResponse);
        factory.getCloseProcess($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_CLOSED_PROCESS_IS_END', 'connectionError');
    });

    it('should send a closed process request and handle an error response', function(){
        var response = {
            Confirmation: 'receivedCloseProcess'
        };
        $httpBackend.when('POST', baseURL + '/user/viewprocessnotactive').respond(201, response);
        factory.getCloseProcess($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_CLOSED_PROCESS_IS_END', response['ListProcess']);
    });

    it('should send a closed process request and handle an error response', function(){
        var response = {
            Confirmation: 'notReceivedCloseProcess'
        };
        $httpBackend.when('POST', baseURL + '/user/viewprocessnotactive').respond(201, response);
        factory.getCloseProcess($rootScope);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('GET_CLOSED_PROCESS_IS_END', 'Non ci sono processi chiusi');
    });
});