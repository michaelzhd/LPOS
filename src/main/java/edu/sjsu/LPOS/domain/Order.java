package edu.sjsu.LPOS.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ordertable")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String menu;
	//table reserve id
//	@OneToOne
//	@JoinColumn(name = "tablereserve_id", foreignKey = @ForeignKey(name = "FK_TABLEID"))
//	private TableReserve tableReserve;
	
	//menu id many to many
	
	
//	@OneToOne
//	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USERID"))
//	private User user;

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

//	public TableReserve getTableReserve() {
//		return tableReserve;
//	}
//
//	public void setTableReserve(TableReserve tableReserve) {
//		this.tableReserve = tableReserve;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
	
	
}
