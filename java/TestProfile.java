package tests.android;

import org.testng.annotations.*;

import base.android.BaseTestAndroid;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;

import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Story;

import screens.add.android.AddActionPopup;
import screens.add.android.AddWeightScreen;
import screens.add.android.LabelsScreen;
import screens.additional_settings.android.SalesPointsScreen;
import screens.additional_settings.android.devices.AddDeviceScreen;
import screens.additional_settings.android.devices.InstructionScreen;
import screens.additional_settings.android.devices.MyDevicesScreen;
import screens.additional_settings.android.support.ServicePointsScreen;
import screens.additional_settings.android.support.SupportScreen;
import screens.additional_settings.android.watchers.AddWatcherScreen;
import screens.additional_settings.android.watchers.MyWatchersScreen;
import screens.main.android.MainScreen;
import screens.diary.android.DiaryScreen;
// import screens.statistics.android.StatiscticsScreen;
import screens.profile.android.DiabetsPopup;
import screens.profile.android.GlucosePopup;
import screens.profile.android.HemoglobinPopup;
import screens.profile.android.ProfileScreen;
import screens.profile_settings.android.ChangeNameScreen;
import screens.profile_settings.android.ChangePasswordScreen;
import screens.profile_settings.android.LegalInformationPopup;
import screens.profile_settings.android.ProfileSettingsScreen;
import screens.profile_settings.android.SpecifyGenderScreen;
import testrail.TestRails;
import testrail.APIClient;
import testrail.APIException;
import listeners.LifecycleListener;

@Feature("Экран - Профиль")
public class TestProfile extends BaseTestAndroid {

    public APIClient TestRailClient;

    @BeforeMethod
    @Parameters({ "MITMDUMP_PATH", "TESTRAIL", "TESTRAIL_URL" })
    public void startProxyAndTestrail(String MITMDUMP_PATH, Boolean TESTRAIL, String TESTRAIL_URL, ITestContext ctx,
            Method method) throws NoSuchMethodException {
        // GoResponseChanger = new GoResponseChanger(MITMDUMP_PATH);
        // GoResponseChanger.startDefaultProxy();
        if (TESTRAIL == true) {
            TestRailClient = new APIClient(TESTRAIL_URL);
            Method m = TestNavbar.class.getMethod(method.getName());
            if (m.isAnnotationPresent(TestRails.class)) {
                TestRails ta = m.getAnnotation(TestRails.class);
                ctx.setAttribute("caseId", ta.id());
            }
        }
    }

    @AfterMethod
    @Parameters({ "TESTRAIL", "TESTRAIL_LOGIN", "TESTRAIL_PASSWORD", "TESTRAIL_TESTRUN" })
    public void stopProxyAndTestrail(Boolean TESTRAIL, String TESTRAIL_LOGIN, String TESTRAIL_PASSWORD,
            String TESTRAIL_TESTRUN, ITestResult result, ITestContext ctx) throws IOException, APIException {
        // GoResponseChanger.stopProxy();
        if (TESTRAIL == true) {
            Map result_data = new HashMap();
            TestRailClient.setUser(TESTRAIL_LOGIN);
            TestRailClient.setPassword(TESTRAIL_PASSWORD);
            if (result.isSuccess()) {
                result_data.put("status_id", 1);
                result_data.put("comment", LifecycleListener.steps);
            } else {
                result_data.put("status_id", 5);
                result_data.put("comment", LifecycleListener.steps + result.getThrowable().toString());
            }
            int case_id = Integer.parseInt((String) ctx.getAttribute("caseId"));
            JSONObject testrail_response = (JSONObject) TestRailClient
                    .sendPost("add_result_for_case/" + TESTRAIL_TESTRUN + "/" + case_id, result_data);
            String result_id = testrail_response.get("id").toString();
            TestRailClient.sendPost("add_attachment_to_result/" + result_id, makeScreenshot("Screenshot_" + result_id));
        }
    }


    @Story("Сценарии")
    @Test(description = "Открытие экрана добавления веса через карточку, заполнение параметров на экране добавления веса")
    @TestRails(id = "")
    @Link(name = "Testrail Link", url = "")
    public void testAddWeightByCardInProfile() {
        MainScreen MainScreen = new MainScreen(driver);
        AddWeightScreen AddWeightScreen = new AddWeightScreen(driver);
        ProfileScreen ProfileScreen = new ProfileScreen(driver);
        LabelsScreen LabelsScreen = new LabelsScreen(driver);
        MainScreen.clickProfileButton();
        ProfileScreen.clickWeightCard();
        AddWeightScreen.swipeDownLeftWeightInput();
        AddWeightScreen.clickLabelButton();
        LabelsScreen.clickBreakfastLabel();
        LabelsScreen.clickСonfirmButton();
        AddWeightScreen.clickDateInput();
        AddWeightScreen.clickTimeInput();
        AddWeightScreen.writeNote();
        AddWeightScreen.clickSaveButton();
        Assert.assertTrue(AddWeightScreen.getWeightScreenTitle().isDisplayed());
    }

    @Story("Сценарии")
    @Test(description = "Добавления стандартного уровня гемоглобина через карточку")
    @TestRails(id = "")
    @Link(name = "Testrail Link", url = "")
    public void testAddHemoglobin() {
        MainScreen MainScreen = new MainScreen(driver);
        ProfileScreen ProfileScreen = new ProfileScreen(driver);
        HemoglobinPopup HemoglobinPopup = new HemoglobinPopup(driver);
        MainScreen.clickProfileButton();
        ProfileScreen.clickHemoglobinCard();
        HemoglobinPopup.addHemoglibinValue();
        Assert.assertTrue(ProfileScreen.getHemoglobinValue().isDisplayed());
    }

    @Story("Сценарии")
    @Test(description = "Открытие экрана точек продаж, поиск точки")
    @TestRails(id = "")
    @Link(name = "Testrail Link", url = "")
    public void testSearchSalesPoint() {
        MainScreen MainScreen = new MainScreen(driver);
        ProfileScreen ProfileScreen = new ProfileScreen(driver);
        SalesPointsScreen SalesPointsScreen = new SalesPointsScreen(driver);
        MainScreen.clickProfileButton();
        ProfileScreen.scrollToAdditionalSettings();
        ProfileScreen.clickSalesPointsLink();
        SalesPointsScreen.serchPointByAdres(
                "\u0422\u0435\u0441\u0442\u043e\u0432\u0430\u044f\u0020\u0443\u043b\u002e\u0020\u0033\u0031");
        Assert.assertTrue(SalesPointsScreen.getPointCardTittle("Тестовая ул. 31").isDisplayed());
    }

    @Story("Сценарии")
    @Test(description = "Открытие экрана точек обслуживания,поиск точки")
    @TestRails(id = "")
    @Link(name = "Testrail Link", url = "")
    public void testOpenServisePointsScreen() {
        MainScreen MainScreen = new MainScreen(driver);
        ProfileScreen ProfileScreen = new ProfileScreen(driver);
        SupportScreen SupportScreen = new SupportScreen(driver);
        ServicePointsScreen ServicePointsScreen = new ServicePointsScreen(driver);
        MainScreen.clickProfileButton();
        ProfileScreen.scrollToAdditionalSettings();
        ProfileScreen.clickSupportLink();
        SupportScreen.clickServicePointsLink();
        ServicePointsScreen.serchPointByAdres("addres");
        Assert.assertTrue(ServicePointsScreen.getPointCardTittle("addres").isDisplayed());
    }
}
