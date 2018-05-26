package com.slandow.cycleganmanager.services

import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.repository.AppUserRepository
import com.slandow.cycleganmanager.services.impl.UserServiceImpl
import groovy.transform.TypeChecked
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner

import static junit.framework.TestCase.assertEquals
import static org.mockito.Mockito.verify

@RunWith(SpringRunner.class)
class UserService_UT {

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

    @Test
    void testCreateUserSendsMail() {
        AppUser userRequest = new AppUser(
                username: "testuser",
                password: "Password123!",
                email: "user@example.com"
        )

        userService.createUser(userRequest)

        verify(mailSender).send(new SimpleMailMessage(
                to: userRequest.email,
                from: SYSTEM_EMAIL,
                subject: "Welcome",
                text: "/verify?user=${userRequest.username}&token=${userRequest.emailToken}"
        ))
    }

    @Test(expected = IllegalArgumentException)
    void testCreateUserFailsWhenUsernameTooShort(){
        userService.createUser(new AppUser(
                username: "abc"
        ))
    }

    @Test(expected = IllegalArgumentException)
    void testFailsWhenUsernameTooLong(){
        userService.createUser(new AppUser(
                username: "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
        ))
    }

    @Test(expected = IllegalArgumentException)
    void testFailsWhenUsernameHasSpecialChars(){
        userService.createUser(new AppUser(
                username: "abc!"
        ))
    }

}
