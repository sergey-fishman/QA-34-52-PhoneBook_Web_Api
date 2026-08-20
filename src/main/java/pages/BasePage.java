package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    static WebDriver driver;

    public void setDriver(WebDriver wd) {
        driver = wd;
    }

    public boolean isTextInElementPresentSimple(WebElement element, String text) {
        return element.getText().contains(text);
    }

    public boolean isTextInElementPresent(WebElement element, String text) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions
                            .textToBePresentInElement
                                    (element, text));
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("created exception");
        }
        return false;
    }

    public boolean isUrlContainsText(String text) {
        try {
            return new WebDriverWait(driver,Duration.ofSeconds(5))
                    .until(ExpectedConditions.urlContains(text));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Alert getAlert() {
        return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions
                        .alertIsPresent());
    }

    public String closeAlert() {
        Alert alert = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions
                        .alertIsPresent());
        String text = alert.getText();
        alert.accept();
        return text;
    }

    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
