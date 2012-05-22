/* Copyright 2009, 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */
package com.predic8.membrane.core.interceptor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.*;

import com.predic8.membrane.core.Router;

public class InternalInvocationTest {

	private Router router;

	@Before
	public void setUp() throws Exception {
		router = Router.init("src/test/resources/userFeature/monitor-beans.xml");
		router.getConfigurationManager().loadConfiguration(
				"src/test/resources/internal-invocation/proxies.xml");
		MockInterceptor.reqLabels.clear();
		MockInterceptor.respLabels.clear();		
	}

	@Test
	public void testFullChain() throws Exception {
		callService(2001);
		assertRequest(new String[] {"Mock1",
			"Mock2", "Mock3", "Mock4", "Mock5", "Mock6"});
		
		assertResponse(new String[] { "Mock6",
				"Mock5", "Mock4", "Mock3", "Mock2", "Mock1" });
	}

	@Test
	public void testReturnedChain() throws Exception {
		callService(2002);
		assertRequest(new String[] {"Mock1",
			"Mock2", "Mock3"});
		
		assertResponse(new String[] { "Mock3",
				"Mock2", "Mock1" });
	}

	@After
	public void tearDown() throws Exception {
		router.getTransport().closeAll();
	}

	private void assertResponse(String[] responseLabels) {
		assertEquals(Arrays.asList(responseLabels), MockInterceptor.respLabels);		
	}

	private void assertRequest(String[] requestLabels) {
		assertEquals(Arrays.asList(requestLabels), MockInterceptor.reqLabels);		
	}
			
	private void callService(int port) throws HttpException, IOException {
		new HttpClient().executeMethod(new GetMethod("http://localhost:"+port));
	}

}
