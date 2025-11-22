package by.pkt.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BetcityEventPageHelper extends HelperBase{

    private WebDriver webDriver;
    private WebDriverWait wait;
    private int implicitlyWait;

    public BetcityEventPageHelper(WebDriver webDriver, WebDriverWait wait, int implicitlyWait) {
        super(webDriver, wait, implicitlyWait);
        this.webDriver = webDriver;
        this.wait = wait;
        this.implicitlyWait = implicitlyWait;
    }
}
