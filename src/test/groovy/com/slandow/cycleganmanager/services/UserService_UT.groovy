package com.slandow.cycleganmanager.services

import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.repository.AppUserRepository
import com.slandow.cycleganmanager.services.impl.UserServiceImpl
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner

import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(SpringRunner)
class UserService_UT {

    private static final String SIGNUP_MESSAGE = "{verifyurl}"
    private static final String SIGNUP_SUBJECT = "Welcome"
    private static final String SYSTEM_EMAIL = "test@system.com"

    @Mock
    JavaMailSender mailSender

    @Mock
    AppUserRepository userRepository

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder()

    @InjectMocks
    UserService userService = new UserServiceImpl(
            bCryptPasswordEncoder: passwordEncoder,
            signupMessage: SIGNUP_MESSAGE,
            signupSubject: SIGNUP_SUBJECT,
            emailAddress: SYSTEM_EMAIL
    )

    @Test
    void testCreateUserSendsMail() {
        AppUser user = new AppUser(username: "username", password: "Password1!", email: "user@example.com")

        userService.createUser(userRequest)

        verify(mailSender).send(new SimpleMailMessage(
                to: userRequest.email,
                from: SYSTEM_EMAIL,
                subject: "Welcome",
                text: "/verify?user=${userRequest.username}&token=${userRequest.emailToken}"
        ))
    }

    @Test
    void testCreateUserFailsWhenUsernameExists() {
        final username = "username"
        when(userRepository.findByUsername(username)).thenReturn(new AppUser())
        try {
            userService.createUser(new AppUser(username: username))
            fail("Expected An IllegalArgumentException to be thrown")
        } catch (IllegalArgumentException e) {
            assertTrue(e.message.contains("user with"))
            assertTrue(e.message.contains("already exists"))
        }
    }

    @Test
    void testPasswordIsEncoded() {
        AppUser user = new AppUser(username: "username", password: "Password1!", email: "user@example.com")
        userService.createUser(user)
        assertTrue(passwordEncoder.matches("Password1!", user.password))
    }

    @Test
    void testCreateUserSavesUser() {
        AppUser user = new AppUser(username: "username", password: "Password1!", email: "user@example.com")
        verify(userRepository).save(user)
    }


}
