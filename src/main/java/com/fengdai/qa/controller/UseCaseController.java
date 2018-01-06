package com.fengdai.qa.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fengdai.qa.har.CaseGeneUtil;

@Controller
public class UseCaseController {

	private static final Logger logger = LoggerFactory.getLogger(UseCaseController.class);

	/*
	 * 获取file.html页面
	 */
	@RequestMapping({ "/getusecase" })
	public String file(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		return "usecase";
	}

	/**
	 * 实现文件上传
	 */
	@RequestMapping("/api/caseupload")
	public void fileUpload(@RequestParam("fileName") MultipartFile file, HttpServletRequest request,
			HttpServletResponse res) {
		if (file.isEmpty()) {
			throw new RuntimeException("开始file处理");
		}
		String fileName = file.getOriginalFilename();
		String casefilename = "casefile.yaml";
		int size = (int) file.getSize();
		System.out.println(fileName + "-->" + size);

		String path = request.getSession().getServletContext().getRealPath("/upload/");
		System.out.println(path);
		File harFile = new File(path + "/" + fileName);
		File caseFile = new File(path + "/" + casefilename);
		System.out.println("casefile:" + caseFile.getAbsolutePath());
		if (!harFile.getParentFile().exists()) { // 判断文件父目录是否存在
			harFile.getParentFile().mkdir();
		}
		try {
			file.transferTo(harFile); // 保存文件
			CaseGeneUtil.generateYaml(harFile, caseFile);
			// 构造返回值
			res.setHeader("content-type", "application/octet-stream");
			res.setContentType("application/octet-stream");
			res.setHeader("Content-Disposition", "attachment;filename=" + casefilename);
			byte[] buff = new byte[1024];
			BufferedInputStream bis = null;
			OutputStream os = null;
			try {
				os = res.getOutputStream();
				bis = new BufferedInputStream(new FileInputStream(caseFile));
				int i = bis.read(buff);
				while (i != -1) {
					os.write(buff, 0, buff.length);
					os.flush();
					i = bis.read(buff);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("success");

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	@RequestMapping(value = "/api/casedownload", method = RequestMethod.GET)
	public void testDownload(HttpServletRequest request, HttpServletResponse res, ModelMap map) {
		String fileName = "logs.zip";
		res.setHeader("content-type", "application/octet-stream");
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = res.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("success");
	}
	*/

}
