package com.jx372.guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jx372.guestbook.vo.GuestbookVo;

public class GuestbookDao {
	private Connection getConnection() throws SQLException {

		Connection conn = null;

		try {
			//1. 드라이버 로딩
			Class.forName( "com.mysql.jdbc.Driver" );

			//2. Connection 하기
			String url = "jdbc:mysql://localhost:3306/guestbook?useUnicode=true&characterEncoding=utf8";
			conn = DriverManager.getConnection( url, "guestbook", "guestbook" );
		} catch( ClassNotFoundException e ) {
			System.out.println( "JDBC Driver 를 찾을 수 없습니다." );
		} 

		return conn;
	}
	public List<GuestbookVo> getList(){
		Connection conn = null;
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		Statement stmt  = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			stmt = conn.createStatement();
			String sql = "select no,name,reg_date,message from guestbook order by no desc";
			rs = stmt.executeQuery(sql);

			while(rs.next()){
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String date = rs.getString(3);
				String message = rs.getString(4);

				GuestbookVo vo = new GuestbookVo();

				vo.setNo(no);
				vo.setName(name);
				vo.setDate(date);
				vo.setMessage(message);

				list.add(vo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


		return list;
	}

	public boolean delete( GuestbookVo vo ) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "delete from guestbook WHERE no = ? AND passwd = ?" ;
			pstmt = conn.prepareStatement( sql );
			
			//4. 바인딩
			pstmt.setLong( 1, vo.getNo() );
			pstmt.setString(2, vo.getPasswd());
			
			//5. sql문 실행
			int count = pstmt.executeUpdate();
			return (count == 1);
			
		} catch ( SQLException e ) {
			System.out.println( "error:" + e );
			return false;
		} finally {
			
			/* 자원정리 */
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if(conn != null ) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
//-----------------------------------------------------------------------
	public boolean delete( Long no,String passwd ) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			
			//3. Statement 준비
			String sql = "delete from guestbook WHERE no = ? AND passwd = ?" ;
			pstmt = conn.prepareStatement( sql );
			
			//4. 바인딩
			pstmt.setLong( 1, no );
			pstmt.setString(2, passwd);
			
			//5. sql문 실행
			int count = pstmt.executeUpdate();
			return (count == 1);
			
		} catch ( SQLException e ) {
			System.out.println( "error:" + e );
			return false;
		} finally {
			
			/* 자원정리 */
			try {
				if( pstmt != null ) {
					pstmt.close();
				}
				if(conn != null ) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean insert(GuestbookVo vo){
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();

			String sql = "insert INTO guestbook values(null, ?,?,?,now())";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPasswd());
			pstmt.setString(3, vo.getMessage());

			int count = pstmt.executeUpdate();

			return count == 1;  //0이 반환되면 실패 1이 반환되면 성공

		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try{

				if(pstmt != null){
					pstmt.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch(SQLException e){
				e.printStackTrace();
			}
		}

		return false;
	}

}
