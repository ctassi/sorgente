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
 * Name : CtrlRegistrationNewAdminSpec.js
 * Target : CtrlRegistrationNewAdmin
 * Location : /test/unit/controller/admin
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-22     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe('CONTROLLER - CtrlRegistrationNewAdmin', function (){
    var AdminUserRequestManagerMock;
    var RegularExpressionsMock;
    var LocationManagerMock;
    var PreAuthenticationRequestManagerMock;
    var DistrictListMock;

    beforeEach(function (){
        RegularExpressionsMock = jasmine.createSpyObj('RegularExpressions',['test']);
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleRegistrationNewAdminLocation','isLogged','isAdmin']);
        AdminUserRequestManagerMock = jasmine.createSpyObj('AdminUserRequestManager',['getAdminInfo']);
        PreAuthenticationRequestManagerMock = jasmine.createSpyObj('PreAuthenticationRequestManager',['doRegistration']);
        DistrictListMock = jasmine.createSpyObj('DistrictList',['']);
        DistrictListMock.DistrictList = [] ;

        RegularExpressionsMock.regularExpressionUsername = new RegExp('^[a-zA-Z][a-zA-Z0-9]+([a-zA-Z0-9]+|[a-zA-Z0-9]*)[a-zA-Z0-9]$');
        RegularExpressionsMock.regularExpressionEmail = new RegExp('^[a-zA-Z0-9][a-zA-Z0-9]+(([.]?[_]?[-]?[.]?)+[a-zA-Z0-9]+|[a-zA-Z0-9]*)[@][a-zA-Z0-9]+[.]([a-z]{2,3})$');
        RegularExpressionsMock.regularExpressionWhiteSpaces = new RegExp('\s');
        RegularExpressionsMock.regularExpressionPassword = new RegExp('');
        RegularExpressionsMock.regularExpressionName = new RegExp('');
        RegularExpressionsMock.regularExpressionSurname = new RegExp('');

        module('CtrlRegistrationNewAdmin');

        inject (function($rootScope, $controller){
            // Creo uno scope fittizio
            $scope = $rootScope.$new();
            $scope.openWebSocket = function(){};
            $scope.closeWebSocket = function(){};
            $scope.scrollOnTop = function() {};

            LocationManagerMock.isLogged.andReturn("true");
            LocationManagerMock.isAdmin.andReturn("true");

            // Creo un controller fittizio
            ctrl = $controller('CtrlRegistrationNewAdmin', {
                $scope: $scope,
                AdminUserRequestManager : AdminUserRequestManagerMock,
                LocationManager : LocationManagerMock,
                RegularExpressions : RegularExpressionsMock,
                PreAuthenticationRequestManager : PreAuthenticationRequestManagerMock,
                DistrictList : DistrictListMock
            });
        });
    });

    it ("should handleRegistrationNewAdminLocation have been called, do a REQUEST_INFO_IS_END and call closeAlert",function(){
        expect(LocationManagerMock.handleRegistrationNewAdminLocation).toHaveBeenCalled();
        $scope.$broadcast('REQUEST_INFO_IS_END','Registration Is End');
        $scope.closeAlert();
        expect($scope.showAlertS).toEqual("false");
        expect($scope.showAlertD).toEqual("false");
    });
    it ("should check if the username is write in a correct form",function(){

        usr="Usr";
        $scope.username=usr;
        $scope.checkUsername();
        expect($scope.errorUsername).toEqual('Lo Username deve essere minimo 4 caratteri.');

        usr="testUsr!";
        $scope.username=usr;
        $scope.checkUsername();
        expect($scope.errorUsername).toEqual('Lo Username non può contenere caratteri speciali o spazi.');

        usr="testUsername";
        $scope.username=usr;
        $scope.checkUsername();
        expect($scope.errorUsername).toEqual(undefined);
    });

    it ("should check if the password is write in a correct form",function(){

        var psw="";
        $scope.password=psw;
        $scope.errorPassword="";
        $scope.okPassword="";
        try{
            $scope.checkPassword();
        }
        catch(test){
            expect(test).toEqual('Errore inserimento password');
        }

        psw="test";
        $scope.password=psw;
        $scope.errorPassword="";
        $scope.okPassword="";
        try{
            $scope.checkPassword();
        }
        catch(test){
            expect($scope.errorPassword).toEqual('La password deve essere compresa tra 6 e 12 caratteri');
            expect($scope.okPassword).toEqual('');
            expect(test).toEqual('Errore inserimento password');
        }

        psw="testPassword";
        pswRe=undefined;
        $scope.password=psw;
        $scope.reinsertedPassword=pswRe;
        try{
            $scope.checkPassword();
        }
        catch(test){
            expect($scope.errorPassword).toEqual('');
            expect($scope.okPassword).toEqual('');
            expect(test).toEqual('Errore inserimento password');
        }

        psw="testPassword";
        pswRe="testpassword";
        $scope.password=psw;
        $scope.reinsertedPassword=pswRe;
        try{
            $scope.checkPassword();
        }
        catch(test){
            expect($scope.errorPassword).toEqual('');
            expect($scope.okPassword).toEqual('');
            expect(test).toEqual('Errore inserimento password');
        }

        psw="testPassword";
        pswRe="testPassword";
        $scope.password=psw;
        $scope.reinsertedPassword=pswRe;
        try{
            $scope.checkPassword();
        }
        catch(test){
            expect($scope.errorPassword).toEqual('');
            expect($scope.okPassword).toEqual('OK password coincidenti');
        }
    });

    it ("should check if the email is write in a correct form",function(){

        var email="";
        $scope.email=email;
        $scope.errorEmail="";
        $scope.okEmail="";
        try{
            $scope.checkEmail();
        }
        catch(test){
            expect(test).toEqual("Email obbligatoria");
        }

        email="email.it";
        $scope.email=email;
        $scope.errorEmail="";
        $scope.okEmail="";
        try{
            $scope.checkEmail();
        }
        catch(test){
            expect($scope.errorEmail).toEqual("La email non rispetta il pattern");
            expect($scope.okEmail).toEqual("");
            expect(test).toEqual("Errore inserimento email");
        }

        email="email@test.it";
        $scope.email=email;
        $scope.errorEmail="";
        $scope.okEmail="";
        try{
            $scope.checkEmail();
        }
        catch(test){
            expect($scope.errorEmail).toEqual("La email non rispetta il pattern");
        }

    });

    it ("should check if the name is write in a correct form",function(){

        var name="";
        $scope.name=name;
        $scope.errorName="";
        $scope.okName="";
        try{
            $scope.checkName();
        }
        catch(test){
            expect($scope.errorName).toEqual("Nome non valido");
            expect($scope.okName).toEqual("");
            expect(test).toEqual("Errore inserimento nome");
        }

        name="nameTest";
        $scope.name=name;
        $scope.errorName="";
        $scope.okName="";
        $scope.checkName();
        expect($scope.errorName).toEqual("");

    });



    it("should do a new admin registration",function(){
        $scope.username = "testUsrname";
        $scope.password = "testPassword";
        $scope.email = "email@test.com";
        $scope.reinsertedPassword = "testPassword";
        $scope.emailOk=true;
        $scope.passwordOk=true;
        $scope.usernameOk=true;
        $scope.passwordReinsertedOk=true;
        $scope.doRegistrationNewAdmin();
        expect(PreAuthenticationRequestManagerMock.doRegistration).toHaveBeenCalled();

        $scope.$broadcast('REQUEST_REGISTRATION_IS_END','Registration Is End');
        expect($scope.messageInfoResult).toEqual("Registration Is End");
        expect($scope.showAlertS).toEqual(true);
        expect($scope.showAlertD).toEqual(false);
    });

    it("should doesn't do a new admin registration",function(){
        $scope.email="emailNonValida.it";
        $scope.password="passwordNonValida";
        $scope.reinsertedPassword="passwordNonValida";
        $scope.emailOk=false;
        $scope.doRegistrationNewAdmin();
        expect($scope.messageError).toEqual("Sono presenti degli errori nei dati inserti. Ricontrolla i dati e prova a registrarti nuovamente.");
        expect($scope.showAlertS).toEqual(false);
        expect($scope.showAlertD).toEqual(true);
    });

    it("should close the webSocket caused by a connection problem", function(){
        $scope.$broadcast("CONNECTION_PROBLEM");
    });

});