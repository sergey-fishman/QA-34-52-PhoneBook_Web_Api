package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class ContactsPage extends BasePage {

    public ContactsPage(WebDriver driver) {
        PageFactory.initElements(new AjaxElementLocatorFactory
                (driver, 10), this);
    }

    @FindBy(xpath = "//*[@id='root']/div[2]/div/h1")
    WebElement messageNoContacts;

    @FindBy(xpath = "//div[@id='root']/div[1]")
    WebElement navBar;
    @FindBy(xpath = "//a[@href='/contacts']")
    WebElement linkContacts;

    public boolean validateTextInMessageNoContacts(String text){
        return isTextInElementPresent(messageNoContacts,text);
    }

    public boolean validateTextInNavbar(String text) {
        return isTextInElementPresent(navBar,text);
    }

    public boolean isLinkContactsDisplayed() {
        return linkContacts.isDisplayed();
    }

    public void printMessageNoContacts() {
        System.out.println(">>>>>>>> message >>>> " +
                messageNoContacts.getText());
    }
}
