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
 * Name : LocationManagerSpec.js
 * Target : LocationManager
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
 * 0.0.2           2014-03-27     Pavanello Mirko
 * ------------------------------------------------------
 * Integrazione test
 * ======================================================
 */

describe('MODEL - LocationManager', function () {

    var factory;
    var cookie;
    var location;

    beforeEach(function(){
        module('LocationManager')

        inject(function($injector){
            factory = $injector.get('LocationManager');
            cookie = $injector.get('$cookieStore');
            location = $injector.get('$location');
        });
    });

    it('should check if the user is logged', function(){
        cookie.put('logged','true');
        expect(factory.isLogged()).toEqual('true');
    });

    it('should check if the user is an Admin', function() {
        cookie.put('isAdmin','true');
        expect(factory.isAdmin()).toEqual('true');
    });

    it('should get the user cookie', function() {
        cookie.put('user','content');
        expect(factory.getUserCookie()).toEqual('content');
    });

    it('should get the user Id from his cookie', function() {
        cookie.put('idUser','42');
        expect(factory.getIdUserCookie()).toEqual('42');
    });

    // LOGIN LOCATION HANDLER

    it('should return to the welcome page after an admin login', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleLoginLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the welcome page after a standard user login', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleLoginLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleLoginLocation();
        expect(location.path()).toBe('/login');
    });

    // REGISTRATION LOCATION HANDLER

    it('should return to the welcome page after an admin registration', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleRegistrationLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the welcome page after a standard user registration', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleRegistrationLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleRegistrationLocation();
        expect(location.path()).toBe('/registration');
    });

    // WELCOME LOCATION HANDLER

    it('should return to the welcome page if the user is logged', function() {
        cookie.put('logged','true');
        factory.handleWelcomeLocation('TestUsername');
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleWelcomeLocation('TestUsername');
        expect(location.path()).toBe('/login');
    });

    // LOGOUT LOCATION HANDLER

    it('should destroy cookie and return to the login page if the user is logged', function() {
        cookie.put('logged','true');
        factory.handleLogoutLocation();
        expect(cookie.get('logged')).toEqual('false');
        expect(cookie.get('user')).toEqual('false');
        expect(cookie.get('isAdmin')).toEqual('false');
        expect(location.path()).toBe('/login');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleLogoutLocation();
        expect(location.path()).toBe('/login');
    });

    // PROFILE LOCATION HANDLER

    it('should return to the profile page if the user is logged', function() {
        cookie.put('logged','true');
        factory.handleProfileLocation();
        expect(location.path()).toBe('/profile');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleProfileLocation();
        expect(location.path()).toBe('/login');
    });

    // CREATE PROCESS LOCATION HANDLER

    it('should return to the createprocess page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleCreateProcessLocation();
        expect(location.path()).toBe('/createprocess');
    });

    it('should return to the welcome page if the user is logged and a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleCreateProcessLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleCreateProcessLocation();
        expect(location.path()).toBe('/login');
    });

    // ADMIN REGISTRATION LOCATION HANDLER

    it('should return to the registrationnewadmin page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleRegistrationNewAdminLocation();
        expect(location.path()).toBe('/registrationnewadmin');
    });

    it('should return to the welcome page if the user is logged and a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleRegistrationNewAdminLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleRegistrationNewAdminLocation();
        expect(location.path()).toBe('/login');
    });

    // ADMIN PROCESS LOCATION HANDLER

    it('should return to the adminprocess page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleAdminProcessLocation();
        expect(location.path()).toBe('/adminprocess');
    });

    it('should return to the welcome page if the user is logged and a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleAdminProcessLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleAdminProcessLocation();
        expect(location.path()).toBe('/login');
    });

    // STEP VALIDATOR LOCATION HANDLER

    it('should return to the stepvalidator page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleStepValidatorLocation();
        expect(location.path()).toBe('/stepvalidator');
    });

    it('should return to the welcome page if the user is logged and a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleStepValidatorLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleStepValidatorLocation();
        expect(location.path()).toBe('/login');
    });

    // VIEW PROCESS LOCATION HANDLER

    it('should return to the viewallprocess page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleViewAllProcessLocation();
        expect(location.path()).toBe('/viewallprocess');
    });

    it('should return to the welcome page if the user is logged and a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleViewAllProcessLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleViewAllProcessLocation();
        expect(location.path()).toBe('/login');
    });

    // ACTIVE PROCESS LOCATION MANAGER

    it('should return to the activeprocess page if the user is logged and a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleActiveProcessLocation();
        expect(location.path()).toBe('/activeprocess');
    });

    it('should return to the welcome page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleActiveProcessLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleActiveProcessLocation();
        expect(location.path()).toBe('/login');
    });

    // AVAILABLE PROCESS LOCATION MANAGER

    it('should return to the availableprocess page if the user is logged a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleAvailableProcessLocation();
        expect(location.path()).toBe('/availableprocess');
    });

    it('should return to the welcome page if the user is logged and an admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleAvailableProcessLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleAvailableProcessLocation();
        expect(location.path()).toBe('/login');
    });

    // LIST USER PROCESS LOCATION MANAGER

    it('should return to the listuserprocess page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleListUserProcessLocation();
        expect(location.path()).toBe('/listuserprocess');
    });

    it('should return to the welcome page if the user is logged and a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleListUserProcessLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleListUserProcessLocation();
        expect(location.path()).toBe('/login');
    });

    // MODIFY PROCESS LOCATION MANAGER

    it('should return to the modifyprocess page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleModifyProcessLocation();
        expect(location.path()).toBe('/modifyprocess');
    });

    it('should return to the welcome page if the user is logged and a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleModifyProcessLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleModifyProcessLocation();
        expect(location.path()).toBe('/login');
    });

    // MODIFY ACCOUNT LOCATION MANAGER

    it('should return to the modifyaccount page if the user is logged and admin', function() {
        cookie.put('logged','true');
        factory.handleModifyAccountLocation();
        expect(location.path()).toBe('/modifyaccount');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleModifyAccountLocation();
        expect(location.path()).toBe('/login');
    });

    // CURRENT STEP LOCATION MANAGER

    it('should return to the currentstep page if the user is logged and a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleCurrentStepLocation();
        expect(location.path()).toBe('/currentstep');
    });

    it('should return to the welcome page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleCurrentStepLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the login page if the user isnt logged', function() {
        cookie.put('logged','false');
        factory.handleCurrentStepLocation();
        expect(location.path()).toBe('/login');
    });

    // RECOVERY PASSWORD LOCATION MANAGER

    it('should return to the recovery password page if the user is logged a standard user', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','false');
        factory.handleRecoveryPasswordLocation();
        expect(location.path()).toBe('/welcome');
    });

    it('should return to the welcome page if the user is logged and admin', function() {
        cookie.put('logged','true');
        cookie.put('isAdmin','true');
        factory.handleRecoveryPasswordLocation();
        expect(location.path()).toBe('/welcome');
    });
});
