package org.ebike.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.ebike.bean.Application;
import org.ebike.dao.HibernateBaseDao;
import org.ebike.tools.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @ClassName: BaseService 
 * @Description: 业务类基类
 * @author hzjintianfan
 * @date 2014-7-23 下午6:54:07
 */
@Service
@Transactional
public class BaseService
{
	@Autowired
	private Application application;

	@Autowired
	protected HibernateBaseDao baseDao;

	/**
	 * 把文件拷入指定目录
	 * @param in 文件流
	 * @orignFilename 原文件名 aa.png
	 * @param destPath 目标目录  /storage/person
	 * @return 文件的完整路径  /storage/person/adasfffs.png
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String copyFileToDestDir(InputStream in, String orignFilename, String destPath) throws IOException
	{
		String destFile = destPath + "/" + orignFilename;
		ImageUtil.resizeFiexed(in, getBashPath() + destFile, 160, 160);
		return destFile;
	}

	/**
	 * 获取应用的根路径
	 * @return
	 */
	public String getBashPath()
	{
		return application.getBathPath();
	}
}
