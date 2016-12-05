package edu.sjsu.LPOS.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.sjsu.LPOS.DTO.OrderMenuDTO;
import edu.sjsu.LPOS.DTO.ReservationDTO;

@Entity
@Table(name = "tablereserve")
public class TableReserve {
//	@PersistenceContext
//	EntityManager em;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String timeSlot;
	
	private Integer people;	

	private String date;
	
	private boolean isPrivate;

	private boolean takeOut;
	
	private double price;
	
	@OneToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USERID"))
	@JsonIgnore
	private User user;
	
	@ManyToOne
	@JoinColumn(name="restaurant_id", referencedColumnName="id", nullable = false, updatable = true, insertable = true)
	@JsonIgnore
	private Restaurant restaurant;
	
////	@OneToMany(mappedBy="tableReserve")
//	@Transient
//	private List<OrderMenuDTO> menus;
//	
//
//	
////	public void addMenu(Menu menu, Integer quatity) {
////	    Order order = new Order();
////	    order.setMenu(menu);
////	    order.setTableReserve(this);
////	    order.setMenuId(menu.getId());
////	    order.setTableReserveId(this.getId());
////	    order.setQuatity(quatity);
////	    em.persist(order);
////	    this.menus.add(order);
////	    menu.getTableReserve().add(order);
////	  }
//	
//	
//	public List<OrderMenuDTO> getMenus() {
//		return menus;
//	}
//	public void setMenus(List<OrderMenuDTO> menus) {
//		this.menus = menus;
//	}
	public TableReserve(User user, Restaurant restaurant, ReservationDTO reservation) {
		this.setUser(user);
		this.setDate(reservation.getDate());
		this.setPeople(reservation.getPeople());
		this.setPrivate(reservation.getIsPrivate());
		this.setRestaurant(restaurant);
		this.setTimeSlot(reservation.getTimeSlot());
	}
	public TableReserve() {
		
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean getIsPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public Integer getPeople() {
		return people;
	}

	public void setPeople(Integer people) {
		this.people = people;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public boolean getTakeOut() {
		return takeOut;
	}
	public void setTakeOut(boolean takeOut) {
		this.takeOut = takeOut;
	}
public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	//	public List<Order> getMenus() {
//		return menus;
//	}
//	public void setMenus(List<Order> menus) {
//		this.menus = menus;
//	}
	@Override
	public String toString() {
		return "TableReserve [id=" + id + ", timeSlot=" + timeSlot + ", people=" + people + ", date=" + date
				+ ", isPrivate=" + isPrivate + ", user=" + user + ", restaurant=" + restaurant + "]";
	}



}
