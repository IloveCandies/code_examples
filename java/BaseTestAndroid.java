package base.android;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

import com.google.common.collect.ImmutableMap;

import org.testng.annotations.*;

import java.net.URL;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;

public class BaseTestAndroid {

    public AppiumDriver driver;

    @BeforeSuite(alwaysRun = true)
    @Parameters({ "BUILD", "DEVICE_MODEL", "PLATFORM_NAME", "PLATFORM_VERSION", "TESTRAIL_TESTRUN" })
    public void setAllureEnvironment(String BUILD, String DEVICE_MODEL, String PLATFORM_NAME, String PLATFORM_VERSION,
            String TESTRAIL_TESTRUN) {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Build", BUILD)
                        .put("Device", DEVICE_MODEL + " " + PLATFORM_NAME + " " + PLATFORM_VERSION)
                        .put("Testrail", "https://testrail.kode.ru/index.php?/runs/view/" + TESTRAIL_TESTRUN)
                        .build(),
                System.getProperty("user.dir") + "/allure-results/");
    }

    @BeforeMethod
    @Parameters({ "PLATFORM_NAME", "PLATFORM_VERSION", "DEVICE_NAME", "APP", "APPIUM_URL" })
    public void setUp(String PLATFORM_NAME, String PLATFORM_VERSION, String DEVICE_NAME, String APP, String APPIUM_URL)
            throws Exception {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", PLATFORM_NAME);
        capabilities.setCapability("platformVersion", PLATFORM_VERSION);
        capabilities.setCapability("deviceName", DEVICE_NAME);
        capabilities.setCapability("app", APP);
        capabilities.setCapability("noReset", "true"); // true для студии со старым uiautomator
        capabilities.setCapability("fullReset", "false");
        driver = new AndroidDriver(new URL(APPIUM_URL), capabilities);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        Allure.addAttachment("Final result",
                new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES))); // для Allure
        driver.closeApp(); // закоментить для запуска через студию, раскоментить для
        // запуска через cli
        driver.quit();
    }

    public String makeScreenshot(String filename) { // для TestRail
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshot_path = System.getProperty("user.dir") + "/screenshots/" + filename + ".jpg";
        try {
            FileUtils.copyFile(file, new File(screenshot_path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenshot_path;
    }

}
