package cn.kang.mall.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;

import cn.kang.mall.common.Const;
import cn.kang.mall.common.ResponseCode;
import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.Product;
import cn.kang.mall.entiy.User;
import cn.kang.mall.service.IFileService;
import cn.kang.mall.service.IProductService;
import cn.kang.mall.service.IUserService;
import cn.kang.mall.util.PropertiesUtil;

/**
 * 商品管理模块
 * @author mi
 *
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
	
	@Autowired
	private IProductService productService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IFileService fileService;
	 @RequestMapping("save.do")
	 @ResponseBody
	 public ServerResponse productSave(HttpSession session, Product product){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

	     }
	     
	     if(userService.checkAdminRole(user).isSuccess()){
	    	//填充我们增加产品的业务逻辑
	        return productService.saveOrUpdateProduct(product);
	     }else{
	         return ServerResponse.createByErrorMessage("无权限操作");
	     }
	 }
	 
	 /**
	  * 修改商品 状态
	  * @param session
	  * @param productId
	  * @param status
	  * @return
	  */
	 @RequestMapping("set_sale_status.do")
	 @ResponseBody
	 public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer status){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	     }
	     if(userService.checkAdminRole(user).isSuccess()){
	            return productService.setSaleStatus(productId,status);
	        }else{
	            return ServerResponse.createByErrorMessage("无权限操作");
	        }
	 }
	 
	/**
	 * 获取商品纤细信息
	 * @param session
	 * @param productId
	 * @return
	 */
	 @RequestMapping("detail.do")
	 @ResponseBody
	 public ServerResponse getDetail(HttpSession session, Integer productId){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	     }
	     if(userService.checkAdminRole(user).isSuccess()){
	         //填充业务
	         return productService.manageProductDetail(productId);

	     }else{
	         return ServerResponse.createByErrorMessage("无权限操作");
	     } 
	 }
	 
	 /**
	  * 商品  列表
	  * @param session
	  * @param pageNum
	  * @param pageSize
	  * @return
	  */
	 @RequestMapping("list.do")
	 @ResponseBody
	 public ServerResponse getList(HttpSession session, 
	    						  @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
	    						  @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	     }
	     if(userService.checkAdminRole(user).isSuccess()){
	         //填充业务
	         return productService.getProductList(pageNum,pageSize);
	     }else{
	         return ServerResponse.createByErrorMessage("无权限操作");
	     } 	
	 }
	 /**
	  * 带条件  的分页查询
	  * @param session
	  * @param productName
	  * @param productId
	  * @param pageNum
	  * @param pageSize
	  * @return
	  */
	 @RequestMapping("search.do")
	 @ResponseBody
	 public ServerResponse productSearch(HttpSession session,
			 							String productName,Integer productId, 
			 							@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
			 							@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	     }
	     if(userService.checkAdminRole(user).isSuccess()){
	         //填充业务
	         return productService.searchProduct(productName,productId,pageNum,pageSize);
	     }else{
	         return ServerResponse.createByErrorMessage("无权限操作");
	     } 	 
	 }
	 
	 /**
	  * 上传图片
	  * @param session
	  * @param file
	  * @param request
	  * @return
	  */
	 @SuppressWarnings("rawtypes")
	 @RequestMapping("upload.do")
	 @ResponseBody
	 public ServerResponse upload(HttpSession session,
	    					@RequestParam(value = "upload_file",required = false) MultipartFile file,
	    					HttpServletRequest request){
		 User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
	     }
	     if(userService.checkAdminRole(user).isSuccess()){
	    	 String path = request.getSession().getServletContext().getRealPath("upload");
	    	 String targetFileName = fileService.upload(file,path);
	    	 String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
	    	 Map fileMap = Maps.newHashMap();
	         fileMap.put("uri",targetFileName);
	         fileMap.put("url",url);
	         return ServerResponse.createBySuccess(fileMap);
	     }else{
	            return ServerResponse.createByErrorMessage("无权限操作");
	        }
	 }
	 
	 /**
	  * 富文本中 图片上传   
	  * 查看参考文档  有返回参数的格式定义
	  * @param session
	  * @param file
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping("richtext_img_upload.do")
	 @ResponseBody
	 public Map richtextImgUpload(HttpSession session, 
	    					@RequestParam(value = "upload_file",required = false) MultipartFile file, 
	    					HttpServletRequest request, 
	    					HttpServletResponse response){
		 Map resultMap = Maps.newHashMap();
	     User user = (User)session.getAttribute(Const.CURRENT_USER);
	     if(user == null){
	         resultMap.put("success",false);
	         resultMap.put("msg","请登录管理员");
	         return resultMap;
	     }
	       //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//       {
//           "success": true/false,
//               "msg": "error message", # optional
//           "file_path": "[real file path]"
//       }
	     if(userService.checkAdminRole(user).isSuccess()){
	    	 String path = request.getSession().getServletContext().getRealPath("upload");
	         String targetFileName = fileService.upload(file,path);
	         if(StringUtils.isBlank(targetFileName)){
	             resultMap.put("success",false);
	             resultMap.put("msg","上传失败");
	             return resultMap;
	         }
	         String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
	         resultMap.put("success",true);
	         resultMap.put("msg","上传成功");
	         resultMap.put("file_path",url);
	         // 改变响应头的  格式
	         response.addHeader("Access-Control-Allow-Headers","X-File-Name");
	         return resultMap;
	     }else{
	            resultMap.put("success",false);
	            resultMap.put("msg","无权限操作");
	            return resultMap;
	     }
	 }
} 
	 
	 
	 
	 
	 