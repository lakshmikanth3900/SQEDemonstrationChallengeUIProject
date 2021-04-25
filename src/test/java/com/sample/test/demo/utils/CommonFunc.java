package com.sample.test.demo.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonFunc {
	private WebDriver _driver;
	WebDriverWait wait;
	
	public CommonFunc(WebDriver _driver) {
		this._driver = _driver;
		wait = new WebDriverWait(_driver, 30);
	}
	
	public void selectValueFromDropDown(WebElement dropDown,String valueToSelect) {
		wait.until(ExpectedConditions.visibilityOf(dropDown));
		Select select = new Select(dropDown);
		select.selectByValue(valueToSelect);
	}
}
