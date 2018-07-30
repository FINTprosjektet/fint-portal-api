package no.fint.portal.testutils;

import no.fint.portal.model.adapter.Adapter;
import no.fint.portal.model.asset.Asset;
import no.fint.portal.model.client.Client;
import no.fint.portal.model.component.Component;
import no.fint.portal.model.contact.Contact;
import no.fint.portal.model.organisation.Organisation;
import no.fint.portal.oauth.OAuthClient;

public enum ObjectFactory {
    ;

    public static Component newComponent() {
        Component component = new Component();
        //component.setDn("ou=comp1,ou=comp,o=fint");
        component.setName("compTest");
        component.setDescription("Created by test");
        return component;
    }

    public static Adapter newAdapter() {
        Adapter adapter = new Adapter();
        adapter.setName("TestAdapter");
        adapter.setNote("Test adapter for test organisation");
        adapter.setShortDescription("Test Adapter");
        adapter.setClientId("123");
        return adapter;
    }

    public static Client newClient() {
        Client client = new Client();
        client.setName("TestClient");
        client.setNote("Test client for test organisation");
        client.setShortDescription("Test Client");
        client.setClientId("123");
        return client;
    }

    public static Organisation newOrganisation() {
        Organisation organisation = new Organisation();
        organisation.setName("TestOrganisation");
        organisation.setOrgNumber("1111111111");
        organisation.setDisplayName("Test organisation");
        organisation.setDn("ou=testOrg,ou=org,o=fint");
        return organisation;
    }

    public static Contact newContact() {
        Contact contact = new Contact();
        contact.setNin("111111111");
        contact.setDn("cn=111111111,ou=contacts,o=fint");
        return contact;
    }

    public static Asset newAsset() {
        Asset asset = new Asset();
        asset.setName("assetName");
        asset.setAssetId("test.no");
        asset.setPrimaryAsset(true);
        return asset;
    }

    public static OAuthClient newOAuthClient() {
        OAuthClient oAuthClient = new OAuthClient();
        oAuthClient.setClientId("123");
        oAuthClient.setClientSecret("secret");

        return oAuthClient;
    }
}
