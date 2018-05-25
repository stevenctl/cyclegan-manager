package com.slandow.cycleganmanager.controller

import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.services.UserService
import com.slandow.cycleganmanager.util.AuthUtil
import com.slandow.cycleganmanager.util.ResponseUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@RestController
@RequestMapping("/users")
class  UserController {

    @Autowired
    UserService userService

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity createUser(@RequestBody @Valid AppUser user) {
        userService.createUser(user)
        return ResponseUtil.ok("Created user '${user.username}'")
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity registerUser(@RequestBody @Valid AppUser user) {
        user.roles = [] // Sanitize roles when doing normal registration
        userService.createUser(user)
        return ResponseUtil.ok("Registered user'${user.username}'")
    }

    @PutMapping("{username}")
    ResponseEntity updateUser(@PathVariable String username, @RequestBody @Valid AppUser requestUser) {
        userService.updateUser(username, requestUser)
        return ResponseUtil.ok("Successfully updated user.")
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    ResponseEntity getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int items) {
        return ResponseEntity.ok(userService.getUsers(page, items))
    }

    @GetMapping("{username}")
    ResponseEntity getUser(@PathVariable String username) {
        AuthUtil.validateAccessToUser(username)
        return ResponseEntity.ok(userService.getUser(username))
    }

}
