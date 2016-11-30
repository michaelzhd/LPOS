package edu.sjsu.LPOS.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;







@Entity
@Table(name = "tablereserve")
public class TableReserve {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	String reserveTime;
	
	String role;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "tableinfo_id",referencedColumnName="id", nullable = false, updatable = true, insertable = true)
	private TableInfo tableinfo;
	
	@OneToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USERID"))
	private User user;

	
	public String getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(String reserveTime) {
		this.reserveTime = reserveTime;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@JsonIgnore
	public TableInfo getTableInfo() {
		return tableinfo;
	}
	
	@JsonIgnore
	public void setTableInfo(TableInfo tableInfo) {
		this.tableinfo = tableInfo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "TableReserve [id=" + id + ", tableInfo=" + tableinfo + ", user=" + user + "]";
	}
	
	
}
