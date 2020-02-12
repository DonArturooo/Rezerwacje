import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Don Arturooo, aka Joker
 */
public class Main{

    /**
     * @param args the command line arguments
     * @args[0] dzień
     * @args[1] miesiąc
     * @args[2] mail
     * @args[3] hasło
     * @args[4] dzień rezerwacji
     * @args[5] godzina rezerwacji
     */
    public static void main(String[] args) throws InterruptedException {
        
        DateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        
        ChromeOptions opcje = new ChromeOptions();
        opcje.addArguments("--start-maximized");
        
        Date data = new Date(120, Integer.parseInt(args[1]) - 1, Integer.parseInt(args[0]), 00, 00);
        
        WebDriver driver = new ChromeDriver(opcje);
        driver.manage().getCookies();
        WebDriverWait wait = new WebDriverWait(driver, 5000);
        
        driver.get("https://panelklienta.osirursus.pl/login");
        
        while(!formatDate.format(data).equals(formatDate.format(new Date()))){
            Thread.sleep(1000);
//            System.out.println(formatDate.format(data));
//            System.out.println(formatDate.format(new Date()));
        }
        
        for(int i = 2; i<args.length;){
            //logowanie do panelu klienta
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'username\']")));
            driver.findElement(By.xpath("//*[@id=\'username\']")).sendKeys(args[i++]);
            Thread.sleep(200);
            driver.findElement(By.xpath("//*[@id=\'password\']")).sendKeys(args[i++]);
            Thread.sleep(200);
            driver.findElement(By.xpath("//*[@id=\'main\']/div/div/div/div[2]/form/input")).click();




            //Rezerwacja
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/header/nav/div/div[2]/ul[1]/li[3]")));
            driver.findElement(By.xpath("/html/body/header/nav/div/div[2]/ul[1]/li[3]")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'main\']/div/div/div[2]/div/div/a")));
            driver.findElement(By.xpath("//*[@id=\'main\']/div/div/div[2]/div/div/a")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'main\']/div/div/div[2]/div/label[1]")));
            driver.findElement(By.xpath("//*[@id=\'main\']/div/div/div[2]/div/label[1]")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\'main\']/div/mwl-calendar/div/mwl-calendar-week/div/div[2]/div[" + args[i] + "]/a")));
            driver.findElement(By.xpath("//*[@id=\'main\']/div/mwl-calendar/div/mwl-calendar-week/div/div[2]/div[" + args[i++] + "]/a")).click();
            //*[@id="main"]/div/mwl-calendar/div/mwl-calendar-week/div/div[2]/div[2]/a
            //*[@id="main"]/div/mwl-calendar/div/mwl-calendar-week/div/div[2]/div[2]/a

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[5]/div/div/div/div[2]/div[2]/div[1]/div/div[2]/div/table[1]/tbody/tr[2]/td[1]/input")));
            driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div[2]/div[2]/div[1]/div/div[2]/div/table[1]/tbody/tr[2]/td[1]/input")).sendKeys(args[i++]);

            driver.findElement(By.xpath("//*[@id=\'regulations\']")).click();

            driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div[3]/input")).click();
            Thread.sleep(200);
            
            if(isElementPresent(driver, By.xpath("/html/body/div[5]/div/div/div[3]/button")));
            //if(isElementPresent(driver, By.xpath("/html/body/div[5]/div/div/div[3]/button")));
            
            driver.findElement(By.xpath("/html/body/header/nav/div/div[2]/ul[2]/li[4]/a")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/header/nav/div/div[2]/ul[2]/li[4]/ul/li[4]/a"))).click();
        }
        
        driver.close();
        
        
        //*[@id="main"]/div/mwl-calendar/div/mwl-calendar-week/div/div[2]/div[1]/a
        //*[@id="main"]/div/mwl-calendar/div/mwl-calendar-week/div/div[2]/div[2]/a
        //driver.close();   
    }

    static public boolean isElementPresent(WebDriver driver,By by) {
        try {
            driver.findElement(by).click();
            return true;
        }
            catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
}

