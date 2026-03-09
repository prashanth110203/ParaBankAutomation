package com.parabank.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegistrationPage {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(id = "customer.firstName")
    private WebElement firstNameInput;
    
    @FindBy(id = "customer.lastName")
    private WebElement lastNameInput;
    
    @FindBy(id = "customer.address.street")
    private WebElement addressInput;
    
    @FindBy(id = "customer.address.city")
    private WebElement cityInput;
    
    @FindBy(id = "customer.address.state")
    private WebElement stateInput;
    
    @FindBy(id = "customer.address.zipCode")
    private WebElement zipCodeInput;
    
    @FindBy(id = "customer.phoneNumber")
    private WebElement phoneNumberInput;
    
    @FindBy(id = "customer.ssn")
    private WebElement ssnInput;
    
    @FindBy(id = "customer.username")
    private WebElement usernameInput;
    
    @FindBy(id = "customer.password")
    private WebElement passwordInput;
    
    @FindBy(id = "repeatedPassword")
    private WebElement confirmPasswordInput;
    
    @FindBy(xpath = "//input[@value='Register']")
    private WebElement registerButton;
    
    @FindBy(xpath = "//p[contains(text(),'Your account was created successfully')]")
    private WebElement successMessage;
    
    @FindBy(linkText = "Log Out")
    private WebElement logoutLink;
    
    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }
    
    public void navigateToRegistrationPage(String url) {
        driver.get(url);
        System.out.println("Navigated to: " + url);
    }
    
    public void fillRegistrationForm(String firstName, String lastName, String address,
                                      String city, String state, String zipCode,
                                      String phone, String ssn, String username, String password) {
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        
        firstNameInput.sendKeys(firstName);
        lastNameInput.sendKeys(lastName);
        addressInput.sendKeys(address);
        cityInput.sendKeys(city);
        stateInput.sendKeys(state);
        zipCodeInput.sendKeys(zipCode);
        phoneNumberInput.sendKeys(phone);
        ssnInput.sendKeys(ssn);
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        confirmPasswordInput.sendKeys(password);
        
        System.out.println("Registration form filled for: " + username);
    }
    
    public void submitRegistration() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();
        System.out.println("Registration submitted");
    }
    
    public boolean isRegistrationSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successMessage));
            System.out.println("Registration successful");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void logout() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
            logoutLink.click();
            Thread.sleep(2000);
            System.out.println("Logged out");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}