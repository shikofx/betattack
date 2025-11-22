package by.pkt.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

public class PageNavigationHelper extends HelperBase {


   public PageNavigationHelper(WebDriver webDriver, WebDriverWait wait, int implicitlyWait) {
      super(webDriver, wait, implicitlyWait);
      PageFactory.initElements(webDriver, this);
   }

   public void goTo(String url) {
      webDriver.get(url);
   }

   private String getNew(Set<String> oldWindowSet) {
      Set<String> newWindowSet = webDriver.getWindowHandles();
      String newWindow = null;
      if (newWindowSet.removeAll(oldWindowSet) && newWindowSet.size() == 1) {
         newWindow = newWindowSet.iterator().next();
      }
      return newWindow;
   }


   public void maximizeWindow() {
      webDriver.manage().window().maximize();
   }

   public void switchTo(String newHandle) {
      webDriver.switchTo().window(newHandle);
   }


   public void switchToNew(Set<String> oldWindowSet) {
      webDriver.switchTo().window(getNew(oldWindowSet));
   }

   private void closeCurrent() {
      webDriver.close();
   }

   public Set<String> all() {
      return webDriver.getWindowHandles();
   }

   public String getCurrent() {
      return webDriver.getWindowHandle();
   }

   public void back() {
      String currentUrl = webDriver.getCurrentUrl();
      webDriver.navigate().back();
   }

   public void closeAll() {
      for (String s : all()) {
         webDriver.switchTo().window(s);
         webDriver.close();
      }
   }

   public PageNavigationHelper close(String wishlistWindowHandle) {
      switchTo(wishlistWindowHandle);
      closeCurrent();
      return this;
   }
}
