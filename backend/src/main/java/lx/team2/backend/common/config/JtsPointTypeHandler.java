package lx.team2.backend.common.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTWriter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JtsPointTypeHandler extends BaseTypeHandler<Point> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Point parameter, JdbcType jdbcType) throws SQLException {
        // Point 객체를 WKT(Well-Known Text) 문자열로 변환하여 DB에 전달
        // PostGIS는 WKT 문자열을 geography 타입으로 자동 변환해줍니다.
        WKTWriter writer = new WKTWriter();
        ps.setString(i, writer.write(parameter));
    }

    @Override
    public Point getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // DB에서 16진수 문자열(Hex String) 형태로 WKB 데이터를 가져옵니다.
        String hexWkb = rs.getString(columnName);
        return parseHexWkb(hexWkb);
    }

    @Override
    public Point getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String hexWkb = rs.getString(columnIndex);
        return parseHexWkb(hexWkb);
    }

    @Override
    public Point getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String hexWkb = cs.getString(columnIndex);
        return parseHexWkb(hexWkb);
    }

    /**
     * 16진수 WKB 문자열을 JTS Point 객체로 변환합니다.
     */
    private Point parseHexWkb(String hexWkb) throws SQLException {
        if (hexWkb == null || hexWkb.isEmpty()) {
            return null;
        }
        try {
            // JTS에서 제공하는 유틸리티를 사용해 16진수 문자열을 byte 배열로 변환
            byte[] wkbBytes = WKBReader.hexToBytes(hexWkb);
            // WKBReader를 사용해 byte 배열을 Geometry 객체로 파싱
            Geometry geom = new WKBReader().read(wkbBytes);
            if (geom instanceof Point) {
                return (Point) geom;
            }
            return null;
        } catch (ParseException e) {
            throw new SQLException("Failed to convert WKB hex string to Point.", e);
        }
    }
}