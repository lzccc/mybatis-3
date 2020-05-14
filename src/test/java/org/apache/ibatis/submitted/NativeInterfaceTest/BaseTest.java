package org.apache.ibatis.submitted.NativeInterfaceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransaction;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mysql.cj.log.Log;


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
  
  @Test
  void usePoolConection() throws Exception {

	  PooledDataSource dsDataSource = (PooledDataSource) sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
	  List<Connection> connections = new ArrayList<>();
      for (int i = 0; i < 3; i++) {
        connections.add(dsDataSource.getConnection());
      }
      assertEquals(10, dsDataSource.getPoolState().getActiveConnectionCount());
      for (Connection c : connections) {
        c.close();
      }
      connections.add(dsDataSource.getConnection());
      assertEquals(2, dsDataSource.getPoolState().getIdleConnectionCount());
  }
  
 
  @Test
  void usePoolConection2() throws Exception {
	  try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
	      sqlSession.close();
	    }
	  PooledDataSource dsDataSource = (PooledDataSource) sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
	  List<Connection> connections = new ArrayList<>();
      for (int i = 0; i < 11; i++) {
        connections.add(dsDataSource.getConnection());
      }
     
      connections.add(dsDataSource.getConnection());
      assertEquals(0, dsDataSource.getPoolState().getIdleConnectionCount());
      assertEquals(10, dsDataSource.getPoolState().getActiveConnectionCount());
      //assertEquals(null, connections.get(11));
  }
  


}
