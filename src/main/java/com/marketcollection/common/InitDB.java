package com.marketcollection.common;

import com.marketcollection.domain.common.Address;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.*;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.OrderStatus;
import com.marketcollection.domain.review.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class InitDB {

    private final InitService initService;

//    @PostConstruct
    public void init() {
            initService.dbInit1();
    }

    @RequiredArgsConstructor
    @Transactional
    @Component
    public static class InitService {
        private final EntityManager em;
        private final ItemRepository itemRepository;
        private final InitDBRepository initDBRepository;

        @RequiredArgsConstructor
        @Repository
        static class InitDBRepository {
        private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

            public void bulkItemInsert(List<Item> items) {
                String sql = "insert into item (item_name, original_price, sale_price, discount_price, stock_quantity, description, category_id, rep_image_url, sales_count, review_count, hit, item_sale_status) " +
                        "values (:itemName, :originalPrice, :salePrice, :discountPrice, :stockQuantity, :description, :categoryId, :repImageUrl, :salesCount, :reviewCount, :hit, \"ON_SALE\")";

                SqlParameterSource[] params = items.stream()
                        .map(BeanPropertySqlParameterSource::new)
                        .toArray(SqlParameterSource[]::new);
                namedParameterJdbcTemplate.batchUpdate(sql, params);
            }
        }

        public void dbInit1() {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            List<Item> items = IntStream.range(1, 1000000)
                    .parallel()
                    .mapToObj(i -> Item.builder()
                            .itemName("향기가득 샤인머스캣_" + i)
                            .originalPrice((int) (Math.random() * 10000) * 10)
                            .salePrice((int) (Math.random() * 10000) * 10)
                            .discountPrice(0)
                            .stockQuantity(10000)
                            .description("너무 맛있어요")
                            .categoryId(24L)
                            .repImageUrl("/image/item/grape1.jpg")
                            .salesCount((int) (Math.random() * 10000))
                            .reviewCount((int) (Math.random() * 10000))
                            .hit((int) (Math.random() * 10000))
                            .itemSaleStatus(ItemSaleStatus.ON_SALE)
                            .build())
                    .collect(Collectors.toList());
            initDBRepository.bulkItemInsert(items);
            stopWatch.stop();
            System.out.println("InitDB 소요 시간 : " + stopWatch.getTotalTimeSeconds());

            for(int i = 1000001; i <= 1000010; i++) {

                Item item = Item.builder()
                        .itemName("향기가득 샤인머스캣_" + i)
                        .originalPrice((int)(Math.random() * 10000) * 10)
                        .salePrice((int)(Math.random() * 10000) * 10)
                        .stockQuantity(10000)
                        .description("너무 맛있어요")
                        .categoryId(14L)
                        .repImageUrl("/image/item/grape1.jpg")
//                        .salesCount((int)(Math.random() * 10000))
//                        .hit((int)(Math.random() * 10000))
                        .itemSaleStatus(ItemSaleStatus.ON_SALE)
                        .build();
                em.persist(item);

                ItemImage repImage = new ItemImage(item, "grape1", "renamed_grape1", "https://market-collection-s3.s3.ap-northeast-2.amazonaws.com/image/grape1.jpg", true);
                em.persist(repImage);

                ItemImage itemImage = new ItemImage(item, "grape2", "renamed_grape2", "https://market-collection-s3.s3.ap-northeast-2.amazonaws.com/image/grape2.jpg", false);
                em.persist(itemImage);

                Item item2 = Item.builder()
                        .itemName("1등급 한우 안심 스테이크_" + i)
                        .originalPrice((int)(Math.random() * 10000) * 10)
                        .salePrice((int)(Math.random() * 10000) * 10)
                        .stockQuantity(10000)
                        .description("너무 맛있어요")
                        .categoryId(39L)
                        .repImageUrl("https://market-collection-s3.s3.ap-northeast-2.amazonaws.com/image/meat1.jpg")
//                        .salesCount((int)(Math.random() * 10000))
//                        .hit((int)(Math.random() * 10000))
                        .itemSaleStatus(ItemSaleStatus.ON_SALE)
                        .build();
                em.persist(item2);

                ItemImage repImage2 = new ItemImage(item2, "meat1", "renamed_meat1", "https://market-collection-s3.s3.ap-northeast-2.amazonaws.com/image/meat1.jpg", true);
                em.persist(repImage2);

                ItemImage itemImage2 = new ItemImage(item2, "meat2", "renamed_meat2", "https://market-collection-s3.s3.ap-northeast-2.amazonaws.com/image/meat2.jpg", false);
                em.persist(itemImage2);

                Member member = Member.builder()
                        .socialType(SocialType.NAVER)
                        .memberStatus(MemberStatus.ACTIVE)
                        .memberName("tester_" + i)
                        .email("tester" + i + "@naver.com")
                        .address(new Address())
                        .role(Role.USER)
                        .grade(Grade.FRIENDS)
                        .build();
                em.persist(member);

                OrderItem orderItem = OrderItem.builder()
                        .item(item)
                        .orderPrice(100000)
                        .count(1)
                        .build();
                em.persist(orderItem);

                Order order = Order.builder()
                        .member(member)
                        .orderItems(List.of(orderItem))
                        .orderStatus(OrderStatus.DONE)
                        .build();
                orderItem.setOrder(order);

                em.persist(order);

                for(int j = 0; j <= 10; j++) {
                    Review review = Review.builder()
                            .item(item)
                            .member(member)
                            .content("너무 좋아요, 또 사고 싶어요")
                            .build();
                    em.persist(review);
                }
                for(int j = 0; j <= 10; j++) {
                    Review review2 = Review.builder()
                            .item(item)
                            .member(member)
                            .content("너무 좋아요, 또 사고 싶어요")
                            .build();
                    em.persist(review2);
                }
            }
        }
    }
}
