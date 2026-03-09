package com.parabank.config;

public class TestConfig {
    
    public static final String BASE_URL = "https://parabank.parasoft.com/parabank";
    public static final String REGISTRATION_URL = BASE_URL + "/register.htm";
    public static final String LOGIN_URL = BASE_URL + "/index.htm";
    
    public static final int IMPLICIT_WAIT = 10;
    public static final int EXPLICIT_WAIT = 15;
    public static final int PAGE_LOAD_TIMEOUT = 30;
    
    public static final String BROWSER = "chrome";
    public static final boolean HEADLESS = false;
    
    public static final String EXCEL_FILE_PATH = "test-data/testdata.xlsx";
    public static final String SHEET_NAME = "RegistrationData";
    
    private TestConfig() {
        throw new IllegalStateException("Config class");
    }
}