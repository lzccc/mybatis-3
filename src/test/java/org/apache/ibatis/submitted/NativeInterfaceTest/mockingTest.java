package org.apache.ibatis.submitted.NativeInterfaceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.datasource.pooled.PoolState;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.submitted.NativeInterfaceTest.*;
import org.apache.ibatis.submitted.basicFunctionalModel.UserMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Spy;
import org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

class mockingTest {

	private static SqlSessionFactory sqlSessionFactory;

}
