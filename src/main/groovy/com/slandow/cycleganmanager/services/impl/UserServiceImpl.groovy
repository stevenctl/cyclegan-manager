package com.slandow.cycleganmanager.services.impl

import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.repository.AppUserRepository
import com.slandow.cycleganmanager.services.UserService
import com.slandow.cycleganmanager.util.PasswordUtil
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Slf4j
class UserServiceImpl implements UserService {

    @Autowired
    private AppUserRepository userRepository

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder

    @Autowired
    private JavaMailSender mailSender

    @Value('${signup-message}')
    private String signupMessage

    @Value('${signup-subject}')
    private String signupSubject

    @Value('${email-address}')
    private String emailAddress

    @Override
    @Transactional
    void createUser(AppUser user) {
        // Validate username isn't taken
        if (userRepository.findByUsername(user.username)) {
            throw new IllegalArgumentException("A user with username '${user.username}' already exists.")
        }

        validateAndUpdateUser(user)

        // Send verification email
        user.emailToken = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase()
        user.emailVerified = false

        mailSender.send(new SimpleMailMessage(
                to: user.email,
                from: emailAddress,
                subject: signupSubject,
                text: signupMessage.replace("{verifyurl}", "/verify?user=${user.username}&token=${user.emailToken}")
        ))

        // Persist User
        userRepository.save(user)

        log.info("Created user ${user.username}")
    }

    @Override
    void updateUser(String username, AppUser user) {
        findExistingUser(username)

        if (username != user.username) {
            throw new IllegalArgumentException("Username cannot be changed.")
        }

        validateAndUpdateUser(user)

        userRepository.save(user)

        log.info("Updated user ${user.username}")

    }

    @Override
    void deleteUser(String username) {
        AppUser user = findExistingUser(username)
        // TODO cleanup resources the user owns, or queue job to do so
        userRepository.delete(user)

        log.info("Deleted user ${user.username}")
    }

    @Override
    AppUser getUser(String username) {
        return findExistingUser()
    }

    @Override
    List<AppUser> getUsers() {
        return users.findAll()
    }

    @Override
    List<AppUser> getUsers(int page, int length) {
        return userRepository.findAll(new PageRequest(page, length)).toList()
    }

    private void validateAndUpdateUser(AppUser appUser) {
        if(!PasswordUtil.isUsernameValid(appUser.username)) {
            throw new IllegalArgumentException("Username must between ${PasswordUtil.MIN_USERNAME}" +
                    " and ${PasswordUtil.MAX_USERNAME} characters and " +
                    " cannot contain spaces or special characters. ")
        }

        if (!PasswordUtil.isPasswordValid(appUser.password)) {
            throw new IllegalArgumentException("Password must be between ${PasswordUtil.MIN_PASSWORD}" +
                    " and ${PasswordUtil.MAX_PASSWORD} characters," +
                    " contain both uppercase and lowercase letters," +
                    " a number, and a special character ().")
        }

        appUser.password = bCryptPasswordEncoder.encode(appUser.getPassword())
    }

    private AppUser findExistingUser(String username) {

        def savedUser = userRepository.findByUsername(username)

        if (!savedUser) {
            throw new NoSuchElementException("Could not find user with username '${username}'")
        }

        return savedUser
    }


}
