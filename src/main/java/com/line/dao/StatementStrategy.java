package com.line.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {
    // 인터페이스를 의존하게 해서 구현체를 필요에 따라 선택할 수 있게 설계하는 디자인 패턴
    PreparedStatement makePreparedStatement(Connection conn) throws SQLException;
}
