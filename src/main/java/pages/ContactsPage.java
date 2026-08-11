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

    public boolean validateTextInMessageNoContacts(String text){
        return isTextInElementPresent(messageNoContacts,text);
    }

    public void printMessageNoContacts() {
        System.out.println(">>>>>>>> message >>>> " +
                messageNoContacts.getText());
    }
}
