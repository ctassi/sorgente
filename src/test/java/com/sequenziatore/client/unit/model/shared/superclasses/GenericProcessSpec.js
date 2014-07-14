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
 * Name : GenericProcessSpec.js
 * Target : GenericProcess
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

describe("MODEL - GenericProcess",function(){
	
	var genericProcess;
	
	beforeEach(function(){
		genericProcess = new GenericProcess();
	});
	
	it("should be able to set his availability", function(){
		genericProcess.setIsAvailable(true);
		expect(genericProcess.getIsAvailable()).toEqual(true);
	});
	
	it("should be able to set his description", function(){
		genericProcess.setDescription('TestDescription');
		expect(genericProcess.getDescription()).toEqual('TestDescription');
	});
	
	it("should be able to set his publication date", function(){
		genericProcess.setPublicationDate("2000/10/10");
		expect(genericProcess.getPublicationDate()).toEqual("2000/10/10");
	});
	
	it("should be able to set his closing date", function(){
		genericProcess.setClosingDate("2000/10/10");
		expect(genericProcess.getClosingDate()).toEqual("2000/10/10");
	});
	
	it("should be able to set his name", function(){
		genericProcess.setName("ProcessTestName");
		expect(genericProcess.getName()).toEqual("ProcessTestName");
	});
	
	it("should be able to set his total steps number", function(){
		genericProcess.setTotalSteps("10");
		expect(genericProcess.getTotalSteps()).toEqual("10");
	});
	
	it("should be able to set his Admin owner", function(){
		genericProcess.setIdAdminOwner("10");
		expect(genericProcess.getIdAdminOwner()).toEqual("10");
	});
});