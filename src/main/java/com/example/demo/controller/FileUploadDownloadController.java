package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by HMa on 2018/7/11
 * 文件上传.
 */
@Controller

public class FileUploadDownloadController {
	/*
     * 获取multifile.html页面
     */
	@RequestMapping("multifile")
	public String multifile(){
		return "/multifile";
	}

	/**
	 * 实现多文件上传
	 * */
	@RequestMapping(value="multifileUpload",method= RequestMethod.POST)
	public @ResponseBody String multifileUpload(HttpServletRequest request){

		List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("fileName");

		if(files.isEmpty()){
			return "false";
		}

		String path = "F:/test" ;

		for(MultipartFile file:files){
			String fileName = file.getOriginalFilename();
			int size = (int) file.getSize();
			System.out.println(fileName + "-->" + size);

			if(file.isEmpty()){
				return "false";
			}else{
				File dest = new File(path + "/" + fileName);
				if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
					dest.getParentFile().mkdir();
				}
				try {
					file.transferTo(dest);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "false";
				}
			}
		}
		return "true";
	}
	@RequestMapping("download")
	public String downLoad(HttpServletResponse response){
		String filename="2.jpg";
		String filePath = "F:/test" ;
		File file = new File(filePath + "/" + filename);
		if(file.exists()){ //判断文件父目录是否存在
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

			byte[] buffer = new byte[1024];
			FileInputStream fis = null; //文件输入流
			BufferedInputStream bis = null;

			OutputStream os = null; //输出流
			try {
				os = response.getOutputStream();
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				int i = bis.read(buffer);
				while(i != -1){
					os.write(buffer);
					i = bis.read(buffer);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("----------file download" + filename);
			try {
				bis.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 读取文件内容
	 * @return
	 */
	public Map<String,Object> readFile(){
		Map<String,Object> map=new HashMap<String,Object>();
		BufferedReader bufferedReader=null;
		try {
			bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/XXX.txt"))));
			String linetxt=null;
			while((linetxt=bufferedReader.readLine())!=null){//一行一行的读取内容
				String[] names=linetxt.split(",");
				if(names.length>0){
					map.put(names[0],names[1]);
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  map;
	}

	/**
	 * 输出数据
	 * @param map
	 */
	  public void  writerFile(Map<String,Object> map){
		  StringBuffer stringBuffer=new StringBuffer();
		  try {
			  BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:/writer.txt"))));//将内容写入到文件
			 Iterator<Map.Entry<String, Object>> iterable= map.entrySet().iterator();
			  while(iterable.hasNext()){
				 Map.Entry<String,Object> entry= iterable.next();
				  bufferedWriter.write(stringBuffer.append(entry.getKey()).append(",").append(entry.getValue()).toString());
				  bufferedWriter.newLine();
			  }
			  bufferedWriter.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	  }



}
