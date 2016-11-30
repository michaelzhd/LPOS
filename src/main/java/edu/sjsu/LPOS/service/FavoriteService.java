package edu.sjsu.LPOS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.Favorite;
import edu.sjsu.LPOS.repository.FavoriteRepository;

@Service
public class FavoriteService {
	@Autowired
	FavoriteRepository favoriteRepository;
	
	public List<Favorite> getFavoriteByUserId(Integer id) {
		return favoriteRepository.findByUser(id);
	}
	
	public Favorite saveFavorite(Favorite favorite) {
		return favoriteRepository.save(favorite);
	}
	
	public Favorite getFavoriteByUserIdAndRestaurantId(Integer userId, Integer restaurantId) {
		return favoriteRepository.findByUserAndRestaurant_id(userId, restaurantId);
	}
	
	public void deleteFavorite(Favorite favorite) {
		favoriteRepository.delete(favorite);
		return;
	}
}
