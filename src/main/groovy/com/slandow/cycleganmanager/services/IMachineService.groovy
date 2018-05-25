package com.slandow.cycleganmanager.services

import com.slandow.cycleganmanager.model.Machine

interface IMachineService {

    Machine refreshMachineConnectivity(Machine machine, boolean save)

}