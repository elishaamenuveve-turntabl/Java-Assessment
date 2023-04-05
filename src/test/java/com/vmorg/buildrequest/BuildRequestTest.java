package com.vmorg.buildrequest;


import com.vmorg.auth.AuthorisingService;
import com.vmorg.build.SystemBuildService;
import com.vmorg.exception.MachineNotCreatedException;
import com.vmorg.exception.UserNotEntitledException;
import com.vmorg.machine.Desktop;
import com.vmorg.machine.Machine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuildRequestTest {

    String username = "Noah";
    VirtualMachineRequestor requestor;
    @Mock
    Machine newMachine;

    @Mock
    AuthorisingService authorisingService;
    @Mock
    SystemBuildService systemBuildService;

    @BeforeEach
    void setUp() {
        authorisingService = mock(AuthorisingService.class);
        requestor = new MachineRequestor("Noah", authorisingService, systemBuildService);
        when(authorisingService.isAuthorised(username)).thenReturn(true);

    }

    @Test
    void requestorThrowsExceptionWhenUserNotEntitled() throws MachineNotCreatedException, UserNotEntitledException {
        when(authorisingService.isAuthorised(username)).thenReturn(false);
        Assertions.assertThrows(UserNotEntitledException.class, ()->requestor.createNewRequest(newMachine));
    }

    @Test
    void requestorDoesNotThrowsExceptionWhenUserIsEntitled() throws MachineNotCreatedException, UserNotEntitledException {
        when(authorisingService.isAuthorised(username)).thenReturn(true);
        when(systemBuildService.createNewMachine(newMachine)).thenReturn("host20230328005");
        Assertions.assertDoesNotThrow(()->requestor.createNewRequest(newMachine));

    }

    @Test
    void requestorCreatesRequestWhenUserIsEntitledTest() throws MachineNotCreatedException, UserNotEntitledException {
        when(systemBuildService.createNewMachine(newMachine)).thenReturn("host20230328005");
        String username = "Noah";
        when(authorisingService.isAuthorised(username)).thenReturn(true);
        requestor.createNewRequest(newMachine);
        verify(systemBuildService,times(1)).createNewMachine(newMachine);
    }

    @Test
    void requestorThrowsExceptionWhenUserIsEntitledButBuildFailsTest() throws MachineNotCreatedException, UserNotEntitledException {
        when(systemBuildService.createNewMachine(newMachine)).thenReturn("");
        int initalFailedRequests = requestor.totalFailedBuildsForDay();

        Assertions.assertThrows(MachineNotCreatedException.class,() -> requestor.createNewRequest(newMachine));

        verify(systemBuildService,times(1)).createNewMachine(newMachine);
    }


//    @Test
//    void requestorLogsFailedBuildWhenUserIsEntitledButBuildFailsTest() throws MachineNotCreatedException, UserNotEntitledException {
//        when(systemBuildService.createNewMachine(newMachine)).thenReturn("");
//        int initalFailedRequests = requestor.totalFailedBuildsForDay();
//        requestor.createNewRequest(newMachine);
//        int failedRequestsafterInvocation = requestor.totalFailedBuildsForDay();
//
//        Assertions.assertEquals(0, initalFailedRequests);
//        Assertions.assertEquals(1, failedRequestsafterInvocation);
//        verify(systemBuildService,times(1)).createNewMachine(newMachine);
//    }




}
