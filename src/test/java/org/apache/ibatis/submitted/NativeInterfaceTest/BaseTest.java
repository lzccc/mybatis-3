package org.apache.ibatis.submitted.NativeInterfaceTest;

import java.io.Reader;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BaseTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  static void setUp() throws Exception {
    // create an SqlSessionFactory
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/NativeInterfaceTest/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // populate in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
            "org/apache/ibatis/submitted/NativeInterfaceTest/CreateDB.sql");
  }

  @Test
  void shouldGetAUser() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
    	User user = (User) sqlSession.selectOne("listUser", 6);
    	Assertions.assertEquals("User6", user.getName());
    }
  }

  @Test
  void shouldNotGetAUserUsingString() {
	  boolean thrown = false;
	  try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
		  User user = (User) sqlSession.selectOne("listUser", "1");
	  }catch (PersistenceException e) {
		  thrown = true;
	  }
  	  Assertions.assertTrue(thrown);
  }
  
  @Test
  void shouldNotGetAUserUsingLargerNumber() {
	  try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
		  User user = (User) sqlSession.selectOne("listUser", 7);
		  Assertions.assertNull(user);
  	  }
  }
  
  @Test
  void shouldNotGetAUserUsingSmallerNumber() {
	  try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
		  User user = (User) sqlSession.selectOne("listUser", 0);
		  Assertions.assertNull(user);
  	  }
  }
  
  @Test
  void shouldNotGetAUserUsingFloatNumber() {
	  boolean thrown = false;
	  try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
		  User user = (User) sqlSession.selectOne("listUser", 3.5);
	  }catch (PersistenceException e) {
		  thrown = true;
	  }
	  Assertions.assertTrue(thrown);
  }
  
  @Test
  void shouldGetAUserUsingWrapperClass() {
	  try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
		  User user = (User) sqlSession.selectOne("listUser",new Integer(1));
  		  Assertions.assertEquals("User1", user.getName());
  	  }
  }

}
