package by.pkt.web;

import by.pkt.web.HelperBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BetcityCalendarPageHelper extends HelperBase {
    private int implicitlyWait;
    WebDriver webDriver;
    WebDriverWait wait;

    public BetcityCalendarPageHelper(WebDriver webDriver, WebDriverWait wait, int implicitlyWait) {
        super(webDriver, wait, implicitlyWait);
        this.implicitlyWait = implicitlyWait;
        this.webDriver = webDriver;
        this.wait = wait;
    }



    @FindBy(css = "a[class*=user-auth-block][href*=login]")
    WebElement buttonLogin;


}
