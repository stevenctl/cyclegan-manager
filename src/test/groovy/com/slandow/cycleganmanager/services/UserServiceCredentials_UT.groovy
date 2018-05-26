package com.slandow.cycleganmanager.services

import com.slandow.cycleganmanager.CycleganManagerApplicationTests
import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.repository.AppUserRepository
import com.slandow.cycleganmanager.services.impl.UserServiceImpl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ContextConfiguration

import static junit.framework.TestCase.assertTrue
import static org.junit.Assert.fail
import static org.mockito.Mockito.verify
import static org.mockito.MockitoAnnotations.initMocks

@RunWith(Parameterized)
@ContextConfiguration(classes=[CycleganManagerApplicationTests])
class UserServiceCredentials_UT {

    private static final String SIGNUP_MESSAGE = "{verifyurl}"
    private static final String SIGNUP_SUBJECT = "Welcome"
    private static final String SYSTEM_EMAIL = "test@system.com"

    @Mock
    JavaMailSender mailSender

    @Mock
    AppUserRepository userRepository

    @InjectMocks
    UserService userService = new UserServiceImpl(
            bCryptPasswordEncoder: new BCryptPasswordEncoder(),
            signupMessage: SIGNUP_MESSAGE,
            signupSubject: SIGNUP_SUBJECT,
            emailAddress: SYSTEM_EMAIL
    )

    @Before
    void setUp(){
        initMocks(this)
    }

    @Parameterized.Parameters
    static Iterable<Object[]> data(){
        def randLetter = {(Math.random() * 26 + 65) as char}
        return [
            ["shrt", "Pw1!"] as Object[],
            [(1..26).collect({randLetter()}).join(), "Pw1!" + (1..50).collect({randLetter()}).join()] as Object[],
            ["!username", "Password1"] as Object[],
            ["", "Password!"] as Object[],
            ["", "password1!"] as Object[],
            ["", "PASSWORD1!"] as Object[],
            ["", "123!@#15124!"] as Object[],
        ]
    }

    private final String username
    private final String password

    UserServiceCredentials_UT(String username, String password){
        this.username = username
        this.password = password
    }

    @Test
    void testCreateUserFailsWhenUsernameDoesNotMeetRequirements() {
        try {
            userService.createUser(new AppUser(
                    username: username
            ))
            fail("Expected An IllegalArgumentException to be thrown")
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.message.startsWith("Username must"))
        }
    }

    @Test
    void testCreateUserFailsWhenPasswordDoesNotMeetRequirements() {
        try {
            userService.createUser(new AppUser(
                    username: "validuser",
                    password: password
            ))
            fail("Expected An IllegalArgumentException to be thrown")
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.message.startsWith("Password must"))
        }
    }


}
