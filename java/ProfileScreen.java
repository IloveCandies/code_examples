package screens.profile.android;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import io.qameta.allure.Step;
import base.android.Instruments;

public class ProfileScreen {

    private AppiumDriver driver;
    private Instruments Instruments;

    public static final String CONTAINER = "xpath://*[@id='mainProfileCONTAINERView']";
    public static final String TOOLBAR = "xpath://*[@id='toolbarView']";
    public static final String TITLE_LABEL = "xpath://*[@text='Профиль']"; // Заголовок Профиль
    public static final String MAIN_BUTTON = "id:menuIconView"; // кнопка Главная
    public static final String DIARY_BUTTON = "id:notesMenuItemView"; // кнопка Дневник
    public static final String STATISTICS_BUTTON = "id:statsMenuItemView"; // кнопка Статистика
    public static final String WEIGHT_CARD = "id:weightView"; // Карточка вес
    public static final String DIABETES_CARD = "id:diabetesTypeView";
    public static final String DIABETES_CARD_BUTTON_VALUE = "id:diabetesTypeButtonView";
    public static final String HEMOGLOBIN_CARD = "id:hemoglobinView";
    public static final String HEMOGLOBIN_VALUE = "xpath://*[@id='hemoglobinValueView']";
    public static final String GLUCOSE_LEVEL_CARD = "id:glucoseLevelView";
    public static final String DIABETES_LADA_VALUE = "xpath://*[@text='LADA']";
    public static final String ADDITIONAL_SETTINGS_TITLE = "xpath://*[@text='Дополнительные функции']";
    public static final String MY_WATCHERS = "xpath://*[@id='functionStateView' and ./preceding-sibling::*[@text='Мои наблюдатели']]";
    public static final String MY_DEVICES = "xpath://*[@id='functionStateView' and ./preceding-sibling::*[@text='Мои устройства']]";
    public static final String SALES_POINT = "xpath://*[@id='functionStateView' and ./preceding-sibling::*[@text='Где приобрести продукцию']]";
    public static final String SUPPORT_LINK = "xpath://*[@id='functionStateView' and ./preceding-sibling::*[@text='Поддержка']]";

    public static final String PROFILE_SETTINGS_LINK = "xpath://*[@text='Настройки профиля']";
    public static final String EXIT_LINK = "xpath://*[@text='Выйти из учетной записи']";

    public ProfileScreen(AppiumDriver driver) {
        this.driver = driver;
        this.Instruments = new Instruments(this.driver);
    }

    @Step("Скролл к дополнительным настройкам")
    public void clickProfileSettingsLink() {
        Instruments.waitForElementAndClick(PROFILE_SETTINGS_LINK, "Не найдена ссылка на настройки профиля");
    }

    @Step("Поиск Заголовка 'Профиль'")
    public WebElement getTitleLabel() {
        return Instruments.waitForElement(TITLE_LABEL, "Не найден заголовок 'Профиль'!");
    }

    @Step("Поиск карточки веса")
    public void clickWeightCard() {
        Instruments.waitForElementAndClick(WEIGHT_CARD, "Не найдена карточка 'Вес'");
    }

    @Step("Поиск карточки диабета")
    public void clickDiabetsCard() {
        Instruments.waitForElementAndClick(DIABETES_CARD, "Не найдена карточка 'Диабет'");
    }

    @Step("Поиск карточки гемоглобина")
    public void clickHemoglobinCard() {
        Instruments.waitForElementAndClick(HEMOGLOBIN_CARD, "Не найдена карточка 'Гемоглобин'");
    }

    @Step("Поиск установленного типа диабета")
    public WebElement getDiabetesCardButtonValue() {
        try {
            Thread.sleep(1300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Instruments.waitForElement(DIABETES_CARD_BUTTON_VALUE, "Не найден установленный тип диабета", 100);
    }

    @Step("Поиск установленного значения гемоглобина")
    public WebElement getHemoglobinValue() {
        return Instruments.waitForElement(HEMOGLOBIN_VALUE, "Не найдено значение гемоглобина");
    }

    @Step("Поиск карточки глюкозы")
    public void clickGlucoseLevelCard() {
        Instruments.waitForElementAndClick(GLUCOSE_LEVEL_CARD, "Не найдена карточка 'Уровень глюкозы'");
    }

    @Step("Скролл к дополнительным настройкам")
    public void scrollToAdditionalSettings() {
        Instruments.swipeUpFromElementToAnotherElement(ADDITIONAL_SETTINGS_TITLE, TOOLBAR, "Не найден scrollView");
    }

    @Step("Переход на экран 'Мои наблюдатели'")
    public void clickMyWatchersLink() {
        Instruments.waitForElementAndClick(MY_WATCHERS, "Не найден элемент");
    }

    @Step("Переход на экран 'Мои устройства'")
    public void clickMyDevicesLink() {
        Instruments.waitForElementAndClick(MY_DEVICES, "Не найден элемент");
    }

    @Step("Переход на экран 'Где приобреси продукцию'")
    public void clickSalesPointsLink() {
        Instruments.waitForElementAndClick(SALES_POINT, "Не найден элемент");
    }

    @Step("Переход на экран 'Поддрежка'")
    public void clickSupportLink() {
        Instruments.waitForElementAndClick(SUPPORT_LINK, "Не найден элемент");
    }

    @Step("Выход из приложения")
    public void clickExitLink() {
        Instruments.waitForElementAndClick(EXIT_LINK, "Не найден элемент");
    }
}
