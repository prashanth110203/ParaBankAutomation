package com.parabank.tests;

import com.parabank.config.TestConfig;
import com.parabank.pages.AccountOverviewPage;
import com.parabank.pages.LoginPage;
import com.parabank.pages.RegistrationPage;
import com.parabank.utils.DriverManager;
import com.parabank.utils.ExcelReader;
import com.parabank.utils.TestDataGenerator;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.List;
import java.util.Map;

public class ParaBankRegistrationTest {
    
    private WebDriver driver;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private AccountOverviewPage accountOverviewPage;
    private ExcelReader excelReader;
    
    @BeforeSuite
    public void setupSuite() {
        System.out.println("========================================");
        System.out.println("PARABANK AUTOMATION TEST");
        System.out.println("========================================");
        excelReader = new ExcelReader(TestConfig.EXCEL_FILE_PATH, TestConfig.SHEET_NAME);
    }
    
    @BeforeMethod
    public void setup() {
        System.out.println("\n--- Initializing Test ---");
        driver = DriverManager.initializeDriver(TestConfig.BROWSER);
        registrationPage = new RegistrationPage(driver);
        loginPage = new LoginPage(driver);
        accountOverviewPage = new AccountOverviewPage(driver);
    }
    
    @Test(dataProvider = "excelData")
    public void testRegistrationAndLogin(Map<String, String> data) {
        System.out.println("\n========================================");
        System.out.println("TEST EXECUTION STARTED");
        System.out.println("User: " + data.get("FirstName") + " " + data.get("LastName"));
        System.out.println("========================================");
        
        String username = TestDataGenerator.generateUniqueUsername(data.get("FirstName"));
        
        System.out.println("\nSTEP 1: Navigate to Registration Page");
        registrationPage.navigateToRegistrationPage(TestConfig.REGISTRATION_URL);
        
        System.out.println("\nSTEP 2: Fill Registration Form");
        registrationPage.fillRegistrationForm(
            data.get("FirstName"),
            data.get("LastName"),
            data.get("Address"),
            data.get("City"),
            data.get("State"),
            data.get("ZipCode"),
            data.get("Phone"),
            data.get("SSN"),
            username,
            data.get("Password")
        );
        
        System.out.println("\nSTEP 3: Submit Registration");
        registrationPage.submitRegistration();
        
        System.out.println("\nSTEP 4: Verify Registration");
        Assert.assertTrue(registrationPage.isRegistrationSuccessful());
        
        System.out.println("\nSTEP 5: Logout");
        registrationPage.logout();
        
        System.out.println("\nSTEP 6: Login");
        loginPage.navigateToLoginPage(TestConfig.LOGIN_URL);
        loginPage.login(username, data.get("Password"));
        
        System.out.println("\nSTEP 7: Verify Login");
        Assert.assertTrue(accountOverviewPage.isLoginSuccessful());
        
        System.out.println("\n========================================");
        System.out.println("TEST PASSED: " + username);
        System.out.println("========================================");
    }
    
    @DataProvider(name = "excelData")
    public Object[][] excelData() {
        List<Map<String, String>> dataList = excelReader.getAllRowsAsMaps();
        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }
    
    @AfterMethod
    public void teardown() {
        System.out.println("\n--- Cleaning Up ---");
        DriverManager.quitDriver();
    }
    
    @AfterSuite
    public void teardownSuite() {
        excelReader.close();
        System.out.println("\n========================================");
        System.out.println("ALL TESTS COMPLETED");
        System.out.println("========================================");
    }
}