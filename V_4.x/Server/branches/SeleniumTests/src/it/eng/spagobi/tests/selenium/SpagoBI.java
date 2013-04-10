/*
 * SpagoBI, the Open Source Business Intelligence suite
 * © 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */
package it.eng.spagobi.tests.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBI {
	WebDriver driver;
	
	public SpagoBI(WebDriver driver) {
		this.driver = driver;
	}
	
	public void login(String url, String usr, String pwd) {
		driver.get( url );
		WebElement username = driver.findElement(By.id("userID"));
		username.sendKeys(usr);
		
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys(pwd);
		
		WebElement submit = driver.findElement(By.xpath(".//*[@id='content']/div[1]/table/tbody/tr[1]/td[3]/input"));
		submit.click();
		
		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
	}
	
	public void openDocumentBrowser() {
		WebElement iframe = driver.findElement(By.xpath("//iframe"));
		driver.switchTo().frame(iframe);
	}
	
	public void goToFolder(String[] path) {
		for(int i = 0; i < path.length; i++) {
			WebElement folder;
			folder = driver.findElement(By.xpath(".//div[@class='item-desc']/p[@id='name']/span[@class='field-value' and contains(.,'" +  path[i] + "')]"));
			folder.click();
		}
	}
	
	public void execDocument(String document) {
		WebElement doc;
		doc = driver.findElement(By.xpath(".//div[@class='item-desc']/p[@id='name']/span[@class='field-value' and contains(.,'" + document + "')]"));
		doc.click();
	}
}
