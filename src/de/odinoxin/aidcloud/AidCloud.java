package de.odinoxin.aidcloud;

import de.odinoxin.aidcloud.plugins.people.People;

import javax.xml.ws.Endpoint;

public class AidCloud {
    private static final String ADDRESS = "http://localhost:15123/AidCloud";

    public static void main(String[] args) {
        Endpoint.publish(AidCloud.ADDRESS + "/Login", new Login());
        Endpoint.publish(AidCloud.ADDRESS + "/People", new People());

        System.out.println("AidCloud is online now!");
    }
}
