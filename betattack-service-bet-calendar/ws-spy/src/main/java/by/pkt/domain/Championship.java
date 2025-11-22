package by.pkt.domain;

import java.net.URL;

public class Championship {
    String kindOfSport;
    String name;
    String url;

    public Championship withKindOfSport(String kindOfSport) {
        this.kindOfSport = kindOfSport;
        return this;
    }

    public Championship withName(String name) {
        this.name = name;
        return this;
    }

    public Championship withUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return "Championship{" +
                "name='" + name + '\'' +
                '}';
    }

    public String stringToFile() {
        return name;
    }
}
