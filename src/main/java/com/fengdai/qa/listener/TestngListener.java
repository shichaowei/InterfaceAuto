package com.fengdai.qa.listener;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.fengdai.qa.meta.StepDetail;
import com.fengdai.qa.util.FileUtil;


public class TestngListener extends TestListenerAdapter {
	private static Logger logger = LoggerFactory.getLogger(TestngListener.class);
	private static HashSet<String> failresult = new HashSet();
	
	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		logger.info(tr.getName() + " Failure");
		saveFailRequest(tr);
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		logger.info(tr.getName() + " Skipped");
                saveFailRequest(tr);
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		logger.info(tr.getName() + " Success");
	}

	@Override
	public void onTestStart(ITestResult tr) {
		super.onTestStart(tr);
		logger.info(tr.getName() + " Start");
	}

	@Override
	public void onFinish(ITestContext testContext){
		super.onFinish(testContext);
		StringBuffer failresultbuffer = new StringBuffer();
		for(String temp:failresult){
			failresultbuffer.append(temp+"\n");
		}
		try {
			FileUtil.clearWriteFile(failresultbuffer.toString(), ResourceUtils.getFile("classpath:failresult.txt").getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		failresult.clear();
	}


	/**
	 * 保存失败的用例请求
	 * 
	 * @param tr
	 */
	private void saveFailRequest(ITestResult tr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String mDateTime = formatter.format(new Date());
		String fileName = mDateTime + "_" + tr.getName();
		Reporter.setCurrentTestResult(tr);
		StepDetail temp = (StepDetail)tr.getParameters()[0];
		failresult.add(temp.toString());
		Reporter.log(temp.toString());

	}
}
