package gift.product.controller;


import gift.product.commons.annotations.Authenticated;
import gift.product.dto.CreateWishListRequest;
import gift.product.dto.GetWishListResponse;
import gift.product.entity.WishList;
import gift.product.service.WishListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/wishlists")
public class WishListController {
	private final WishListService wishListService;
	public WishListController(WishListService wishListService) {
		this.wishListService = wishListService;
	}

	@GetMapping
	@Authenticated
	public List<GetWishListResponse> getWishList(@RequestAttribute Long userId) {
		return wishListService.getWishList(userId);
	}

	@PostMapping
	@Authenticated
	public Long createWishList(@RequestAttribute Long userId, @RequestBody CreateWishListRequest request) {
		return wishListService.createWishList(userId, request);
	}

	@DeleteMapping("/{wishListId}")
	@Authenticated
	public void deleteWishList(@RequestAttribute Long userId, @PathVariable Long wishListId) {
		wishListService.deleteWishList(userId, wishListId);
	}

	@PatchMapping("/{wishListId}/update")
	@Authenticated
	public void updateWishListAmount(@RequestAttribute Long userId, @PathVariable Long wishListId, @RequestParam(name = "amount") int amount) {
		wishListService.updateAmount(userId, wishListId, amount);
	}

}
