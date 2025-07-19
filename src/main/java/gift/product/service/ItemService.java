package gift.product.service;


import gift.product.entity.Item;
import gift.product.entity.User;
import gift.product.repository.ItemRepository;
import gift.product.dto.GetItemResponse;
import gift.product.dto.ItemRequest;
import gift.product.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
public class ItemService {

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}


	public Long createItem(ItemRequest req, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

		Item item = Item.builder()
			.user(user)
			.name(req.name())
			.price(req.price())
			.imageUrl(req.imageUrl())
			.build();

		Item saved = itemRepository.save(item);
		return saved.getId();
	}


	@Transactional(readOnly = true)
	public Page<GetItemResponse> getAllItems(Pageable pageable) {
		Page<Item> items = itemRepository.findAll(pageable);

		return items.map(item -> GetItemResponse.builder()
			.id(item.getId())
			.authorId(item.getUser().getId())
			.name(item.getName())
			.price(item.getPrice())
			.imageUrl(item.getImageUrl())
			.build());
	}


	@Transactional(readOnly = true)
	public GetItemResponse getItem(Long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이템입니다."));
		return GetItemResponse.builder()
			.id(item.getId())
			.authorId(item.getUser().getId())
			.name(item.getName())
			.price(item.getPrice())
			.imageUrl(item.getImageUrl())
			.build();
	}


	public GetItemResponse updateItem(Long itemId, Long userId, ItemRequest req) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이템입니다."));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
		item.isItemAuthor(user);

		item.updateItem(req.name(), req.price(), req.imageUrl());
		return getItem(itemId);
	}


	public void deleteItem(Long itemId, Long userId) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이템입니다."));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
		item.isItemAuthor(user);
		itemRepository.deleteById(itemId);
	}

}
