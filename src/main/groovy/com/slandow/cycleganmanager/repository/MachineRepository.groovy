package com.slandow.cycleganmanager.repository

import com.slandow.cycleganmanager.model.AppUser
import com.slandow.cycleganmanager.model.Machine
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MachineRepository extends MongoRepository<Machine, String> {

    List<Machine> findByOwner(String owner)

    Optional<Machine> findByIdAndOwner(String id, String owner)

}
