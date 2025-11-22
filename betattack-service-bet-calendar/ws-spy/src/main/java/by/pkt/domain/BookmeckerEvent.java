package by.pkt.domain;

import java.util.stream.Stream;

public class BookmeckerEvent {
    private Championship champ;
    private String firstCommand;
    private String secondCommand;
    private String timeStart;
    private String coefficient;
    private String url;

    public BookmeckerEvent() {
        this.champ = new Championship();
    }

    public BookmeckerEvent withChamp(Championship champ) {
        this.champ = champ;
        return this;
    }

    public BookmeckerEvent withFirstCommand(String firstCommand) {
        this.firstCommand = firstCommand;
        return this;
    }

    public BookmeckerEvent withSecondCommand(String secondCommand) {
        this.secondCommand = secondCommand;
        return this;
    }

    public BookmeckerEvent withBeginningTime(String timeStatic) {
        this.timeStart = timeStatic;
        return this;
    }

    public BookmeckerEvent withCoefficient(String coeff) {
        this.coefficient = coeff;
        return this;
    }

    public BookmeckerEvent withURL(String url) {
        this.url = url;
        return this;
    }

    public Championship getChamp() {
        return champ;
    }

    public String getFirstCommand() {
        return firstCommand;
    }

    public String getSecondCommand() {
        return secondCommand;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getCoefficient() {
        return coefficient;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "BookmeckerEvent{" +
                champ.toString() +
                ": " + firstCommand + '\'' +
                " <-> " + secondCommand + '\'' +
                " - " + timeStart + '\'' +
                " +" + coefficient +
                " url=" + url +
                '}';
    }

    public String[] stringToCsvFile(){
        return new String[]{timeStart, coefficient, url, firstCommand, secondCommand, champ.stringToFile()};
    }
}
