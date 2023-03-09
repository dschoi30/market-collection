package com.marketcollection.common;

import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.domain.item.Category;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.repository.ItemRepository;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.MemberStatus;
import com.marketcollection.domain.member.Role;
import com.marketcollection.domain.member.SocialType;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.OrderStatus;
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

@Profile("local")
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
                String sql = "insert into item (item_name, original_price, sale_price, stock_quantity, description, category, rep_image_url, sales_count, hit, item_sale_status) " +
                        "values (:itemName, :originalPrice, :salePrice, :stockQuantity, :description, \"FRUIT_RICE\", :repImageUrl, :salesCount, :hit, \"ON_SALE\")";

                SqlParameterSource[] params = items.stream()
                        .map(BeanPropertySqlParameterSource::new)
                        .toArray(SqlParameterSource[]::new);
                namedParameterJdbcTemplate.batchUpdate(sql, params);
            }
        }

        public void dbInit1() {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            List<Item> items = IntStream.range(1, 10000)
                    .parallel()
                    .mapToObj(i -> Item.builder()
                            .itemName("향기가득 샤인머스캣_" + i)
                            .originalPrice((int) (Math.random() * 10000) * 10)
                            .salePrice((int) (Math.random() * 10000) * 10)
                            .stockQuantity(10000)
                            .description("너무 맛있어요")
                            .category(Category.SEAFOOD)
                            .repImageUrl("/image/item/grape1.jpg")
                            .salesCount((int) (Math.random() * 10000))
                            .hit((int) (Math.random() * 10000))
                            .itemSaleStatus(ItemSaleStatus.ON_SALE)
                            .build())
                    .collect(Collectors.toList());
            initDBRepository.bulkItemInsert(items);
            stopWatch.stop();
            System.out.println("InitDB 소요 시간 : " + stopWatch.getTotalTimeSeconds());

            for(int i = 1; i <= 100; i++) {

                Item item = Item.builder()
                        .itemName("향기가득 샤인머스캣_" + i)
                        .originalPrice((int)(Math.random() * 10000) * 10)
                        .salePrice((int)(Math.random() * 10000) * 10)
                        .stockQuantity(10000)
                        .description("너무 맛있어요")
                        .category(Category.FRUIT_RICE)
                        .repImageUrl("/image/item/grape1.jpg")
//                        .salesCount((int)(Math.random() * 10000))
//                        .hit((int)(Math.random() * 10000))
                        .itemSaleStatus(ItemSaleStatus.ON_SALE)
                        .build();
                em.persist(item);

                ItemImage repImage = new ItemImage(item, "grape1", "grape1", "/image/item/grape1.jpg", true);
                em.persist(repImage);

                for(int j = 0; j < 3; j++) {
                    ItemImage itemImage = new ItemImage(item, "grape2", "grape2", "/image/item/grape2.jpg", false);
                    em.persist(itemImage);
                }

/*                Member member = Member.builder()
                        .socialType(SocialType.NAVER)
                        .memberStatus(MemberStatus.ACTIVE)
                        .memberName("tester_" + i)
                        .email("tester" + i + "@naver.com")
                        .address(new BaseEntity.Address())
                        .role(Role.USER)
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
                        .address(new BaseEntity.Address())
                        .orderItems(List.of(orderItem))
                        .orderStatus(OrderStatus.ORDERED)
                        .build();
                orderItem.setOrder(order);

                em.persist(order);*/
            }
        }
    }
}
