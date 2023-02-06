package com.example.bravetracing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureObservability
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class BraveTracingApplicationTests {

	static protected final Log log = LogFactory.getLog(BraveTracingApplicationTests.class);

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCallable() throws Exception {
		var result = mockMvc.perform(get("/callable"))
				.andExpect(request().asyncStarted())
				.andReturn();

		mockMvc.perform(asyncDispatch(result))
				.andExpect(status().isOk());
	}

	@Test
	void testNormal() throws Exception {
		mockMvc.perform(get("/normal")).andExpect(status().isOk());
	}


}
