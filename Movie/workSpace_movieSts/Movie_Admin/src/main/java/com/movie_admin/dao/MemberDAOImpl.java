package com.movie_admin.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.movie_admin.vo.MemberBean;
import com.sun.org.apache.xml.internal.utils.NameSpace;

@Repository
public class MemberDAOImpl implements MemberDAO{

	@Inject
	SqlSession sqlsession;
	
	private static final String namespace = "com.movie_admin.mapper.MemberMapper";
	
	@Override
	public List<MemberBean> getMember() {
		return sqlsession.selectList(namespace+".getMember");
	}

}