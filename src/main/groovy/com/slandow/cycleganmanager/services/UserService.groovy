package com.slandow.cycleganmanager.services

import com.slandow.cycleganmanager.model.AppUser

interface UserService {

    void createUser(AppUser user)

    void updateUser(String username, AppUser user)

    void deleteUser(String username)

    AppUser getUser(String username)

    List<AppUser> getUsers()

    List<AppUser> getUsers(int page, int length)
}
