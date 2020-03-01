import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.internal.Util;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Don Arturooo, aka Joker
 */
public class Main {

    /**
     * @param args the command line arguments
     * @args[0] dzień
     * @args[1] miesiąc
     * @args[2] mail
     * @args[3] hasło
     * @args[4] dzień rezerwacji
     * @args[5] godzina rezerwacji
     */
    public static DateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    
    public static Date data;
    
    public static void main(String[] args) throws InterruptedException, IOException {
        
        data = new Date(120, Integer.parseInt(args[1]) - 1, Integer.parseInt(args[0]), 00, 00);
    
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        
        ChromeOptions opcje = new ChromeOptions();
        opcje.addArguments("--start-maximized");
        
        WebDriver driver = new ChromeDriver(opcje);
        driver.manage().getCookies();
        WebDriverWait wait = new WebDriverWait(driver, 5000);
        
        WebDriver driver1 = new ChromeDriver(opcje);
        driver.manage().getCookies();
        WebDriverWait wait1 = new WebDriverWait(driver1, 5000);
        driver1.manage().window().setPosition(new Point(2000, 0));
        driver1.manage().window().maximize();
        
        
        logowanie(driver, wait, 2, args);
        logowanie(driver1, wait1, 6, args);
        

        rezerwacja(driver, wait, 4, args);
        rezerwacja(driver1, wait1, 8, args);
        
        potwierdzenieRezerwacji(driver, wait, 2, args);
        potwierdzenieRezerwacji(driver1, wait1, 6, args);
    }

    static public boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by).click();
            return true;
        }
            catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
    
    static public void logowanie(WebDriver driver, WebDriverWait wait,  int index, String[] args) throws InterruptedException{
        
        driver.get("https://panelklienta.osirursus.pl/login");
        //logowanie do panelu klienta
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'username\']"))).sendKeys(args[index++]);
        driver.findElement(By.xpath("//*[@id=\'password\']")).sendKeys(args[index++]);
        driver.findElement(By.xpath("//*[@id=\'main\']/div/div/div/div[2]/form/input")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/header/nav/div/div[2]/ul[1]/li[3]"))).click();
    }
    
    static public void rezerwacja(WebDriver driver, WebDriverWait wait,  int index, String[] args) throws InterruptedException{
        
        if(index < 5){
            while(!formatDate.format(data).equals(formatDate.format(new Date())))
                Thread.sleep(1000);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'main\']/div/div/div[2]/div/div/a"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'main\']/div/div/div[2]/div/label[1]"))).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'main\']/div/mwl-calendar/div/mwl-calendar-week/div/div[2]/div[" + args[index++] + "]/a"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[5]/div/div/div/div[2]/div[2]/div[1]/div/div[2]/div/table[1]/tbody/tr[2]/td[1]/input")));
        System.out.println(driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div[2]/div[2]/div[1]/div/div[2]/div/table[1]/tbody/tr[2]/td[1]/input")).getAttribute("value"));
        
        JavascriptExecutor  jse = (JavascriptExecutor)driver;
        String godzina = (String) jse.executeScript("return arguments[0].value;", driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div[2]/div[2]/div[1]/div/div[2]/div/table[1]/tbody/tr[2]/td[1]/input")));
        
        System.err.println(godzina);
        
        while(!godzina.equals(args[index])){
          //driver.findElement(By.xpath(xpathExpression)).click();
          
          godzina = (String) jse.executeScript("return arguments[0].value;", driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div[2]/div[2]/div[1]/div/div[2]/div/table[1]/tbody/tr[2]/td[1]/input")));
        }
        //driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div[2]/div[2]/div[1]/div/div[2]/div/table[1]/tbody/tr[2]/td[1]/input")).sendKeys(args[index++]);

        driver.findElement(By.xpath("//*[@id=\'regulations\']")).click();

        driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div[3]/input")).click();
        Thread.sleep(200);

        if(isElementPresent(driver, By.xpath("/html/body/div[5]/div/div/div[3]/button")));
        if(isElementPresent(driver, By.xpath("/html/body/div[4]/div/div/div[3]/button")));
    }
    
    static public void potwierdzenieRezerwacji(WebDriver driver, WebDriverWait wait,  int index, String[] args) throws IOException, InterruptedException{
        driver.findElement(By.xpath("/html/body/header/nav/div/div[2]/ul[1]/li[4]")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'main\']/div/div/div/div[2]/table")));
        
        takesScreenshot(driver, index, args);
        
        Thread.sleep(1000);
        
        if(isElementPresent(driver, By.xpath("/html/body/header/nav/div/div[2]/ul[2]/li[4]")));
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/header/nav/div/div[2]/ul[2]/li[4]/ul/li[4]/a"))).click();
    }
    
    public static void takesScreenshot(WebDriver driver, int index, String[] args) throws IOException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String nazwa = args[index] + " " + format.format(new Date()) + ".png";
        FileUtils.copyFile(scrFile, new File("C:/Users/Don Arturooo/Desktop/Rezerwacje/" + nazwa));
    }
}

