package com.slandow.cycleganmanager.security

import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.repository.AppUserRepository
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository

    @Override
    UserDetails loadUserByUsername(String username) throws NoSuchElementException{
        AppUser applicationUser = userRepository.findByUsername(username)
        if (applicationUser == null) {
            throw new NoSuchElementException("No user with username '${username}'.")
        }

        return new User(
                applicationUser.username,
                applicationUser.password,
                true, true, true, true,
                applicationUser.roles.collect({roleName -> new SimpleGrantedAuthority(roleName)})
        )
    }



}