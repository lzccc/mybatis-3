package org.apache.ibatis.submitted.basicFunctionalModel;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface UserMapper {
	@Select("SELECT * FROM users WHERE id = #{id}")
	  User getOneUser(final int id);

	  @Select("SELECT * FROM users")
	  List<User> getUsers();
	  
	  void addOne(User user);
}
