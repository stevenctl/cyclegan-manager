package com.slandow.cycleganmanager.controller

import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.repository.AppUserRepository
import com.slandow.cycleganmanager.util.ResponseUtil
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")

class UserController {

    @Autowired
    private AppUserRepository userRepository

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder

    @PostMapping
    ResponseEntity createUser(@RequestBody AppUser user) {
        if (userRepository.findByUsername(user.username)) {
            return ResponseUtil.badRequest("A user with username '${user.username}' already exists.")
        }

        user.password = bCryptPasswordEncoder.encode(user.getPassword())
        userRepository.save(user)

        return ResponseUtil.ok("Created user'${user.username}'")
    }

    @PutMapping("{username}")
    ResponseEntity updateUser(@PathVariable String username, @RequestBody AppUser requestUser) {
        def user = userRepository.findByUsername(username)

        if (!user) {
            return ResponseUtil.notFound("Could not find user with username '${username}'")
        }

        if (username != requestUser.username) {
            return ResponseUtil.badRequest("Username cannot be changed.")
        }

        user.password = bCryptPasswordEncoder.encode(requestUser.getPassword())

        userRepository.save(user)

        return ResponseUtil.ok("Successfully updated user.")
    }

    @GetMapping
    List<String> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int itemsPerPage
    ){
        return userRepository.findAll(new PageRequest(page, itemsPerPage)).collect({user -> user.username})
    }



}
