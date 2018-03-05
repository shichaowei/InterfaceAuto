package com.fengdai.qa.test;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class TestJDBC {

	public static boolean checkName(String cellphone) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://10.200.141.26:3306/fengdai_user?useUnicode=true&characterEncoding=utf-8");
		dataSource.setUsername("fdtest");
		dataSource.setPassword("Mysqltest@123098");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql="SELECT count(*) FROM fengdai_user.sys_user WHERE cellphone='" + cellphone + "';";
		System.out.println(sql);
		jdbcTemplate.queryForMap(sql);
		System.out.println(jdbcTemplate
				.queryForMap(sql));
		Long var = (Long) jdbcTemplate
				.queryForMap("SELECT count(*) FROM fengdai_user.sys_user WHERE cellphone='" + cellphone + "'")
				.get("count(1)");
		if (var != 0) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {

		checkName("18667911000");
	}

}
