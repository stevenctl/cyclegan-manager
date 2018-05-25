package com.slandow.cycleganmanager.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

class Machine {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String id

    String host

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String owner

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    boolean reachable

}
