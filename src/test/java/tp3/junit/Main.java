package tp3.junit;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.github.bonigarcia.wdm.WebDriverManager;
public class Main {
    WebDriver driver;
    JavascriptExecutor js;

    @BeforeAll
    public static void initialiser() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void preparer(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().setScriptTimeout(Duration.ofMinutes(2));

        js = (JavascriptExecutor) driver;
    }

    public static void takeScreenshot(WebDriver webdriver,String fileWithPath) throws Exception{
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile=new File(fileWithPath);
        FileUtils.copyFile(SrcFile, DestFile);
    }
    @Test
    public void todoTestCase() throws Exception {
        driver.get("https://todomvc.com");
        choosePlatform("Backbone.js");
        addTodo("Meet a Friend");
        addTodo("Buy Meat");
        addTodo("clean the car");
        removeTodo(1);
        removeTodo(3);
        Thread.sleep(1000);
        assertLeft(1);
        takeScreenshot(driver, "D:\\GL4\\2021-2022\\Qualite\\TP\\TP3 - JUnit\\TP2 - Selenium\\tp3.selenium\\screenshots\\screenshot.png"); ;
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "Backbone.js",
            "AngularJS",
            "Dojo",
            "React"
        })
    public void todosTestCase(String platform) throws Exception {
        driver.get("https://todomvc.com");
        choosePlatform(platform);
        addTodo("Meet a Friend");
        addTodo("Buy Meat");
        addTodo("clean the car");
        removeTodo(1);
        removeTodo(2);
        assertLeft(1);
        takeScreenshot(driver, "D:\\GL4\\2021-2022\\Qualite\\TP\\TP3 - JUnit\\TP2 - Selenium\\tp3.selenium\\screenshots\\screenshot_" + platform +".png");
    }

    private void choosePlatform(String platform) {
        WebElement element = driver.findElement(By.linkText(platform));
        element.click();
    }
    private void addTodo(String todo) throws InterruptedException {
        WebElement element = driver.findElement(By.className("new-todo"));
        element.sendKeys(todo);
        element.sendKeys(Keys.RETURN);
        Thread.sleep(1000);
    }
    private void removeTodo(int number) throws InterruptedException {
        WebElement element = driver.findElement(By.cssSelector("li:nth-child("+number+") .toggle"));
        element.click();
        Thread.sleep(1000);
    }
    private void assertLeft(int expectedLeft) throws InterruptedException {
        WebElement element = driver.findElement(By.xpath("//footer/*/span | //footer/span"));
            String expectedTest = String.format("$d item left", expectedLeft);
            validateInnerText(element,expectedTest);
        Thread.sleep(2000);
    }

    private void validateInnerText(WebElement element, String expectedTest) {
        ExpectedConditions.textToBePresentInElement(element, expectedTest);
    }

    @AfterEach
    public void quitter() throws InterruptedException {
        driver.quit();
    }
}
