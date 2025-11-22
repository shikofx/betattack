package by.pkt.spies;

import by.pkt.domain.BookmeckerEvent;
import by.pkt.domain.Championship;
import by.pkt.setup.BetcityProperties;
import by.pkt.web.BetcityEventPageHelper;
import by.pkt.web.BetcityCalendarPageHelper;
import by.pkt.web.PageNavigationHelper;
import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.*;

public class BetcitySpy {
    public static final int ZERO_IMPLICITLY_WAIT = 0;
    private List<BookmeckerEvent> eventList;
    private BetcityCalendarPageHelper mainPage;
    private BetcityEventPageHelper eventPage;

    WebDriver webDriver;
    private int implicitlyWait = 20;
    WebDriverWait wait;
    PageNavigationHelper pageNavigation;

    public BetcitySpy() throws IOException {
        webDriver = new ChromeDriver();
        setImplicitlyWait(30);
        wait = new WebDriverWait(webDriver, 30);
        mainPage = new BetcityCalendarPageHelper(webDriver, wait, implicitlyWait);
        eventPage = new BetcityEventPageHelper(webDriver, wait, implicitlyWait);
        pageNavigation = new PageNavigationHelper(webDriver, wait, implicitlyWait);
        webDriver.get(new BetcityProperties().BETCITY_URL);

        closePushConfirm();
    }

    private void closePushConfirm() {
        By dropdownForm = By.cssSelector(".push-confirm");
        wait.until(d -> d.findElement(dropdownForm));
        if (mainPage.isElementPresent(dropdownForm)) {
            if (webDriver.findElement(dropdownForm).isDisplayed()) {
                webDriver.findElement(By.cssSelector(".push-confirm__button:not(.push-confirm__button_agree)")).click();
            }
        }
    }

    public List<BookmeckerEvent> getTargetEvents(String kindOfSport) throws InterruptedException, IOException {

//        WebElement filterFootball = wait.until(d -> d.findElement(By.xpath("//span[@class='sports-filter__item-text' and .='??????']")));
//        filterFootball.click();

        setExplicitlyWait(5);
        List<BookmeckerEvent> events = getAllEventsUrls(kindOfSport);
        List<BookmeckerEvent> targetEvents = new ArrayList<>();
        Date date = new Date();
        File file = new File("reports/betcity-" + kindOfSport + "-" + date.getTime() + ".csv");
        file.createNewFile();

        CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file), "windows-1251"));

        int counter = 0;
        for (BookmeckerEvent event : events) {
            try {
                counter++;

                goTo(event);
                setImplicitlyWait(10);
                By totalPenaltyBlockBy = By.xpath("//div[@class='dops-item' and .//*[contains(text(),'енальт')] and not(.//*[contains(text(),'енальти ')]) and count(.//div/div)<7]");
                if(mainPage.isElementPresent(totalPenaltyBlockBy, 5)) {
                    WebElement eventAttackBlock = webDriver.findElement(totalPenaltyBlockBy);
                    WebElement eventLineElement = webDriver.findElement(By.cssSelector(".line-event"));
                    By championshipBy = By.cssSelector(".line-champ__header-name a");
                    WebElement champElement = webDriver.findElement(championshipBy);
                    Championship champ = new Championship()
                        .withKindOfSport(kindOfSport)
                        .withName(champElement.getText())
                        .withUrl(champElement.getAttribute("href"));
                    event
                        .withChamp(champ)
                        .withFirstCommand(
                            eventLineElement.findElement(By.cssSelector(".line-event__name-teams :first-child"))
                                .getText())
                        .withSecondCommand(
                            eventLineElement.findElement(By.cssSelector(".line-event__name-teams :last-child"))
                                .getText())
                        .withBeginningTime(
                            eventLineElement.findElement(By.cssSelector(".line-event__time-static")).getText())
                        .withCoefficient(webDriver.findElement(
                            By.xpath(
                                "//div[@class='dops-item' and .//*[contains(text(),'енальти')] and not(.//*[contains(text(),'енальти ')]) and count(.//div/div)<7]//*[contains(text(),'Не') or contains(text(),'Мен') or contains(text(),'мен')]/following-sibling::button"))
                                             .getText())
                        .withURL(event.getUrl());
                    event.stringToCsvFile();
                    System.out.println(counter + "/" + events.size() + ": " + event.toString());
                    writer.writeNext(event.stringToCsvFile());
                    writer.flush();
                    targetEvents.add(event);
                }
                System.out.println(counter + "/" + events.size());
            } catch (NoSuchElementException e){
                e.printStackTrace();
            }
        }

        writer.close();
        deinit();
        return targetEvents;
    }

    private void setExplicitlyWait(int seconds) {
        wait.withTimeout(ofSeconds(seconds));
    }

    private void setImplicitlyWait(int seconds) {
        webDriver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    private void goTo(BookmeckerEvent event) {
        webDriver.get(String.valueOf(event.getUrl()));
    }

    private List<BookmeckerEvent> getAllEventsUrls(String hexFilter) {
        List<BookmeckerEvent> events = new ArrayList<>();
        List<WebElement> eventElements = wait.until(d -> d.findElements(By.cssSelector(".live-soon-block-event__title a")));
        for (WebElement element : eventElements) {
            String eventStr = element.getAttribute("href");
            if (eventStr.contains(hexFilter)) {
                events.add(new BookmeckerEvent()
                        //.withChamp(new Championship())
                        .withURL(eventStr));
            }
        }
        return events;
    }

    private void deinit() {
        webDriver.close();
        webDriver = null;
    }

    private void login() {

        WebElement buttonLoginForm = wait.until(d -> d.findElement(By.cssSelector("a[class*=user-auth-block][href*=login]")));
        buttonLoginForm.click();
        WebElement formLogin = wait.until(d -> d.findElement(By.cssSelector("app-login-block")));
        WebElement usernameInput = webDriver.findElement(By.cssSelector("input[name=login]"));
        usernameInput.click();
        usernameInput.sendKeys("tanechka28");
        WebElement passwordInput = webDriver.findElement(By.cssSelector("input[name=pass]"));
        passwordInput.click();
        passwordInput.sendKeys("140983");
        WebElement buttonLogin = webDriver.findElement(By.cssSelector(".login__submit"));
        buttonLogin.click();
    }

}
