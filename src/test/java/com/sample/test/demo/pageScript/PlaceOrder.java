package com.sample.test.demo.pageScript;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.sample.test.demo.constants.PizzaToppings;
import com.sample.test.demo.utils.CommonFunc;

public class PlaceOrder {
	private WebDriver _driver;
	WebDriverWait wait;
	CommonFunc cFunc;

	public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	public PlaceOrder(WebDriver _driver) {
		this._driver = _driver;
		wait = new WebDriverWait(_driver, 30);
		cFunc = new CommonFunc(_driver);
		PageFactory.initElements(this._driver, this);
	}
	
	@FindBy(id = "pizza1Pizza")
	WebElement pizzaTypeLoc;
	
	@FindBy(xpath = "//div[@id='pizza1']//select[@class='toppings1']")
	WebElement toppingOneLoc;
	
	@FindBy(xpath = "//div[@id='pizza1']//select[@class='toppings2']")
	WebElement toppingTwoLoc;
	
	@FindBy(id = "pizza1Qty")
	WebElement pizzaQtyLoc;
	
	@FindBy(id = "pizza1Cost")
	WebElement pizzaCostLoc;
	
	@FindBy(id = "email")
	WebElement emailLoc;
	
	@FindBy(id = "name")
	WebElement nameLoc;
	
	@FindBy(id = "phone")
	WebElement phoneLoc;
	
	@FindBy(id = "ccpayment")
	WebElement creditCardLoc;
	
	@FindBy(id = "cashpayment")
	WebElement cashLoc;
	
	@FindBy(id = "placeOrder")
	WebElement placeOrderLoc;
	
	@FindBy(id = "reset")
	WebElement resetLoc;
	
	@FindBy(id = "dialog")
	WebElement orderConfirmationDialogBoxLoc;
	
	@FindBy(xpath = "//div[@id='dialog']/p")
	WebElement orderConfirmationDialogTextLoc;
	
	
	public void createOrders(String pizzaName,double pizzaExpectedCost,String pizzaTopping,String quantity,String pymtType) {
		//select Pizza type
		cFunc.selectValueFromDropDown(pizzaTypeLoc, pizzaName);
		if(!(pizzaTopping.isEmpty())) {
			if(pizzaTopping.split(",").length == 1) {
				cFunc.selectValueFromDropDown(toppingOneLoc, PizzaToppings.valueOf(pizzaTopping).getDisplayName());
			}
			else if(pizzaTopping.split(",").length == 2) {
				String[] toppings = pizzaTopping.split(",");
				cFunc.selectValueFromDropDown(toppingOneLoc, PizzaToppings.valueOf(toppings[0]).getDisplayName());
				cFunc.selectValueFromDropDown(toppingTwoLoc, PizzaToppings.valueOf(toppings[1]).getDisplayName());
			}
		}
		wait.until(ExpectedConditions.visibilityOf(pizzaQtyLoc)).sendKeys(quantity);
		double expectedCost = Integer.valueOf(quantity) * pizzaExpectedCost;
		
		//enter personal details
		wait.until(ExpectedConditions.visibilityOf(nameLoc)).sendKeys("TestUser");
		wait.until(ExpectedConditions.visibilityOf(phoneLoc)).sendKeys("1234566789");
		wait.until(ExpectedConditions.visibilityOf(emailLoc)).sendKeys("test@gmail.com");

		//select payment type
		if(pymtType.contains("Credit Card"))
			creditCardLoc.click();
		else
			cashLoc.click();
		
		wait.until(ExpectedConditions.visibilityOf(placeOrderLoc)).click();
		//validate confirmation message pop up
		Assert.assertEquals(validateOrderConfirmationPopUp(pizzaName, expectedCost), true,"Failed while validating the order confirmation message");
	}
	
	public boolean validateOrderConfirmationPopUp(String pizzaName,double expectedCost) {
		boolean flag = true;
		if(wait.until(ExpectedConditions.visibilityOf(orderConfirmationDialogBoxLoc)).isDisplayed()) {
			String actualText = wait.until(ExpectedConditions.visibilityOf(orderConfirmationDialogTextLoc)).getText();
			logger.info("Order confirmation dialog box is displayed");
			logger.info("Actual cofirmation message: "+ actualText);
			if(actualText.contains("Thank you for your order!")) {
				logger.info("Dialog box text has text >>>>>" + "Thank you for your order!");
				if(actualText.contains(pizzaName)) {
					logger.info("Successfully validated ordered pizza name in dialog box>>>>>"+ pizzaName);
					if(actualText.contains("TOTAL: "+String.valueOf((int) expectedCost)+"")) {
						logger.info("Successfully validated cost of order in dialog box>>>>>"+ expectedCost);
					}
					else {
						flag = false;
						logger.info("Failed while validating cost of order in dialog box>>>>>"+ expectedCost);
					}
				}
				else {
					flag = false;
					logger.info("Failed while validating ordered pizza name in dialog box>>>>>"+ pizzaName);
				}
			}
			else {
				flag = false;
				logger.info("Failed while validating dialog box text. Does not have text >>>>>" + "Thank you for your order!");
			}
		}
		else {
			flag = false;
			logger.info("Order confirmation dialog box is not displayed");
		}
		
		return flag;
	}
	
	public void placeOrderWithOutEnteringDetails() throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOf(placeOrderLoc)).click();
		String actualText = wait.until(ExpectedConditions.visibilityOf(orderConfirmationDialogTextLoc)).getText();
		Assert.assertEquals(actualText.contains("Missing name"), true,"Missing name text not displayed in error dialog box");
		Assert.assertEquals(actualText.contains("Missing phone number"), true,"Missing phone number text not displayed in error dialog box");
		Thread.sleep(3000);
		logger.info("Successfully validated error message");
	}
}
