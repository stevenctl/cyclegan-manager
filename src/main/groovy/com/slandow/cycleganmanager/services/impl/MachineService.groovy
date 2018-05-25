package com.slandow.cycleganmanager.services.impl

import com.slandow.cycleganmanager.model.Machine
import com.slandow.cycleganmanager.repository.MachineRepository
import com.slandow.cycleganmanager.services.IMachineService
import com.slandow.cycleganmanager.util.ResponseUtil
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@TypeChecked
class MachineService implements IMachineService {


    @Autowired
    private MachineRepository machineRepository

    @Override
    Machine refreshMachineConnectivity(Machine machine, boolean save = false){
        // TODO test that we can ssh with our ansible key
        def pingProcess = "ping ${machine.host} -c 1".execute()
        pingProcess.waitForOrKill(500)
        machine.reachable = pingProcess.exitValue() == 0

        if(save) {
            machine = machineRepository.save(machine)
        }

        return machine
    }
}
