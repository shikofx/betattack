package by.pkt;

import by.pkt.domain.BookmeckerEvent;
import by.pkt.spies.BetcitySpy;
//import com.opencsv.CSVWriter;

import java.io.*;
import java.util.List;

public class Service {
    public static void main(String[] args) throws InterruptedException, IOException {
        BetcitySpy soccer = new BetcitySpy();
        List<BookmeckerEvent> soccers = soccer.getTargetEvents("soccer");
//        BetcitySpy hockey = new BetcitySpy();
//        List<BookmeckerEvent> hockeys = hockey.getTargetEvents("ice-hockey");
        System.out.println("The end!!!!");
    }
}
