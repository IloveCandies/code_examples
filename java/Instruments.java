package base.android;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Allure;

public class Instruments {

    private AppiumDriver driver;

    public Instruments(AppiumDriver driver) {
        this.driver = driver;
    }

    private By getLocatorByString(String locator_with_type) {
        String[] exploded_locator = locator_with_type.split(Pattern.quote(":"), 2);
        String by_type = exploded_locator[0];
        String locator = exploded_locator[1];
        if (by_type.equals("xpath")) {
            return By.xpath(locator);
        } else if (by_type.equals("id")) {
            return By.id(locator);
        } else {
            throw new IllegalArgumentException("Cannot get type of locator: " + locator_with_type);
        }
    }

    public boolean checkIsElementExist(String id, long timeout) {
        By by = this.getLocatorByString(id);
        this.driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        if (this.driver.findElements(by).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkIsElementExist(String id) {
        return checkIsElementExist(id, 5);
    }

    public WebElement waitForElement(String id, String error_message, long timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.withMessage(error_message + '\n');
        By by = this.getLocatorByString(id);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public WebElement waitForElement(String id, String error_message) {
        return waitForElement(id, error_message, 5);
    }

    public WebElement waitForElementAndClick(String locator, String error_message, long timeout) {
        WebElement element = waitForElement(locator, error_message, timeout);
        element.click();
        return element;
    }

    public WebElement waitForElementAndClick(String locator, String error_message) {
        WebElement element = waitForElement(locator, error_message);
        element.click();
        return element;
    }

    public WebElement waitForElementAndSendKeys(String locator, String value, String error_message, long timeout) {
        WebElement element = waitForElement(locator, error_message, timeout);
        element.sendKeys(value);
        return element;
    }

    public WebElement waitForElementAndSendKeys(String locator, String value, String error_message) {
        WebElement element = waitForElement(locator, error_message);
        element.sendKeys(value);
        return element;
    }

    public WebElement waitForElementClearAndSendKeys(String locator, String value, String error_message) {
        WebElement element = waitForElement(locator, error_message);
        element.clear();
        element.sendKeys(value);
        return element;
    }

    public void waitAndPressBack(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.navigate().back();
    }

    public void waitAndTouch(int x, int y, int timeout) {
        TouchAction touchAction = new TouchAction(driver);
        touchAction
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(timeout * 1000)))
                .tap(PointOption.point(x, y)).perform();
    }

    public void waitAndSwipeDown(double startPercentage, double endPercentage, double anchorPercentage, int timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Dimension size = driver.manage().window().getSize();
        int anchor = (int) (size.width * anchorPercentage);
        int startPoint = (int) (size.height * startPercentage);
        int endPoint = (int) (size.height * endPercentage);
        new TouchAction(driver)
                .press(PointOption.point(anchor, startPoint))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(400)))
                .moveTo(PointOption.point(anchor, endPoint))
                .release().perform();
    }

    public void swipeFromElementToAnotherElement(String start_locator, String end_locator, String error_message) {
        WebElement start_element = waitForElement(start_locator, error_message);
        WebElement end_elemet = waitForElement(end_locator, error_message);
        TouchAction action = new TouchAction(driver);
        action.longPress(PointOption.point(start_element.getLocation().getX(), start_element.getLocation().getY()))
                .moveTo(PointOption.point(end_elemet.getLocation().getX(), end_elemet.getLocation().getY()))
                .release();
        action.perform();
    }
}
