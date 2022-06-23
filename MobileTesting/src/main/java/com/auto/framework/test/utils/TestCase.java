package com.auto.framework.test.utils;

import java.util.ArrayList;
import java.util.List;

public class TestCase
{
	public String release;
	public String sprint;
	public String cycle;
	public String testId;
	public String start_time;
	public String end_time;
	public String draft_run;
	public String test_staus;
	public List<TestStep> steps = new ArrayList<TestStep>();
}
