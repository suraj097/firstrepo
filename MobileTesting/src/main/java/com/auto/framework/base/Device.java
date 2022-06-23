package com.auto.framework.base;

public class Device
{
	// Application Name - SILVER, SPRE, SPRO
	public String	APPLICATION;
	// Mobile OS - ANDROID or IOS
	public String	MOB_OS;
	// In Use - Y or N
	public String	MOB_INUSE;
	public String	MOB_APPIUM_HOST;
	public String	MOB_APPIUM_PORT;
	// IS Real Device - Y or N
	public String	MOB_IS_REAL_DEVICE;
	// For android
	public String	MOB_AND_PLATFORM;
	public String	MOB_AND_DEVICE;
	// Android Emulator fields
	public String	MOB_AND_EMU_PLATFORM_VERSION;
	public String	MOB_AND_EMU_DEVICE_NAME;
	public String	MOB_AND_EMU_DEVICE_ID;
	public String	MOB_AND_EMU_AVD;
	// Android Real-Device fields
	public String	MOB_AND_REAL_PLATFORM_VERSION;
	public String	MOB_AND_REAL_DEVICE_NAME;
	public String	MOB_AND_REAL_DEVICE_ID;
	// Android Native App
	public String	MOB_AND_APP;
	public String	MOB_AND_APP_PACKAGE;
	public String	MOB_AND_APP_ACTIVITY;
	// For IOS
	public String	MOB_IOS_PLATFORM;
	public String	MOB_IOS_DEVICE;
	// IOS Device Type - iPad or iPhone
	public String	MOB_IOS_DEVICE_TYPE;
	// iOS Simulator fields
	public String	MOB_IOS_SIM_DEVICE_NAME;
	public String	MOB_IOS_SIM_PLATFORM_VERSION;
	public String	MOB_IOS_SIM_DEVICE_ID;
	// iOS Real-Device fields
	public String	MOB_IOS_REAL_DEVICE_NAME;
	public String	MOB_IOS_REAL_PLATFORM_VERSION;
	public String	MOB_IOS_REAL_DEVICE_ID;
	public String	MOB_IOS_REAL_BUNDLE_ID;
	// optional (may be not required)
	public String	MOB_IOS_REAL_XCODE_ORG_ID;
	public String	MOB_IOS_REAL_XCODE_SIGN_ID;
	public String	MOB_IOS_REAL_WDA_BUNDLE_ID;
	// IOS Native App
	public String	MOB_IOS_APP;
	// EXTRA
	public String	STATION;
}
