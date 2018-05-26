package com.slandow.cycleganmanager.services

import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.repository.AppUserRepository
import com.slandow.cycleganmanager.services.impl.UserServiceImpl
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit4.SpringRunner

import static org.junit.Assert.*
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
        // Given
        final requestUser = new AppUser(username: "username", password: "Password1!", email: "user@example.com")

        // When
        userService.createUser(requestUser)

        // Then
        verify(mailSender).send(new SimpleMailMessage(
                to: requestUser.email,
                from: SYSTEM_EMAIL,
                subject: "Welcome",
                text: "/verify?user=${requestUser.username}&token=${requestUser.emailToken}"
        ))
    }

    @Test
    void testCreateUserFailsWhenUsernameExists() {

        // Given
        final username = "username"
        when(userRepository.findByUsername(username)).thenReturn(new AppUser())

        try {
            // When
            userService.createUser(new AppUser(username: username))
            fail("Expected An IllegalArgumentException to be thrown")
        } catch (IllegalArgumentException e) {
            // Then
            assertTrue(e.message.contains("user with"))
            assertTrue(e.message.contains("already exists"))
        }
    }

    @Test
    void testPasswordIsEncoded() {
        final user = new AppUser(username: "username", password: "Password1!", email: "user@example.com")
        userService.createUser(user)
        assertTrue(passwordEncoder.matches("Password1!", user.password))
    }

    @Test
    void testCreateUserSavesUser() {
        // Given
        final user = new AppUser(username: "username", password: "Password1!", email: "user@example.com")

        // When
        userService.createUser(user)

        // Then
        verify(userRepository).save(user)
    }

    @Test
    void testUpdateUserFailsWhenUserDoesNotExist() {
        // Given
        final newUser = new AppUser(username: "username", password: "Password123!", email: "user@example.com")

        // When
        try {
            userService.updateUser("username", newUser)
            fail("Expected a NoSucElementException to be thrown")
        } catch (NoSuchElementException e) {
            // Then
            assertTrue(e.message.startsWith("Could not find user"))
        }
    }

    @Test
    void testUpdateUserFailsWhenUsernameIsDifferent() {
        // Given
        final savedUser = new AppUser(username: "username", password: "Password1!", email: "user@example.com")
        when(userRepository.findByUsername("username")).thenReturn(savedUser)
        final newUser = new AppUser(username: "username1", password: "Password123!", email: "user@example.com")

        // When
        try {
            userService.updateUser("username", newUser)
            fail("Expected a IllegalArgumentException to be thrown")
        } catch (IllegalArgumentException e) {
            // Then
            assertTrue(e.message.endsWith("cannot be changed."))
        }
    }

    @Test
    void testUpdateUserSavesUserAndEncodesPassword() {
        // Given
        final savedUser = new AppUser(username: "username", password: "Password1!", email: "user@example.com")
        when(userRepository.findByUsername("username")).thenReturn(savedUser)
        final newUser = new AppUser(username: "username", password: "Password123!", email: "user@example.com")

        // When
        userService.updateUser("username", newUser)

        // Then
        assertTrue(passwordEncoder.matches("Password123!", newUser.password))
        verify(userRepository).save(newUser)

    }

    @Test
    void testDeleteUserFailsWhenUserDoesNotExist() {
        // Given there is no user with username "username"

        // When
        try {
            userService.deleteUser("username")
            fail("Expected a NoSucElementException to be thrown")
        } catch (NoSuchElementException e) {
            // Then
            assertTrue(e.message.startsWith("Could not find user"))
        }
    }

    @Test
    void testDeleteUserRemovesUser() {
        // Given
        final savedUser = new AppUser(username: "username", password: "Password1!", email: "user@example.com")
        when(userRepository.findByUsername("username")).thenReturn(savedUser)

        // When
        userService.deleteUser("username")

        // Then
        verify(userRepository).delete(savedUser)
    }

    @Test
    void testGetUserReturnsNullIfUserDoesNotExist() {
        // Given there is no user with username "username"

        // When
        final user = userService.getUser("username")

        // Then
        assertNull(user)
    }

    @Test
    void testGetUser() {
        // Given
        final savedUser = new AppUser(username: "username", password: "Password1!", email: "user@example.com")
        when(userRepository.findByUsername("username")).thenReturn(savedUser)

        // When
        final retrievedUser = userService.getUser("username")

        // Then
        assertEquals(savedUser, retrievedUser)
    }

    @Test
    void testGetAllUsers() {
        // Given
        final savedUsers = [
                new AppUser(username: "username", password: "Password1!", email: "user@example.com"),
                new AppUser(username: "username1", password: "Password2!", email: "user1@example.com"),
                new AppUser(username: "username2", password: "Password3!", email: "user2@example.com")
        ]
        when(userRepository.findAll()).thenReturn(savedUsers)

        // When
        final retrievedUsers = userService.getUsers()

        // Then
        assertEquals(savedUsers, retrievedUsers)
    }

    @Test
    void testGetAllUsersPaged() {
        // Given
        final savedUsers = [
                new AppUser(username: "username", password: "Password1!", email: "user@example.com"),
                new AppUser(username: "username1", password: "Password2!", email: "user1@example.com"),
                new AppUser(username: "username2", password: "Password3!", email: "user2@example.com")
        ]
        when(userRepository.findAll(new PageRequest(0, 1))).thenReturn(new PageImpl<AppUser>([savedUsers[0]]))

        // When
        final retrievedUsers = userService.getUsers(0, 1)

        // Then
        assertEquals([savedUsers[0]], retrievedUsers)
    }

}
