package cn.kang.mall.service;

import cn.kang.mall.common.ServerResponse;
import cn.kang.mall.entiy.User;

public interface IUserService {
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	ServerResponse<User> login(String username, String password);
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	ServerResponse<String> register(User user);
	
	/**
	 * 检验用户名 和 邮箱
	 * @param str
	 * @param type
	 * @return
	 */
	ServerResponse<String> checkValid(String str, String type);
	
	/**
	 * 获取的忘记密码提示问题
	 * @param username
	 * @return
	 */
	ServerResponse<String> selectQuestion(String username);
	
	/**
	 * 检验问题和答案
	 * @param username
	 * @param question
	 * @param answer
	 * @return
	 */
	ServerResponse<String> checkAnswer(String username, String question, String answer);
	
	/**
	 * 通过回答问题 重置密码
	 * @param username
	 * @param passwordNew
	 * @param forgetToken
	 * @return
	 */
	ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

	/**
	 * 通过原来密码  重置密码
	 * @param passwordOld
	 * @param passwordNew
	 * @param user
	 * @return
	 */
	ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	ServerResponse<User> updateInformation(User user);
	
	/**
	 * 获取用户详细信息
	 * @param id
	 * @return
	 */
	ServerResponse<User> getInformation(Integer id);
	
	/**
	 * 检验是否是管理员
	 * @param user
	 * @return
	 */
	ServerResponse checkAdminRole(User user);	

}
