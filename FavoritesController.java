package com.jbhunt.infrastructure.notification.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jbhunt.infrastructure.notification.favorite.dto.LinkDTO;
import com.jbhunt.infrastructure.notification.service.FavoriteService;


@RestController
@RequestMapping("/favorites")
public class FavoritesController {
	
	private final FavoriteService favoriteService;
	
	public FavoritesController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

  @RequestMapping(method = RequestMethod.POST, value = "/favoriteList")
  public List<LinkDTO> getFavoriteList(@RequestParam(value = "userId") String userId, @RequestBody List<LinkDTO> links){
    return favoriteService.getFavoriteList(userId, links);
  }
  @RequestMapping(method = RequestMethod.GET, value = "/favorite")
  public void createFavorite(@RequestParam(value = "userId") String userId,
                             @RequestParam(value = "application") String application){
    favoriteService.createFavorite(userId, application);
  }
  @RequestMapping(method = RequestMethod.DELETE, value = "/favorite")
  public void deleteFavorite(@RequestParam(value = "userId") String userId,
                             @RequestParam(value = "application") String application){
    favoriteService.deleteFavorite(userId, application);
  }
}
