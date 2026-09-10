package utils;

import manager.AppManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListener implements ITestListener {

    Logger logger = LoggerFactory.getLogger(TestNGListener.class);
    private WebDriver driver;

    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
        logger.info("starting test: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
        logger.info("test success: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            logger.error("Test failed: {}, Message: {}",
                    result.getName(), throwable.getMessage());
        } else
            logger.error("Test failed: {}, Status: {}",
                    result.getName(), result.getStatus());

        this.driver = ((AppManager) result.getInstance()).getDriver();

        // Пытаемся спасти текст из Alert до того, как его закроет скриншот
        try {
            Alert alert = driver.switchTo().alert();
            logger.warn("⚠️ During failure Alert detected. Alert text: [{}]", alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e){
            logger.info(e.getMessage());
        }
        Screenshot.takeScreenshot((TakesScreenshot) driver);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
        logger.warn("test skipped: {}", result.getName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
        logger.error("test failed with timeout: {}, status: {}",
                result.getName(), result.getStatus());

        this.driver = ((AppManager) result.getInstance()).getDriver();
        Screenshot.takeScreenshot((TakesScreenshot) driver);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
        logger.info("{} start at {}",
                context.getName(), context.getStartDate());
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
        logger.info("{} finish at {}",
                context.getName(), context.getEndDate());
    }
}
