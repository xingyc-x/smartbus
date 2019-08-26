package com.pcis.smartbus.project.service;

import org.springframework.stereotype.Service;

/**
* @author qishan
* @version 创建时间：2019年8月19日 上午9:07:23
*/

@Service
public class ProjectService {

	public static String[] exts = new String[] {"bmp","png","jpg","jpeg"};
	//根据文件扩展名判断上传的文件是否为图片
	public  boolean isImage(String orignalFileName)
	{
		int dotIndex = orignalFileName.lastIndexOf(".");
		if(dotIndex<0)
			return false;
		String fileExt = orignalFileName.substring(dotIndex+1).toLowerCase();
		for(String ext : exts)
		{
			if(ext.equals(fileExt))
				return true;
		}
		return false;
		
	}
}
