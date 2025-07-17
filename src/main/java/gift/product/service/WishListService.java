package gift.product.service;


import gift.product.dto.CreateWishListRequest;
import gift.product.dto.GetWishListResponse;
import gift.product.entity.Item;
import gift.product.entity.User;
import gift.product.entity.WishList;
import gift.product.repository.ItemRepository;
import gift.product.repository.UserRepository;
import gift.product.repository.WishListRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@Transactional
public class WishListService {

	private final WishListRepository wishListRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;

	public WishListService(WishListRepository wishListRepository, UserRepository userRepository, ItemRepository itemRepository) {
		this.wishListRepository = wishListRepository;
		this.userRepository = userRepository;
		this.itemRepository = itemRepository;
	}

	public Long createWishList(Long userId, CreateWishListRequest request) {
		if(wishListRepository.findByUserIdAndItemId(userId, request.itemId()).isPresent())
			throw new DataIntegrityViolationException("이미 위시리스트에 담은 상품입니다");

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
		Item item = itemRepository.findById(request.itemId())
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이템입니다."));

		WishList wishList = new WishList(user, item, 1);
		WishList saved = wishListRepository.save(wishList);

		return saved.getId();
	}

	public void updateAmount(Long userId, Long wishListId, int amount) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
		WishList wishItem = wishListRepository.findById(wishListId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 위시리스트 상품입니다."));

		wishItem.validateOwner(user);
		wishItem.updateAmount(amount);
	}

	@Transactional(readOnly = true)
	public List<GetWishListResponse> getWishList(Long userId, int page) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

		int index = page -1;
		PageRequest pageRequest = PageRequest.of(index, 2);
		Page<WishList> myWishList = wishListRepository.findAllByUser(user, pageRequest);

		return myWishList.stream()
			.map(GetWishListResponse::from)
			.collect(Collectors.toList());
	}

	public void deleteWishList(Long userId, Long wishListId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
		WishList wishItem = wishListRepository.findById(wishListId)
				.orElseThrow(() -> new NoSuchElementException("존재하지 않는 위시 아이템입니다."));

		wishItem.validateOwner(user);
		wishListRepository.delete(wishItem);
	}
}
