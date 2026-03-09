package com.parabank.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AccountOverviewPage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(xpath = "//h1[@class='title']")
    private WebElement accountsOverviewTitle;
    
    @FindBy(linkText = "Log Out")
    private WebElement logoutLink;
    
    public AccountOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }
    
    public boolean isLoginSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOf(accountsOverviewTitle));
            String title = accountsOverviewTitle.getText();
            System.out.println("Page title: " + title);
            return title.equals("Accounts Overview");
        } catch (Exception e) {
            return false;
        }
    }
}