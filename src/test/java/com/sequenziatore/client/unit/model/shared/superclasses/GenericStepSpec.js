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
 * Name : GenericStepSpec.js
 * Target : GenericStep
 * Location : /test/unit/model/shared/superclasses
 *
 * History :
 *
 * Version         Date           Programmer
 * ======================================================
 * 0.0.1           2014-03-27     Sartoretto Massimiliano
 * ------------------------------------------------------
 * Codifica test
 * ======================================================
 */

describe("MODEL - GenericStep", function() {
    var genericStep;

    beforeEach(function() {
        genericStep = new GenericStep();
    });

    it("should be able to set the AdminVerify", function() {
        genericStep.setAdminVerify(true);
        expect(genericStep.getAdminVerify()).toEqual(true);
    });

    it("should be able to set his description", function() {
        genericStep.setDescription("TestDescription");
        expect(genericStep.getDescription()).toEqual("TestDescription");
    });

    it("should be able to set the isGeolocation", function() {
        genericStep.setIsGeolocation(true);
        expect(genericStep.getIsGeolocation()).toEqual(true);
    });

    it("should be able to set the isPhoto", function() {
        genericStep.setIsPhoto(true);
        expect(genericStep.getIsPhoto()).toEqual(true);
    });

    it("should be able to set the isText", function() {
        genericStep.setIsText(true);
        expect(genericStep.getIsText()).toEqual(true);
    });

    it("should be able to set his level", function() {
        genericStep.setLevel('10');
        expect(genericStep.getLevel()).toEqual('10');
    });

    it("should be able to set his parallelism", function() {
        genericStep.setIsPhoto('AND');
        expect(genericStep.getIsPhoto()).toEqual('AND');
    });
});
