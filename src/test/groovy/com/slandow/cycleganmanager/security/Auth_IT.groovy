package com.slandow.cycleganmanager.security

import com.slandow.cycleganmanager.CycleganManagerApplication
import com.slandow.cycleganmanager.IntegrationTestConfig
import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.repository.AppUserRepository
import groovy.util.logging.Slf4j
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

@Slf4j
@RunWith(SpringRunner)
@ContextConfiguration(classes = [CycleganManagerApplication, IntegrationTestConfig])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Auth_IT {

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private AppUserRepository userRepository

    @Autowired
    private BCryptPasswordEncoder passwordEncoder


    private static final String ADMIN_USERNAME = "admin"
    private static final String ADMIN_PASSWORD = "Admin4Life!"

    private static final String USER_USERNAME = "normaluser"
    private static final String USER_PASSWORD = "Password123!"

    @Before
    void setUp() {
        userRepository.save(new AppUser(
                username: ADMIN_USERNAME,
                password: passwordEncoder.encode(ADMIN_PASSWORD),
                email: "admin@example.com",
                emailVerified: true,
                roles: ["ROLE_ADMIN"]
        ))

        userRepository.save(new AppUser(
                username: USER_USERNAME,
                password: passwordEncoder.encode(USER_PASSWORD),
                email: "user@example.com",
                emailVerified: true,
                roles: []
        ))
    }

    @Test
    void testUnauthorizedWithoutAuthHeader() {
        ResponseEntity usersResponse = restTemplate.getForEntity("/users", Object)
        log.info(usersResponse.toString())
    }

    @Test
    void testLogin() {
        ResponseEntity loginResponse = restTemplate.postForEntity("/login", [
                username: ADMIN_USERNAME,
                password: ADMIN_PASSWORD
        ], Object)

        assertEquals(HttpStatus.OK, loginResponse.statusCode)
        assertTrue(loginResponse.headers.containsKey(SecurityConstants.HEADER_STRING))
        assertTrue(loginResponse.headers.get(SecurityConstants.HEADER_STRING)[0]
                .startsWith(SecurityConstants.TOKEN_PREFIX))
    }

    @Test
    void testAuthenticatedRequest() {
        ResponseEntity loginResponse = restTemplate.postForEntity("/login", [
                username: ADMIN_USERNAME,
                password: ADMIN_PASSWORD
        ], Object)

        MultiValueMap headers = new LinkedMultiValueMap()
        headers.put(SecurityConstants.HEADER_STRING, loginResponse.headers.get(SecurityConstants.HEADER_STRING))

        ResponseEntity authenticatedResponse = restTemplate.exchange(
                "/users",
                HttpMethod.GET,
                new HttpEntity<Object>(headers),
                Object
        )

        assertEquals(HttpStatus.OK, authenticatedResponse.statusCode)
    }

    @Test
    void testAuthenticatedRequestWithoutCorrectRoles(){
        ResponseEntity loginResponse = restTemplate.postForEntity("/login", [
                username: USER_USERNAME,
                password: USER_PASSWORD
        ], Object)

        MultiValueMap headers = new LinkedMultiValueMap()
        headers.put(SecurityConstants.HEADER_STRING, loginResponse.headers.get(SecurityConstants.HEADER_STRING))

        ResponseEntity authenticatedResponse = restTemplate.exchange(
                "/users",
                HttpMethod.GET,
                new HttpEntity<Object>(headers),
                Object
        )

        assertEquals(HttpStatus.UNAUTHORIZED, authenticatedResponse.statusCode)
    }

}
