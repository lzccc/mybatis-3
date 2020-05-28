package org.apache.ibatis.submitted.basicFunctionalModel;

import static org.assertj.core.api.Assertions.anyOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//@ExtendWith(MockitoExtension.class)
class MockTest extends BaseDataTest  {

	private static SqlSessionFactory sqlSessionFactory;

	SqlSessionFactory ssf;
	SqlSession ss;
	UserMapper some;
	User userWithCorrectPassword;
		
	void setUp() throws Exception {
		  ssf = mock(SqlSessionFactory.class);
		  ss = mock(SqlSession.class);
		  some = mock(UserMapper.class);
		  userWithCorrectPassword = new User();
		  when(ssf.openSession()).thenReturn(ss);
		  when(ss.getMapper(UserMapper.class)).thenReturn(some);	    
		  when(some.getOneUser(ArgumentMatchers.anyInt())).thenReturn(userWithCorrectPassword);
	  }
	@Test
	void test() throws Exception {
		setUp();
		SqlSession session = ssf.openSession();
		UserMapper up = session.getMapper(UserMapper.class);
		assertNotNull(up.getOneUser(1));
		//fail("Not yet implemented");
	}
	
	@Mock
	  private Connection conn;

	  @Test
	  void shouldCloseConnection() throws Exception {
	    TransactionFactory tranFac = new ManagedTransactionFactory();
	    tranFac.setProperties(new Properties());
	    Transaction tranInstance = tranFac.newTransaction(conn);
	    assertEquals(conn, tranInstance.getConnection());
	    tranInstance.commit();
	    tranInstance.rollback();
	    tranInstance.close();
	    verify(conn).close();
	  }

}
