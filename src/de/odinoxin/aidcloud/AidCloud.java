package de.odinoxin.aidcloud;

import de.odinoxin.aidcloud.plugins.addresses.Addresses;
import de.odinoxin.aidcloud.plugins.countries.Countries;
import de.odinoxin.aidcloud.plugins.people.People;
import de.odinoxin.aidcloud.refbox.RefBox;

import javax.xml.ws.Endpoint;

public class AidCloud {
    private static final String ADDRESS = "http://localhost:15123/AidCloud";

    public static void main(String[] args) {
        Endpoint.publish(AidCloud.ADDRESS + "/Login", new Login());
        Endpoint.publish(AidCloud.ADDRESS + "/Translator", new Translator());
        Endpoint.publish(AidCloud.ADDRESS + "/RefBox", new RefBox());
        Endpoint.publish(AidCloud.ADDRESS + "/People", new People());
        Endpoint.publish(AidCloud.ADDRESS + "/Addresses", new Addresses());
        Endpoint.publish(AidCloud.ADDRESS + "/Countries", new Countries());

        System.out.println("AidCloud is online now!");
    }
}
