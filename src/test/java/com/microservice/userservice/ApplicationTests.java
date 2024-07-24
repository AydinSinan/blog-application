package com.microservice.userservice;

import com.microservice.userservice.model.User;
import com.microservice.userservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testTokenGenerationAndValidation() {
		User testUser = new User();
		testUser.setUsername("testuser");

		JwtService jwtService = new JwtService();

		String token = jwtService.generateToken(testUser);
		assertNotNull(token);

		String username = jwtService.extractUsername(token);
		assertEquals("testuser", username);

		boolean isValid = jwtService.validateToken(token, testUser);
		assertTrue(isValid);
	}


}
