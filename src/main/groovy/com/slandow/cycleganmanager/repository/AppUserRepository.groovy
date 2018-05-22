package com.slandow.cycleganmanager.repository

import com.slandow.cycleganmanager.model.AppUser
import groovy.transform.TypeChecked
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository

interface AppUserRepository extends MongoRepository<AppUser, String> {

    AppUser findByUsername(String username)

}
