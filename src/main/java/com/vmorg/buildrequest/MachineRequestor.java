package com.vmorg.buildrequest;

import com.vmorg.auth.AuthorisingService;
import com.vmorg.exception.MachineNotCreatedException;
import com.vmorg.exception.UserNotEntitledException;
import com.vmorg.machine.Machine;

import java.util.Map;

public class MachineRequestor implements VirtualMachineRequestor{
    private Machine machine;
    private final String username;
    private AuthorisingService authorisingService;
    public MachineRequestor(String username, AuthorisingService authorisingService) {
        this.authorisingService = authorisingService;
        this.username = username;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Machine getMachine() {
        return machine;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void createNewRequest(Machine machine) throws UserNotEntitledException, MachineNotCreatedException {
        if (authorisingService.isAuthorised(username)) {
            //
        } else {
            throw new UserNotEntitledException();
        }


    }

    @Override
    public Map<String, Map<String, Integer>> totalBuildsByUserForDay() {
        return null;
    }

    @Override
    public int totalFailedBuildsForDay() {
        return 0;
    }
}
