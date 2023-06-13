package org.example;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.PartyIdentifier;
import cdm.base.staticdata.party.PartyIdentifierTypeEnum;
import cdm.event.common.TradeIdentifier;
import cdm.event.common.TradeState;
import cdm.event.workflow.WorkflowStep;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.MetaFields;
import org.isda.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.example.ResourcesUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SwapTradeProcessorTest {

    private SwapTradeProcessor swapTradeProcessor;

    @BeforeEach
    public void setUp() {
        Injector injector = Guice.createInjector(new CdmRuntimeModule());
        swapTradeProcessor = injector.getInstance(SwapTradeProcessor.class);
    }

    @Test
    void shouldCreatePartialTerminationInstruction() throws IOException {
        // Trade to be partially terminated.  Note that all references are resolved here.
        TradeState beforeTradeState = getObjectAndResolveReferences(TradeState.class, "partial-termination/before-trade-state.json");

        WorkflowStep eventInstruction =
                swapTradeProcessor.createTerminationEventInstruction(beforeTradeState,
                        Date.of(2013, 2, 12),
                        BigDecimal.valueOf(7000000),
                        "USD")
                        .build();

        assertEquals(
                getJson("partial-termination/event-instruction.json"),
                toJson(eventInstruction));
    }

    @Test
    void shouldExecutePartialTerminationInstruction() throws IOException {
        // Trade to be partially terminated.  Note that all references are resolved here.
        WorkflowStep eventInstruction = getObjectAndResolveReferences(WorkflowStep.class, "partial-termination/event-instruction.json");

        WorkflowStep event = swapTradeProcessor.executeEvent(eventInstruction);

        assertEquals(
                getJson("partial-termination/event.json"),
                toJson(event));
    }

    @Test
    void shouldCreateNovationInstruction() throws IOException {
        // Trade to be novated.  Note that all references are resolved here.
        TradeState beforeTradeState = getObjectAndResolveReferences(TradeState.class, "novation/before-trade-state.json");

        WorkflowStep eventInstruction =
                swapTradeProcessor.createNovationEventInstruction(beforeTradeState,
                        Date.of(2013, 2, 12),
                        CounterpartyRoleEnum.PARTY_2,
                        getParty(),
                        getTradeIdentifier(),
                        "USD")
                        .build();

        assertEquals(
                getJson("novation/event-instruction.json"),
                toJson(eventInstruction));
    }

    @Test
    void shouldExecuteNovationInstruction() throws IOException {
        // Trade to be novated.  Note that all references are resolved here.
        WorkflowStep eventInstruction = getObjectAndResolveReferences(WorkflowStep.class, "novation/event-instruction.json");

        WorkflowStep event = swapTradeProcessor.executeEvent(eventInstruction);

        assertEquals(
                getJson("novation/event.json"),
                toJson(event));
    }

    private TradeIdentifier getTradeIdentifier() {
        return TradeIdentifier.builder()
                .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                .addAssignedIdentifier(AssignedIdentifier.builder().setIdentifierValue("UTI_Bank_Z"))
                .setIssuerValue("LEI_Bank_Z")
                .build();
    }

    private Party getParty() {
        return Party.builder()
                .setMeta(MetaFields.builder().setExternalKey("party3"))
                .setNameValue("Bank Z")
                .addPartyId(PartyIdentifier.builder()
                        .setIdentifierType(PartyIdentifierTypeEnum.LEI)
                        .setIdentifierValue("LEI_Bank_Z"))
                .build();
    }
}