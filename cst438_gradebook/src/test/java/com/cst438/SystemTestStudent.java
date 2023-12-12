package com.cst438;

import com.sun.xml.bind.v2.runtime.reflect.Lister;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SystemTestStudent {
    public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/adisa/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe";

    public static final String URL = "http://localhost:3000";
    public static final int SLEEP_DURATION = 1000; // 1 second.

    @Test
    public void addAssignmentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver wd = new ChromeDriver();
        wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wd.get(URL);
        Thread.sleep(SLEEP_DURATION);

        try {
            WebElement addButton = wd.findElement(By.id("ADD"));
            addButton.click();

            WebElement add = wd.findElement(By.xpath("//button[contains(text(), 'Add Assignment')]"));
            add.click();

            WebElement name = wd.findElement(By.name("assignmentName"));
            name.sendKeys("db design");

            WebElement courseID = wd.findElement(By.name("courseID"));
            courseID.sendKeys("31045");

            WebElement due = wd.findElement(By.name(("dueDate")));
            due.sendKeys("10132002");

            WebElement submit = wd.findElement(By.xpath("//button[contains(text(), 'Submit')]"));
            submit.click();

            WebElement success = wd.findElement(By.xpath("//p[contains(text(), 'Assignment added successfully')]"));

            assert (submit.isDisplayed());

        } catch (Exception e) {
            throw e;
        } finally {
            wd.quit();
        }
    }


    @Test
    public void updateAssignmentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver wd = new ChromeDriver();
        wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wd.get(URL);
        Thread.sleep(SLEEP_DURATION);

        try {
            WebElement edit = wd.findElement(By.xpath("//td[contains(text(),'requirements')]/following-sibling::td/button[contains(text(),'Edit')]"));
            edit.click();

            WebElement name = wd.findElement(By.name("assignmentName"));
            name.sendKeys("Edited requirements");

            WebElement date = wd.findElement(By.name("dueDate"));
            date.clear();
            date.sendKeys("08302002");

            WebElement update = wd.findElement(By.xpath("//button[contains(text(), 'Update')]"));
            update.click();

            WebElement success = wd.findElement(By.xpath("//td[contains(text(), 'Edited requirements')]"));
            assert (success.isDisplayed());

        } catch (Exception e) {
            throw e;
        } finally {
            wd.quit();
        }
    }

    //todo delete test
    @Test
    public void deleteAssignmentTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
        WebDriver wd = new ChromeDriver();
        wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wd.get(URL);
        Thread.sleep(SLEEP_DURATION);

        try {
            WebElement delete = wd.findElement(By.xpath("//td[contains(text(), 'db design')]/following-sibling::td/button[contains(text(), 'Force Delete')]"));
            delete.click();

            WebElement message = wd.findElement(By.xpath("//h4[contains(text(), 'Assignment deleted')]"));
            assert (message.isDisplayed());


        } catch (Exception e) {
            throw e;
        } finally {
            wd.quit();
        }
    }
}
