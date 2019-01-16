package cn.kang.mall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.kang.mall.common.Const;
import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.User;
import cn.kang.mall.service.IUserService;

/**
 * 管理员模块
 * @author mi
 *
 */
@Controller
@RequestMapping("/manage/user")
public class UserManagerController {
	
	@Autowired
    private IUserService userService;
	/**
	 * 管理员登录
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	 @RequestMapping(value="login.do",method = RequestMethod.POST)
	 @ResponseBody
	 public ServerResponse<User> login(String username, String password, HttpSession session){
		 ServerResponse<User> response = userService.login(username,password);
		 if(response.isSuccess()){
	            User user = response.getData();
	            if(user.getRole() == Const.Role.ROLE_ADMIN){
	                //说明登录的是管理员
	                session.setAttribute(Const.CURRENT_USER,user);
	                return response;
	            }else{
	                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
	            }
	        }
	        return response;
	 }
}














