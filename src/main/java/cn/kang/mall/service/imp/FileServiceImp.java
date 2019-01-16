package cn.kang.mall.service.imp;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import cn.kang.mall.service.IFileService;
import cn.kang.mall.util.FTPUtil;
@Service
public class FileServiceImp implements IFileService {
	
	private Logger LOGGER = LoggerFactory.getLogger(FileServiceImp.class);
	@Override
	public String upload(MultipartFile file, String path) {
		String fileName = file.getOriginalFilename();
		//扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        LOGGER.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
		
        File fileDir = new File(path);
        if(!fileDir.exists()){  
            fileDir.setWritable(true);  // 设置权限 
            fileDir.mkdirs();  // 如果不存在  就创建
        }
        File targetFile = new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //文件已经上传成功了
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到ftp服务器上

            targetFile.delete();
            
        }catch (IOException e) {
        	LOGGER.error("上传文件异常",e);
            return null;
        }
        //A:abc.jpg
        //B:abc.jpg
        return targetFile.getName();
	}
	
}
