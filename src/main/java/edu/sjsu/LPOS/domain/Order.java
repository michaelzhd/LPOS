package edu.sjsu.LPOS.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_menu")
//@IdClass(OrderId.class)
public class Order{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;	
	
	private Integer tableReserveId;
	
	private Integer menuId;
	
	private Integer quatity;
	
	public Order() {
		
	}
	
	public Order(Integer tableReserveId, Integer menuId, Integer quatity) {
		this.tableReserveId = tableReserveId;
		this.menuId = menuId;
		this.quatity = quatity;
	}
	
//	@ManyToOne
//	@JoinColumn(name = "tableReserveId", updatable = false, insertable = false, referencedColumnName = "id")
//	private TableReserve tableReserve;
//	
//	@ManyToOne
//	@JoinColumn(name = "menuId", updatable = false, insertable = false, referencedColumnName = "id")
//	private Menu menu;

	public Integer getTableReserveId() {
		return tableReserveId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTableReserveId(Integer tableReserveId) {
		this.tableReserveId = tableReserveId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getQuatity() {
		return quatity;
	}

	public void setQuatity(Integer quatity) {
		this.quatity = quatity;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", tableReserveId=" + tableReserveId + ", menuId=" + menuId + ", quatity=" + quatity
				+ "]";
	}

	
}
