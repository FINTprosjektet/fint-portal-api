package no.fint.portal.testutils;

import no.fint.portal.adapter.Adapter;
import no.fint.portal.client.Client;
import no.fint.portal.component.Component;
import no.fint.portal.contact.Contact;
import no.fint.portal.oauth.OAuthClient;
import no.fint.portal.organisation.Organisation;

public enum ObjectFactory {
    ;

    public static Component newComponent() {
        Component component = new Component();
        //component.setDn("ou=comp1,ou=comp,o=fint");
        component.setTechnicalName("compTest");
        component.setDisplayName("Comp Test");
        component.setDescription("Created by test");
        return component;
    }

    public static Adapter newAdapter() {
        Adapter adapter = new Adapter();
        //adapter.setDn("cn=a1,ou=adapter,ou=org,o=fint");
        adapter.setNote("Test adapter for test organisation");
        adapter.setOrgId("test.no");
        adapter.setShortDescription("Test Adapter");
        adapter.setClientId("123");
        return adapter;
    }

    public static Client newClient() {
        Client client = new Client();
        //client.setDn("cn=c1,ou=client,ou=org,o=fint");
        client.setNote("Test client for test organisation");
        client.setOrgId("test.no");
        client.setShortDescription("Test Client");
        client.setClientId("123");
        return client;
    }

    public static Organisation newOrganisation() {
        Organisation organisation = new Organisation();
        //organisation.setDn("ou=org1,ou=org,o=fint");
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

    public static OAuthClient newOAuthClient() {
        OAuthClient oAuthClient = new OAuthClient();
        oAuthClient.setClientId("123");
        oAuthClient.setClientSecret("secret");

        return oAuthClient;
    }
}
