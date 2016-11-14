package de.odinoxin.aidcloud;

import de.odinoxin.aidcloud.plugins.addresses.AddressProvider;
import de.odinoxin.aidcloud.plugins.contact.information.ContactInformationProvider;
import de.odinoxin.aidcloud.plugins.contact.types.ContactTypeProvider;
import de.odinoxin.aidcloud.plugins.countries.CountryProvider;
import de.odinoxin.aidcloud.plugins.languages.LanguageProvider;
import de.odinoxin.aidcloud.plugins.people.PersonProvider;
import de.odinoxin.aidcloud.translation.Translator;

import javax.xml.ws.Endpoint;

public class AidCloud {
    public static final String INVALID_SESSION = "Invalid session, or session is over!";
    private static final String ADDRESS = "http://localhost:15123/AidCloud";

    public static void main(String[] args) {
        Endpoint.publish(AidCloud.ADDRESS + "/Login", new Login());
        Endpoint.publish(AidCloud.ADDRESS + "/LanguageProvider", new LanguageProvider());
        Endpoint.publish(AidCloud.ADDRESS + "/Translator", Translator.get());
        Endpoint.publish(AidCloud.ADDRESS + "/PersonProvider", new PersonProvider());
        Endpoint.publish(AidCloud.ADDRESS + "/AddressProvider", new AddressProvider());
        Endpoint.publish(AidCloud.ADDRESS + "/CountryProvider", new CountryProvider());
        Endpoint.publish(AidCloud.ADDRESS + "/ContactTypeProvider", new ContactTypeProvider());
        Endpoint.publish(AidCloud.ADDRESS + "/ContactInformationProvider", new ContactInformationProvider());
        System.out.println("AidCloud is online now!");
    }
}
