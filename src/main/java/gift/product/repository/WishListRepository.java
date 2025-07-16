package gift.product.repository;


import gift.product.entity.User;
import gift.product.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
	Optional<WishList> findByUserIdAndItemId(Long userId, Long itemId);
	List<WishList> findAllByUser(User user);

}
