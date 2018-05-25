package com.slandow.cycleganmanager.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.TypeChecked
import org.springframework.data.annotation.Id

import javax.management.relation.Role
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class AppUser {

    @Id
    @NotBlank
    String username

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password

    @NotBlank
    String email

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    boolean emailVerified

    @JsonIgnore
    String emailToken

    Set<String> roles = []

}
