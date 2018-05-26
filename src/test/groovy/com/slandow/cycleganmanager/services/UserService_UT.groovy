package com.slandow.cycleganmanager.services

import com.slandow.cycleganmanager.CycleganManagerApplicationTests
import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.repository.AppUserRepository
import com.slandow.cycleganmanager.services.impl.UserServiceImpl
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

import static junit.framework.TestCase.assertTrue
import static org.junit.Assert.fail
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.verify

@RunWith(SpringRunner)
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

}
