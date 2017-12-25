package com.fengdai.qa.test;

import java.util.ArrayList;
import java.util.List;

import com.fengdai.qa.util.FileUtil;

import io.restassured.path.json.JsonPath;

public class TestDTScheck {

	public static List<String> checkDtslist = new ArrayList();
	static {
		checkDtslist.add("uniqueId");
		checkDtslist.add("customerNature");
		checkDtslist.add("name");
		checkDtslist.add("certNo");
		checkDtslist.add("phoneNumber");
		checkDtslist.add("phoneNumberBank");
		checkDtslist.add("bankCardNo");
		checkDtslist.add("yearIncome");
		checkDtslist.add("industry");
		checkDtslist.add("principal");
		checkDtslist.add("yearRate");
		checkDtslist.add("terms");
		checkDtslist.add("perTermUnit");
		checkDtslist.add("perTermNum");
		checkDtslist.add("contractID");
		checkDtslist.add("type");
		checkDtslist.add("files");
		checkDtslist.add("prodName");
		checkDtslist.add("loanProdType");
		checkDtslist.add("loanFees");
		checkDtslist.add("threeMonthLoanCount");
		checkDtslist.add("sixMonthExpireCount");
		checkDtslist.add("sixMonthExpireAmount");
		checkDtslist.add("fundsUseInfo");
		checkDtslist.add("financialInfo");
		checkDtslist.add("businessInfo");
		checkDtslist.add("lawsuitInfo");
		checkDtslist.add("punishmentInfo");
		checkDtslist.add("gpsStatus");
		checkDtslist.add("paymentSource");
		checkDtslist.add("loanAssetType");
		checkDtslist.add("checkInfo");
	}



	public static void main(String[] args) {
		List<String> okKeys = new ArrayList<>();
		List<String> failKeys = new ArrayList<>();
		String temp = FileUtil.readFileByLines("src/main/resources/testDF.json");
		for(String key:checkDtslist) {
			Object var1=JsonPath.from(temp).get(key);
			if(var1 != null) {
				okKeys.add(key);
//				System.out.println(key);
			}else {
				failKeys.add(key);
//				System.err.println(key);;
			}
		}
		okKeys.forEach(var -> System.out.println(var));
		failKeys.forEach(var -> System.err.println(var));
	}

}
