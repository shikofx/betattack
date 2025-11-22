package by.pkt.web;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class HelperBase {
   private int implicitlyWait;
   WebDriver webDriver;
   WebDriverWait wait;

   protected HelperBase(WebDriver webDriver, WebDriverWait wait, int implicitlyWait) {
      PageFactory.initElements(webDriver, this);
      this.webDriver = webDriver;
      this.wait = wait;
      this.implicitlyWait = implicitlyWait;

   }

   public void clearBrowserData() {
      webDriver.manage().deleteAllCookies();
   }

   public void refreshPage() {
      webDriver.navigate().refresh();
   }

   void displayDropDown(WebElement whatClick, WebElement whatWait, int secondsToWait) {
      wait.until(elementToBeClickable(whatClick));
//      wait.until((WebDriver d) -> whatClick.getTagName());
      boolean isClickable = whatClick.isDisplayed();
      boolean whatW = isElementPresent(whatWait, 1);
      if (isClickable && !whatW) {
         whatClick.click();
         //   wait.until(visibilityOf(whatWait));//!!!!!!!!!!!!!!!!!!!!!
      } else if (!whatWait.isDisplayed()) {
         whatClick.click();
      }
   }

   void hideDropdown(WebElement whatClick, WebElement whatWait) {
      wait.until((WebDriver d) -> {
         if (whatWait.isDisplayed()) {
            whatClick.click();
         } else {
            return true;
         }
         return false;
      });
   }

   String textByPattern(String regex, String text) {
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(text);
      String result = null;
      while (matcher.find()) {
         result = text.substring(matcher.start(), matcher.end());
      }
      return result;
   }

   String textByPatternWithout(String pattern, String withoutRegex, String text) {
      return textByPattern(pattern, text).replaceAll(withoutRegex, "");
   }


   private boolean isElementDisplayed(By whatWait) {
      return webDriver.findElement(whatWait).isDisplayed();
   }

   public boolean isElementPresent(By locator) {
      int w = webDriver.findElements(locator).size();
      return webDriver.findElements(locator).size() > 0;
   }
   public boolean isElementPresent(By locator, int secondsToWait) {
      setImplicitlyWait(secondsToWait);
      boolean isPresent = false;
      try {
         webDriver.findElement(locator).getTagName();
         isPresent = true;
      } catch (Exception ignored) {
      }
      setImplicitlyWait(implicitlyWait);
      return isPresent;
   }

   boolean isElementPresentNoWait(WebElement inElement, By locator) {
      setImplicitlyWait(0);
      boolean isPresent = inElement.findElements(locator).size() > 0;
      setImplicitlyWait(implicitlyWait);
      return isPresent;
   }

   public boolean isElementPresent(WebElement inElement, By locator) {
      return inElement.findElements(locator).size() > 0;
   }


   boolean isElementPresent(WebElement element, int secondsToWait) {
      setImplicitlyWait(secondsToWait);
      boolean isPresent = false;
      try {
         element.getTagName();
         isPresent = true;
      } catch (NullPointerException | NoSuchElementException ignored) {
      }
      setImplicitlyWait(implicitlyWait);
      return isPresent;
   }

   void isElementPresentAndVisible(WebElement e) {
      wait.until((WebDriver d) -> e.getTagName());
      if (isElementPresent(e, 0)) {
         e.isDisplayed();
      }
   }

   private void setImplicitlyWait(int secondsToWait) {
      webDriver.manage().timeouts().implicitlyWait(secondsToWait, TimeUnit.SECONDS);
   }

   void inputText(WebElement input, String text) {
      wait.until(elementToBeClickable(input));
      input.click();
      input.clear();
      input.sendKeys(text);
   }

   private Alert alertAfterClick(By whatClick) {
      wait.until(visibilityOfElementLocated(whatClick));
      return wait.until((WebDriver d) -> {
         d.findElement(whatClick).click();
         try {
            return d.switchTo().alert();
         } catch (NoAlertPresentException e) {
            return null;
         }
      });
   }

   Alert alertAfterClick(WebElement whatClick) {
      wait.until(visibilityOfAllElements(whatClick));
      return wait.until((WebDriver d) -> {
         whatClick.click();
         try {
            return d.switchTo().alert();
         } catch (NoAlertPresentException e) {
            return null;
         }
      });
   }

}
