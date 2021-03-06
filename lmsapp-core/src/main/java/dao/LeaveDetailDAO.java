package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import model.Employee;
import model.LeaveDetail;
import model.LeaveStatus;
import model.LeaveType;
import util.ConnectionUtil;

public class LeaveDetailDAO {

	private JdbcTemplate jdbcTemplate = ConnectionUtil.getJdbcTemplate();

	public void applyLeave(LeaveDetail ld) {

		String sql = "INSERT INTO EMPLOYEE_LEAVE_DETAILS ( EMP_ID , FROM_DATE, TO_DATE, NO_OF_DAYS, LEAVE_TYPE,  STATUS_ID, APPLIED_DATE, MODIFIED_DATE )"
				+ "VALUES ( ?, ?, ?, ?, ?, ?, NOW(), NOW() )";

		int rows = jdbcTemplate.update(sql, ld.getEmployee().getId(), ld.getFromDate(), ld.getToDate(),
				ld.getNoOfDays(), ld.getLeaveType().getId(), ld.getStatus().getId());

		System.out.println("No of rows inserted:" + rows);
	}

	public void update(LeaveDetail ld) {

		String sql = "UPDATE EMPLOYEE_LEAVE_DETAILS SET STATUS_ID = ? , MODIFIED_BY = ? , MODIFIED_DATE= NOW() WHERE ID = ?";

		int rows = jdbcTemplate.update(sql, ld.getStatus().getId(), ld.getModifiedBy().getId(), ld.getId());

		System.out.println("No of rows inserted:" + rows);

	}

	public List<LeaveDetail> list(Long empId) {

		String sql = "SELECT e.NAME, ld.ID, ld.EMP_ID, FROM_DATE,TO_DATE, NO_OF_DAYS, LEAVE_TYPE AS LEAVE_TYPE_ID, ( SELECT LEAVE_TYPE FROM LEAVE_TYPES WHERE ID = ld.LEAVE_TYPE) LEAVE_TYPE , STATUS_ID, ( SELECT CODE FROM LEAVE_STATUS WHERE ID = STATUS_ID ) LEAVE_STATUS, ld.APPLIED_DATE, ld.MODIFIED_BY, ld.MODIFIED_DATE FROM EMPLOYEE_LEAVE_DETAILS ld , EMPLOYEES e WHERE ld.EMP_ID = e.ID AND EMP_ID= ? ";

		// List<LeaveDetail> list = jdbcTemplate.query(sql, new Object[] { empId
		// }, new LeaveDetailRowMapper());

		List<LeaveDetail> list = jdbcTemplate.query(sql, new Object[] { empId }, (rs, rowNo) -> {
			Employee emp = new Employee();
			
			emp.setId(rs.getLong("EMP_ID"));
			emp.setName(rs.getString("NAME"));
			
			Employee modifiedBy = new Employee();
			modifiedBy.setId(rs.getLong("MODIFIED_BY"));

			LeaveStatus ls = new LeaveStatus();
			ls.setId(rs.getLong("STATUS_ID"));
			ls.setStatus(rs.getString("LEAVE_STATUS"));

			LeaveType lt = new LeaveType();
			lt.setId(rs.getLong("LEAVE_TYPE_ID"));
			lt.setType(rs.getString("LEAVE_TYPE"));

			LeaveDetail ld = new LeaveDetail();
			ld.setId(rs.getLong("ID"));
			ld.setEmployee(emp);
			ld.setFromDate(rs.getDate("FROM_DATE").toLocalDate());
			ld.setToDate(rs.getDate("TO_DATE").toLocalDate());
			ld.setNoOfDays(rs.getFloat("NO_OF_DAYS"));
			ld.setLeaveType(lt);
			ld.setStatus(ls);
			ld.setAppliedDate(rs.getDate("APPLIED_DATE").toLocalDate());
			ld.setModifiedBy(modifiedBy);
			ld.setModifiedDate(rs.getDate("MODIFIED_DATE").toLocalDate());
			return ld;
		});
		return list;

	}

	private class LeaveDetailRowMapper implements RowMapper<LeaveDetail> {

		public LeaveDetail mapRow(ResultSet rs, int rowNo) throws SQLException {

			Employee emp = new Employee();
			emp.setId(rs.getLong("EMP_ID"));

			Employee modifiedBy = new Employee();
			modifiedBy.setId(rs.getLong("MODIFIED_BY"));

			LeaveStatus ls = new LeaveStatus();
			ls.setId(rs.getLong("STATUS_ID"));

			LeaveType lt = new LeaveType();
			lt.setId(rs.getLong("LEAVE_TYPE"));

			LeaveDetail ld = new LeaveDetail();
			ld.setId(rs.getLong("ID"));
			ld.setEmployee(emp);
			ld.setFromDate(rs.getDate("FROM_DATE").toLocalDate());
			ld.setToDate(rs.getDate("TO_DATE").toLocalDate());
			ld.setNoOfDays(rs.getFloat("NO_OF_DAYS"));
			ld.setLeaveType(lt);
			ld.setStatus(ls);
			ld.setModifiedBy(modifiedBy);
			ld.setModifiedDate(rs.getDate("MODIFIED_DATE").toLocalDate());
			return ld;
		}

	}
}
