/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.submitted.basetest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.junit.MockitoTestListener;

class BaseTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  static void setUp() throws Exception {
    // create an SqlSessionFactory
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/basetest/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // populate in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
            "org/apache/ibatis/submitted/basetest/CreateDB.sql");
  }
  
  static void setUp2() throws Exception {
	    // create an SqlSessionFactory
	    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/basetest/mybatis-config2.xml")) {
	      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
	    }

	    // populate in-memory database
	    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
	            "org/apache/ibatis/submitted/basetest/CreateDB.sql");
	  }

  @Test
  void shouldGetAUser() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      User user = mapper.getUser(1);
      Assertions.assertEquals("User1", user.getName());
    }
  }

  @Test
  void shouldInsertAUser() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);
      User user = new User();
      user.setId(2);
      user.setName("User2");
      mapper.insertUser(user);
    }
  }
  
  @Ignore
  void usePoolConection() throws Exception {

	  PooledDataSource dsDataSource = (PooledDataSource) sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
	  List<Connection> connections = new ArrayList<>();
      for (int i = 0; i < 3; i++) {
        connections.add(dsDataSource.getConnection());
      }
      assertEquals(3, dsDataSource.getPoolState().getActiveConnectionCount());
      for (Connection c : connections) {
        c.close();
      }
      connections.add(dsDataSource.getConnection());
      assertEquals(2, dsDataSource.getPoolState().getIdleConnectionCount());
	  setUp2();
  }
  
 
  @Ignore
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
      assertEquals(null, connections.get(11));
	  setUp2();
  }

}
