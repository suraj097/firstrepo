package com.auto.framework.tools;

/**
 * Tool to be extended by every Test Tool.
 * 
 * @author naini.ghai
 *
 */
public class Tool
{
	private boolean bInstanceClassLevel = false;

	public void setInstanceClassLevel()
	{
		bInstanceClassLevel = true;
	}

	public boolean isInstanceClassLevel()
	{
		return bInstanceClassLevel;
	}
}
