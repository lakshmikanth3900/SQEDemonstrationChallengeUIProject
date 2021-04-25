package com.sample.test.demo.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.sample.test.demo.TestBase;
import com.sample.test.demo.constants.PizzaToppings;
import com.sample.test.demo.constants.PizzaTypes;
import com.sample.test.demo.pageScript.PlaceOrder;

public class DemoTest extends TestBase {

	@DataProvider(name = "getPizzaType")
	public Object[][] returnPizzaTypeData(){
		return new Object[][] {
			{PizzaTypes.SMALL_NOTOPPINGS.getDisplayName(),PizzaTypes.SMALL_NOTOPPINGS.getCost(),"","1","Credit Card"},
			{PizzaTypes.SMALL_ONETOPPINGS.getDisplayName(),PizzaTypes.SMALL_ONETOPPINGS.getCost(),"MANGOS","2","Cash"},
			{PizzaTypes.MEDIUM_TWOTOPPINGS.getDisplayName(),PizzaTypes.MEDIUM_TWOTOPPINGS.getCost(),"MANGOS,OLIVES","3","Credit Card"},
			{PizzaTypes.LARE_NOTOPPINGS.getDisplayName(),PizzaTypes.LARE_NOTOPPINGS.getCost(),"","4","Cash"},
			{PizzaTypes.LARGE_THREETOPPINGS.getDisplayName(),PizzaTypes.LARGE_THREETOPPINGS.getCost(),"OLIVES,MUSHROOMS","5","Credit Card"}
		};
	}
	
//    @Test
//    public void demoTest() {
//        System.out.println("HELLO WORLD");
//    }
//    
    @Test(dataProvider="getPizzaType")
    public void testOrderCreation(String pizzaName,double pizzaCost,String toppings,String quantity,String pymtType) {
    	driver.manage().window().maximize();
    	driver.navigate().refresh();
    	PlaceOrder placeOrderObj = new PlaceOrder(driver);
    	placeOrderObj.createOrders(pizzaName,pizzaCost,toppings,quantity,pymtType);
    }
    
    @Test
    public void testMandatoryFieldErrorMessage() throws InterruptedException {
    	driver.manage().window().maximize();
    	PlaceOrder placeOrderObj = new PlaceOrder(driver);
    	placeOrderObj.placeOrderWithOutEnteringDetails();
    }

}
