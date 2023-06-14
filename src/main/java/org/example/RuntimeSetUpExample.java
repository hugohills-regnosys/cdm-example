package org.example;

import cdm.event.common.functions.Create_ContractFormationInstruction;
import cdm.event.common.functions.Create_TerminationInstruction;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.isda.cdm.CdmRuntimeModule;

public class RuntimeSetUpExample {

    public static void main(String[] args) {
        // Guice dependency injection  - published by Google
        // All functionality wired in in the CdmRuntimeModule
        // Highly extensible
        Injector injector = Guice.createInjector(new CdmRuntimeModule());

        // Get any functionality from the injector...
        // For example, create an instruction to form a contract
        Create_ContractFormationInstruction createContractFormationInstruction =
                injector.getInstance(Create_ContractFormationInstruction.class);

        // Or, create an instruction to terminate
        Create_TerminationInstruction createTerminationInstruction =
                injector.getInstance(Create_TerminationInstruction.class);

        // Plus many many more functions available...
    }
}
