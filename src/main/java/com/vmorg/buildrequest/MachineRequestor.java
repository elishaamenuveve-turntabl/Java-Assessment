package com.vmorg.buildrequest;

import com.vmorg.auth.AuthorisingService;
import com.vmorg.build.SystemBuildService;
import com.vmorg.exception.MachineNotCreatedException;
import com.vmorg.exception.UserNotEntitledException;
import com.vmorg.machine.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MachineRequestor implements VirtualMachineRequestor{
    private static Map<String, Map<String, Integer>> totalBuildsByUserForToday;
    private Map<String, Integer> totalBuildsToday;
    private static int totalFailedBuildsForToday;


    private static List<Machine>  collectionOfMachinesBuiltToday = new ArrayList<>();
    private static List<Machine>  collectionOfFailedBuildsToday = new ArrayList<>();

//    private static List<VirtualMachineRequestor>  failedRequests = new ArrayList<>();
    private Machine machine;
    private final String username;
    private AuthorisingService authorisingService;

    private SystemBuildService systemBuildService;

    public MachineRequestor(String username, AuthorisingService authorisingService, SystemBuildService systemBuildService) {
        this.authorisingService = authorisingService;
        this.systemBuildService = systemBuildService;
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
           String hostname = systemBuildService.createNewMachine(machine);
           if (Objects.equals(hostname, "")) {
               MachineRequestor.totalFailedBuildsForToday ++;
               throw new MachineNotCreatedException();
           } else {
               totalBuildsToday.putIfAbsent(machine.getClass().getSimpleName(), 0);
               totalBuildsToday.put(machine.getClass().getSimpleName(), 1);
               totalBuildsByUserForToday.put(username, totalBuildsToday);
               collectionOfMachinesBuiltToday.add(machine);
               MachineRequestor.collectionOfMachinesBuiltToday.add(machine);
           }
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
        return totalFailedBuildsForToday;
    }
}
