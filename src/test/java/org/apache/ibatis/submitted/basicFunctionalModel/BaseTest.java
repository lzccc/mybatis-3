package org.apache.ibatis.submitted.basicFunctionalModel;

import java.io.Reader;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mysql.cj.Session;


class BaseTest {

  private static SqlSessionFactory sqlSessionFactory;

  
  static void setUp() throws Exception {
    // create an SqlSessionFactory
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/basicFunctionalModel/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // populate in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
            "org/apache/ibatis/submitted/basicFunctionalModel/CreateDB.sql");
  }

  @Test
  void shouldHaveDefaultFactory() throws Exception {
	  Assertions.assertEquals(DefaultSqlSessionFactory.class, sqlSessionFactory.getClass());
  }
  
  @Test
  void shouldHaveConfiguration() throws Exception {
	  setUp();
	  Assertions.assertNotNull(sqlSessionFactory);
	  Assertions.assertNotNull(sqlSessionFactory.getConfiguration());
  }
  
  @Test
  void shouldGetSession() throws Exception {
	  setUp();
	  Assertions.assertNotNull(sqlSessionFactory.openSession());
  }
  
  @Test
  void shouldGetMapper() throws Exception {
	  setUp();
	  SqlSession sqlSession = sqlSessionFactory.openSession();
	  Assertions.assertNotNull(sqlSession.getMapper(UserMapper.class));
  }
  
  @Test
  void shouldGetAUserUsingNativeFunction() throws Exception {
	setUp();
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
    	User user = (User) sqlSession.selectOne("listUser", 1);
    	Assertions.assertEquals("User1", user.getName());
    }
  }
  
  @Test
  void shoutldGetAUserUsingCustomFunction() throws Exception {
	setUp();
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
    	UserMapper usermapper = sqlSession.getMapper(UserMapper.class);
    	User user = usermapper.getOneUser(1);
    	Assertions.assertEquals("User1", user.getName());
    }
  }

  @Test
  void shouldUpdateDB() throws Exception {
	setUp();
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
    	UserMapper usermapper = sqlSession.getMapper(UserMapper.class);
    	User newuser = new User();
    	newuser.setId(2);
    	newuser.setName("Lily");
    	usermapper.addOne(newuser);
    	Assertions.assertEquals(2, usermapper.getUsers().size());
    }
  }
  
  @Test
  void shouldRollBack() throws Exception {
	setUp();
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
    	UserMapper usermapper = sqlSession.getMapper(UserMapper.class);
    	User newuser = new User();
    	int origin = usermapper.getUsers().size();
    	newuser.setId(3);
    	newuser.setName("Tom");
    	usermapper.addOne(newuser);
    	sqlSession.rollback();
    	Assertions.assertEquals(origin, usermapper.getUsers().size());
    }
  }

}
