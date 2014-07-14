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
 * Name : StandardUserStepSpec.js
 * Target : StandardUserStep
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
 */

describe("MODEL - StandardUserStep", function(){
	
	beforeEach(function(){
		standardUserStep = new StandardUserStep();
	});
	
	it("should be able to setInsertedText attribute", function(){
		standardUserStep.setInsertedText("Test text");
		expect(standardUserStep.getInsertedText()).toEqual("Test text");
	});
	
	it("should be able to setInsertedLatitude attribute", function(){
		standardUserStep.setInsertedLatitude("");
		expect(standardUserStep.getInsertedLatitude()).toEqual("");
	});
	
	it("should be able to setInsertedLongitude attribute", function(){
		standardUserStep.setInsertedLongitude("");
		expect(standardUserStep.getInsertedLongitude()).toEqual("");
	});
	
	it("should be able to setInsertedPhoto attribute", function(){
		standardUserStep.setInsertedPhoto("Test p");
		expect(standardUserStep.getInsertedPhoto()).toEqual("Test p");
	});
});