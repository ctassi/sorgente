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
 * Name : CtrlRegistrationSpec.js
 * Target : CtrlRegistration
 * Location : /test/unit/controller/user
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-23     Pavanello Mirko
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe("CONTROLLER - CtrlRegistration",function(){
    var PreAuthenticationRequestManagerMock;
    var LocationManagerMock;
    var RegularExpressionsMock;
    var DistrictListMock;

    beforeEach(function(){
        PreAuthenticationRequestManagerMock = jasmine.createSpyObj('PreAuthenticationRequestManager',['doRegistration']);
        LocationManagerMock = jasmine.createSpyObj('LocationManager',['handleRegistrationLocation']);
        RegularExpressionsMock = jasmine.createSpyObj('RegularExpressions',['test']);
        DistrictListMock = jasmine.createSpyObj('DistrictList',['']);
        DistrictListMock.DistrictList = [] ;

        RegularExpressionsMock.regularExpressionUsername= new RegExp('^[a-zA-Z][a-zA-Z0-9]+([.][a-zA-Z0-9]+|[a-zA-Z0-9]*)[a-zA-Z0-9]$');
        RegularExpressionsMock.regularExpressionEmail = new RegExp('^[a-zA-Z0-9][a-zA-Z0-9]+([.][a-zA-Z0-9]+|[a-zA-Z0-9]*)[@][a-zA-Z0-9]+[.][a-z]{2,3}');
        RegularExpressionsMock.regularExpressionPassword = new RegExp('');
        RegularExpressionsMock.regularExpressionName = new RegExp('');
        RegularExpressionsMock.regularExpressionSurname = new RegExp('');

        module('CtrlRegistration');

        inject (function($rootScope, $controller){
            $scope = $rootScope.$new();
            $scope.scrollOnTop = function () {};

            ctrl = $controller('CtrlRegistration', {
                $scope: $scope,
                LocationManager : LocationManagerMock,
                RegularExpressions : RegularExpressionsMock,
                PreAuthenticationRequestManager : PreAuthenticationRequestManagerMock,
                DistrictList : DistrictListMock
            });
        });
    });

    it("should handleRegistrationLocation have been called",function(){
        expect(LocationManagerMock.handleRegistrationLocation).toHaveBeenCalled();

        $scope.$broadcast("REQUEST_REGISTRATION_IS_END",'Data for scope message');
        expect($scope.message).toEqual('Data for scope message');
    });

    it("should do registration of an user",function(){

        $scope.emailOk=true;
        $scope.passwordOk=true;
        $scope.usernameOk=true;
        $scope.passwordReinsertedOk=true;
        $scope.password = "testPassword";
        $scope.reinsertedPassword = "testPassword";
        $scope.email = "email@test.com";
        $scope.doRegistration();
        expect(PreAuthenticationRequestManagerMock.doRegistration).toHaveBeenCalled();
    });

    it("should doesn't do registration of an user",function(){
        $scope.email="emailNonValida.it";
        $scope.password="passwordNonValida";
        $scope.reinsertedPassword="passwordNonValida";
        $scope.emailOk=false;
        $scope.doRegistration();
        expect($scope.message).toEqual("Sono presenti degli errori nei dati inserti. Ricontrolla i dati e prova a registrarti nuovamente.");
        expect($scope.showAlert).toEqual("true");
    });

    it ("should check if the username is write in a correct form",function(){

        var usr="";
        $scope.username=usr;
        $scope.errorUsername="";
        $scope.okUsername="";
        try{$scope.checkUsername();}
        catch(test){
            expect(test).toEqual("errore");
        }

        usr="Usr";
        $scope.username=usr;
        try{$scope.checkUsername();}
        catch(test){
            expect($scope.errorUsername).toEqual('Lo username deve essere compreso tra 4 e 10 caratteri');
            expect($scope.okUsername).toEqual('');
            expect(test).toEqual('Errore inserimento username');
        }

        usr="Usrname.";
        $scope.username=usr;
        try{
            $scope.checkUsername();
            expect(RegularExpressionsMock.regularExpressionUsername.test).toHaveBeenCalled();
        }
        catch(test){
            expect($scope.errorUsername).toEqual("Lo Username non può contenere caratteri speciali o spazi");
            expect($scope.okUsername).toEqual('');
        }

        usr="Username";
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
            expect(test).toEqual('Password obbligatoria');
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
            expect($scope.errorReinsertedPassword).toEqual('Per sicurezza reinserisci la password');
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
            expect($scope.errorReinsertedPassword).toEqual('La password non coincide');
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
            expect($scope.errorEmail).toEqual("");
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

    it ("should check if the surname is write in a correct form",function(){

        var surname="";
        $scope.surname=surname;
        $scope.errorSurname="";
        $scope.okSurname="";
        try{
            $scope.checkSurname();
        }
        catch(test){
            expect($scope.errorName).toEqual("Cognome non valido");
            expect($scope.okName).toEqual("");
            expect(test).toEqual("Errore inserimento cognome");
        }

        surnamename="surnameTest";
        $scope.name=surnamename;
        $scope.errorName="";
        $scope.okName="";
        $scope.checkSurname();
        expect($scope.errorSurname).toEqual("");
    });
});