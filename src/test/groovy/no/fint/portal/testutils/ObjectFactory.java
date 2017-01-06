package no.fint.portal.testutils;

import no.fint.portal.component.Adapter;
import no.fint.portal.component.Client;
import no.fint.portal.component.Component;
import no.fint.portal.organisation.Contact;
import no.fint.portal.organisation.Organisation;

public enum ObjectFactory {
    ;

    public static Component newComponent() {
        Component component = new Component();
        component.setTechnicalName("compTest");
        component.setDisplayName("Comp Test");
        component.setDescription("Created by test");
        return component;
    }

    public static Adapter newAdapter() {
        Adapter adapter = new Adapter();
        adapter.setNote("Test adapter for test organisation");
        adapter.setOrgId("test.no");
        adapter.setShortDescription("Test Adapter");
        return adapter;
    }

    public static Client newClient() {
        Client client = new Client();
        client.setNote("Test client for test organisation");
        client.setOrgId("test.no");
        client.setShortDescription("Test Client");
        return client;
    }

    public static Organisation newOrganisation() {
        Organisation organisation = new Organisation();
        organisation.setOrgId("test.no");
        organisation.setOrgNumber("1111111111");
        organisation.setDisplayName("Test organisation");
        return organisation;
    }

    public static Contact newContact() {
        Contact contact = new Contact();
        contact.setNin("111111111");
        return contact;
    }
}
