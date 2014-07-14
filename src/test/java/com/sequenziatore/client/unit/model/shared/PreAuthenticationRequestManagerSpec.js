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
 * Name : PreAuthenticationRequestManagerSpec.js
 * Target : PreAuthenticationRequestManager
 * Location : /test/unit/model/shared
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-23     Romagnosi Nicolò
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('MODEL - PreAuthenticationRequestManager', function () {
    var factory;
    var locationManager;
    var standardUser;
    var adminUser;
    var $httpBackend;
    var $rootScope;

    // baseURL centralizzato per aggiornare i test con deploy su server diversi
    var baseURL = 'http://localhost:8080/seq/Sequenziatore';
    var dataForm = {
        user: 'TestUser',
        password: 'TestPassword',
        email: 'TestEmail'
    };

    var connectionErrorResponse = {
        Confirmation: 'connectionError'
    };

    beforeEach(function(){
        module('PreAuthenticationRequestManager')

        inject(function($injector){
            factory = $injector.get('PreAuthenticationRequestManager');
            locationManager = $injector.get('LocationManager');
            standardUser = $injector.get('StandardUser');
            adminUser = $injector.get('AdminUser');

            $httpBackend = $injector.get('$httpBackend');

            $rootScope = $injector.get('$rootScope');
            spyOn($rootScope, '$broadcast').andCallThrough();
            spyOn(standardUser, 'build');
            spyOn(adminUser, 'build');
        });
    });

    afterEach(function(){
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });

    // doRegistration

    it('should send an admin registration request and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/shared/userregistration').respond(201, connectionErrorResponse);
        factory.doRegistration($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_REGISTRATION_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should send an admin registration request and handle a success response', function(){
        var response = {
            Confirmation: 'registrationOK',
            isAdmin: 'false'
        };
        $httpBackend.when('POST', baseURL + '/shared/userregistration').respond(201, response);
        factory.doRegistration($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_REGISTRATION_IS_END', 'Username registrato correttamente');
    });

    it('should send an admin registration request and handle an user/mail error response', function(){
        var response = {
            Confirmation: 'userAndMailNotAvailable',
            isAdmin: 'false'
        };
        $httpBackend.when('POST', baseURL + '/shared/userregistration').respond(201, response);
        factory.doRegistration($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_REGISTRATION_IS_END', "Username ed email già presenti");
    });

    it('should send an admin registration request and handle an username error response', function(){
        var response = {
            Confirmation: 'usernameNotAvailable',
            isAdmin: 'false'
        };
        $httpBackend.when('POST', baseURL + '/shared/userregistration').respond(201, response);
        factory.doRegistration($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_REGISTRATION_IS_END', "Username già presente");
    });

    it('should send an admin registration request and handle an email error response', function(){
        var response = {
            Confirmation: 'emailNotAvailable',
            isAdmin: 'false'
        };
        $httpBackend.when('POST', baseURL + '/shared/userregistration').respond(201, response);
        factory.doRegistration($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_REGISTRATION_IS_END', "Email già presente");
    });

    // doFacebookLogin

    it('should send a facebook login request and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/shared/facebooklogin').respond(201, connectionErrorResponse);
        factory.doFacebookLogin($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_LOGIN_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should send a facebook login request and handle an error response', function(){
        var response = {
            Confirmation: 'wrongAuthentication'
        };
        $httpBackend.when('POST', baseURL + '/shared/facebooklogin').respond(201, response);
        factory.doFacebookLogin($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_LOGIN_IS_END', 'Username o Password inseriti non corretti');
    });

    // doLogin

    it('should send a login request and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/shared/login').respond(201, connectionErrorResponse);
        factory.doLogin($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_LOGIN_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });

    it('should send a login request and handle an error response', function(){
        var response = {
            Confirmation: 'wrongAuthentication'
        };
        $httpBackend.when('POST', baseURL + '/shared/login').respond(201, response);
        factory.doLogin($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_LOGIN_IS_END', 'Username o Password inseriti non corretti');
    });

    // doRecoveryPassword

    it('should do a recovery password and handle a success response', function(){
        var response = {
            Confirmation: 'successRecovery'
        };
        $httpBackend.when('POST', baseURL + '/shared/recoverypassword').respond(201, response);
        factory.doRecoveryPassword($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_RECOVERY_PASSWORD_IS_END', 'Richiesta di reset password inviata con successo. Controlla la tua casella mail');
    });

    it('should do a recovery password and handle an error response', function(){
        var response = {
            Confirmation: 'wrongRecovery'
        };
        $httpBackend.when('POST', baseURL + '/shared/recoverypassword').respond(201, response);
        factory.doRecoveryPassword($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_RECOVERY_PASSWORD_IS_END', 'Utente non trovato, controllare lo username inserito');
    });

    it('should send a recovery password request and handle an internet connection error response', function(){
        var response = {
            Confirmation: 'internetConnectionError'
        };
        $httpBackend.when('POST', baseURL + '/shared/recoverypassword').respond(201, response);
        factory.doRecoveryPassword($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_RECOVERY_PASSWORD_IS_END', 'Errore nell\'invio dell\'e-mail con la nuova password, riprovare più tardi');
    });

    it('should send a recovery password request and handle a connection error response', function(){
        $httpBackend.when('POST', baseURL + '/shared/recoverypassword').respond(201, {Confirmation: 'connectionError'});
        factory.doRecoveryPassword($rootScope, dataForm);
        $httpBackend.flush();
        expect($rootScope.$broadcast).toHaveBeenCalledWith('REQUEST_RECOVERY_PASSWORD_IS_END', 'Problemi di connessione al server, contattare l\'amministratore');
    });
});