package edu.sjsu.LPOS.DTO;

import edu.sjsu.LPOS.domain.Menu;

public class MenuDTO {
	private Menu menu;
	private Integer quatity;
	
	public MenuDTO() {
		
	}
	public MenuDTO(Menu menu, Integer quatity) {
		this.menu = menu;
		this.quatity = quatity;
	}
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public Integer getQuatity() {
		return quatity;
	}
	public void setQuatity(Integer quatity) {
		this.quatity = quatity;
	}
	@Override
	public String toString() {
		return "MenuDTO [menu=" + menu + ", quatity=" + quatity + "]";
	}
	
}
