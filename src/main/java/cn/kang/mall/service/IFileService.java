package cn.kang.mall.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	
	/**
	 * 上传  图片 
	 * @param file
	 * @param path
	 * @return
	 */
	String upload(MultipartFile file, String path);

}
