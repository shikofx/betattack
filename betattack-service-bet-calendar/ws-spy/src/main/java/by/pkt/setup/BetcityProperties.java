package by.pkt.setup;

import java.io.IOException;

public class BetcityProperties extends PropertiesFactory {
    public final String BETCITY_PROPERTIES_FILE = "betcity.properties";
    public final String BETCITY_URL;
    public final double BETCITY_SIZE_OF_ATTACK;

    public BetcityProperties() throws IOException {
        BETCITY_URL = getProperties("vicUrl", BETCITY_PROPERTIES_FILE);
        BETCITY_SIZE_OF_ATTACK = Double.parseDouble(getProperties("maxSizeOfAttack", "betcity.properties"));
    }
}
