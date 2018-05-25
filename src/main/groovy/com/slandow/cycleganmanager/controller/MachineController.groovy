package com.slandow.cycleganmanager.controller

import com.slandow.cycleganmanager.model.Machine
import com.slandow.cycleganmanager.repository.MachineRepository
import com.slandow.cycleganmanager.services.impl.MachineService
import com.slandow.cycleganmanager.util.ResponseUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/machines")
class MachineController {

    @Autowired
    private MachineRepository machineRepository

    @Autowired
    private MachineService machineService

    @GetMapping
    ResponseEntity getMachines() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        return ResponseEntity.ok(machineRepository.findByOwner(auth.name))
    }

    @GetMapping("{machineId}")
    ResponseEntity getMachine(
            @PathVariable String machineId,
            @RequestParam(defaultValue="false") boolean refresh){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        Optional<Machine> queryResult = machineRepository.findByIdAndOwner(machineId, auth.name)

        if(!queryResult.isPresent()){
            return ResponseUtil.notFound("No machine found for ${}")
        }

        Machine machine = queryResult.get()

        if (refresh){
            machine = machineService.refreshMachineConnectivity(machine, true)
        }

        return ResponseEntity.ok(machine)
    }

    @PostMapping
    ResponseEntity addMachine(@RequestBody Machine machine) {\
        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
        // TODO disallow duplicate hosts and special hosts (localhost, 0.0.0.0, etc.)
        machine.owner = auth.name
        machine = machineRepository.save(machine)
        machine = machineService.refreshMachineConnectivity(machine, true)
        return ResponseUtil.ok("Successfully registered machine ${machine.id}.")
    }


}
