package gift.product.repository;


import gift.product.entity.Item;

import java.util.List;
import java.util.Optional;


public interface ItemRepository {

	// 생성
	Long save(Item item);

	// 단건조회
	Optional<Item> findById(Long id);

	// 목록조회
	List<Item> findAll();

	// 수정
	void update(Long id, Item item);

	// 삭제
	void deleteById(Long id);

}
