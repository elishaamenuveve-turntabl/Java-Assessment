package com.vmorg.buildrequest;


import com.vmorg.auth.AuthorisingService;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BuildRequestTest {
    VirtualMachineRequestor requestor;
    @Mock
    Machine newMachine;

    @Mock
    AuthorisingService authorisingService;

    @BeforeEach
    void setUp() {
        authorisingService = mock(AuthorisingService.class);
        requestor = new MachineRequestor("Noah", authorisingService);

    }

    @Test
    void vmCreatedUserNotEntitled() throws MachineNotCreatedException, UserNotEntitledException {
        String username = "Noah";
        when(authorisingService.isAuthorised(username)).thenReturn(false);


        Assertions.assertThrows(UserNotEntitledException.class, ()->requestor.createNewRequest(newMachine));


    }


}
