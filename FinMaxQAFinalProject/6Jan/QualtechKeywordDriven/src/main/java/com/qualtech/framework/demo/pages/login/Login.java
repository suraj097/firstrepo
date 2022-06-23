package com.qualtech.framework.demo.pages.login;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.qualtech.framework.demo.genricmethod.login.BaseMethod;

public class Login  {

	/*@FindBy(name="")
	private WebElement userName_ED; 
	@FindBy(name="")
	private WebElement password_ED; 
	@FindBy(name="")
	private WebElement Login_BT; 
	@FindBy(name="")
	private WebElement firstName,_ED; 
	@FindBy(name="")
	private WebElement lastname_ED; 
	@FindBy(name="")
	private WebElement createemaiId_ED; 
	@FindBy(name="")
	private WebElement createpassword_ED; */
	
public static BaseMethod genricMethodobj;
	public void validLogin() {
		String browsername = null;
		genricMethodobj.LaunchBrowse(browsername);
		
		
	}
    public void inValidLogin() {
		
	}
}
