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

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class TestSuite {
	public static void doSmokeTest(WebDriver driver) {
		SpagoBI spagobi = new SpagoBI(driver);
		spagobi.login("http://localhost:8181/SpagoBI", "biadmin", "biadmin");		
		spagobi.openDocumentBrowser();
		spagobi.goToFolder(new String[]{"TESTS", "Server", "Funzionalità utente", "Pannello di esecuzione", "Pannello dei parametri"});
		spagobi.execDocument("Slider Simple");	

		// voglio lavorare sull'iframe che contiene il doc eseguito
		WebElement iframe = driver.findElement(By.xpath("//iframe"));
		driver.switchTo().frame(iframe);

		// trovo gli input field di tipo slider che ci sono nel parameters panel
		List<WebElement> sliders = driver.findElements(By.xpath(".//div[@class='x-slider x-slider-horz']"));
		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
		
		for(int i = 0; i < sliders.size(); i++) {
			WebElement slider = sliders.get(i);
			Point point = slider.getLocation();
			System.out.println(point.x + " - " + point.y);
			
			Actions builder = new Actions(driver);
			Action dragAndDrop = builder
				.clickAndHold(slider)
				.moveToElement(slider, 200, 0)      
				.release(slider)       
				.build();   
			dragAndDrop.perform();
			driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
		}
	}
}
