package cn.kang.mall.mapper;

import org.apache.ibatis.annotations.Param;

import cn.kang.mall.entiy.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	int checkUsername(String username);

	User selectLogin(@Param("username")String username, @Param("password")String password);
	
	int checkEmail(String str);

	String selectQuestionByUsername(String username);

	int checkAnswer(@Param("username")String username, @Param("question")String question, @Param("answer")String answer);
	
	
	int updatePasswordByUsername(@Param("username")String username, @Param("password")String password);

	int checkPassword(@Param("password")String password, @Param("id")Integer id);

	int checkEmailByUserId(@Param("email")String email, @Param("id")Integer id);
}







