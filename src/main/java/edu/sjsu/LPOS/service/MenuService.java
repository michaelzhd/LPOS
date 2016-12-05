package edu.sjsu.LPOS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.Menu;
import edu.sjsu.LPOS.repository.MenuRepository;

@Service
public class MenuService {
	@Autowired
	MenuRepository menuRepository;
	
	public Menu getMenuById(Integer id) {
		return menuRepository.findOne(id);
	}
	public Menu getMenuByRestaurantId(Integer id) {
		return menuRepository.findByRestaurant_id(id);
	}
	
	public Menu getMenuByName(String name) {
		return menuRepository.findByName(name);
	}
	
	public Iterable<Menu> getMenuByStatus(String status) {
		return menuRepository.findByStatus(status);
	}
	
	public Iterable<Menu> getMenuByRestaurantIdAndStatus(String id, String status) {
		return menuRepository.findByRestaurant_idAndStatus(id, status);
	}
	
	public Menu saveMenu(Menu menu) {
		return menuRepository.save(menu);
	}
	
	public void deleteMenu(Integer id) {
		menuRepository.delete(id);
		return;
	}
}
