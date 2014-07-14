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
 * Name : GenericUserSpec.js
 * Target : GenericUser
 * Location : /test/unit/model/shared/superclasses
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-25     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe("MODEL - GenericUser", function() {
    var genericUser;

    beforeEach(function() {
        genericUser = new GenericUser();
    });

    it("should be able to set his id", function() {
        genericUser.setId(2);
        expect(genericUser.getId()).toEqual(2);
    });

    it("should be able to set his username", function() {
        genericUser.setUsername('username');
        expect(genericUser.getUsername()).toEqual('username');
    });

    it("should be able to set his email", function() {
        genericUser.setEmail('test@email.com');
        expect(genericUser.getEmail()).toEqual('test@email.com');
    });

    it("should be able to set his name", function() {
        genericUser.setName('TestName');
        expect(genericUser.getName()).toEqual('TestName');
    });

    it("should be able to set his surname", function() {
        genericUser.setSurname('TestSurname');
        expect(genericUser.getSurname()).toEqual('TestSurname');
    });

    it("should be able to set his city", function() {
        genericUser.setCity('TestCity');
        expect(genericUser.getCity()).toEqual('TestCity');
    });

    it("should be able to set his discrict", function() {
        genericUser.setDistrict('TestDistrict');
        expect(genericUser.getDistrict()).toEqual('TestDistrict');
    });
});
