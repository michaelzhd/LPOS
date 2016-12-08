package edu.sjsu.LPOS.DTO;

import edu.sjsu.LPOS.domain.Menu;

public class CreateMenuDTO {
	private Menu menu;
	private Integer restaurantId;
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public Integer getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}
	
}
