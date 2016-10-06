package es.udc.pa.pa002.practicapa.test.webtest;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AuthenticationTestSelenium {
	private WebDriver driver;
    
    @Before
    public void setUp() {
    	driver = new FirefoxDriver();
    }
     
    @After
    public void tearDown() {
        driver.close();
    }
    
    @Test
    public void loginToBetFic(){
    	 driver.get("http://localhost:9090/bet-fic");

    	 WebElement element = driver.findElement(By.id("autenticar"));

         element.click();
         
         element = driver.findElement(By.id("loginName"));
         element.sendKeys("user1");
         
         element = driver.findElement(By.id("password"));
         element.sendKeys("pojo");
         
         element = driver.findElement(By.className("btn-primary"));
         element.submit();
         

         assertEquals("User", driver.findElement(By.linkText("User")).getText());
    	
    }
    
    @Test
    public void apostar(){
    	driver.get("http://localhost:9090/bet-fic/");
    	WebElement element = driver.findElement(By.id("find"));
    	element.click();
    	
    	element = driver.findElement(By.id("findeventos"));
    	element.click();
    	
    	element = driver.findElement(By.id("keywords"));
    	element.sendKeys("maDRi");
    	
    	element = driver.findElement(By.id("submit"));
    	element.submit();
    	
    	element = driver.findElement(By.linkText("Madrid-Barcelona"));
    	element.click();
    	
    	element = driver.findElement(By.linkText("Goles marcados"));
    	element.click();
    	
    	element = driver.findElement(By.linkText("10"));
    	element.click();
    	
    	element = driver.findElement(By.id("importe"));
    	element.sendKeys("10");
    	
    	element = driver.findElement(By.id("submit"));
    	element.submit();
    	
    	element = driver.findElement(By.id("loginName"));
		element.sendKeys("user1");

		element = driver.findElement(By.id("password"));
		element.sendKeys("pojo");

		element = driver.findElement(By.id("submit"));
		element.submit();
		
		element = driver.findElement(By.id("importe"));
    	element.sendKeys("10");
    	
    
    	element = driver.findElement(By.id("submit"));
    	element.submit();

    	Calendar fecha=Calendar.getInstance();
    	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyy HH:mm");
    	String fechaApuesta=sdf.format(fecha.getTime());

    	
    	element = driver.findElement(By.id("find"));
    	element.click();
    	
    	element = driver.findElement(By.id("findapuestas"));
    	element.click();
    	
    	
    	element = driver.findElement(By.className("table"));
    	
    	String[] apuesta=element.getText().substring(92, 178).split(" ");
    	
    	

    	assertEquals("Madrid-Barcelona",apuesta[1]);
    	assertEquals("Futbol",apuesta[2]);
    	assertEquals("Goles marcados",apuesta[3]+" "+apuesta[4]);
    	assertEquals("10",apuesta[5]);
    	assertEquals(fechaApuesta,apuesta[6]+" "+apuesta[7]);
    	assertEquals("10.0",apuesta[8]);
    	assertEquals("1000.0",apuesta[9]);
    	assertEquals("Pendiente",apuesta[10]);
    	
    }
    
}
