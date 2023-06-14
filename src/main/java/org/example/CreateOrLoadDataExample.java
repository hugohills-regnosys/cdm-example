package org.example;

import cdm.base.staticdata.party.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;

public class CreateOrLoadDataExample {

    /**
     * Load or create CDM data
     */
    public static void main(String[] args) throws JsonProcessingException {
        // Creating CDM data using Java.
        // Builder pattern
        Party party1 = Party.builder()
                .setNameValue("REGnosys")
                .addPartyId(PartyIdentifier.builder()
                        .setIdentifierValue("XYZ123")
                        .setIdentifierType(PartyIdentifierTypeEnum.LEI))
                .addPerson(NaturalPerson.builder()
                        .addPersonRole(NaturalPersonRole.builder()
                                .addRoleValue(NaturalPersonRoleEnum.TRADER))
                        .setFirstName("Hugo")
                        .setSurname("Hills"))
                .build();

        // Load CDM data by deserializing JSON.
        // Utility classes provided
        Party party2 = RosettaObjectMapper.getNewRosettaObjectMapper()
                .readValue("""
                        {
                          "name" : { "value" : "REGnosys" },
                          "partyId" : [ {
                            "identifier" : { "value" : "XYZ123" },
                            "identifierType" : "LEI"
                          } ],
                          "person" : [ {
                            "firstName" : "Hugo",
                            "surname" : "Hills",
                            "personRole" : [ {
                              "role" : [ { "value" : "TRADER" } ]
                            } ]
                          } ]
                        }
                        """, Party.class);
    }
}
