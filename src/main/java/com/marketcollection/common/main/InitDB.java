package com.marketcollection.common.main;

import com.marketcollection.common.entity.Address;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.member.MemberStatus;
import com.marketcollection.domain.member.Role;
import com.marketcollection.domain.member.SocialType;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@Profile("local")
@RequiredArgsConstructor
@Component
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
            initService.dbInit1();
    }

    @RequiredArgsConstructor
    @Transactional
    @Component
    public static class InitService {
        private final EntityManager em;

        public void dbInit1() {

            for(int i = 0; i < 100; i++) {

                Item item = Item.builder()
                        .itemName("향기가득 샤인머스캣_" + i)
                        .originalPrice(100000)
                        .salePrice((int)(Math.random() * 10000) * 10)
                        .stockQuantity(10000)
                        .description("너무 맛있어요")
                        .categoryId(1L)
                        .repImageUrl("/image/item/grape1.jpg")
                        .itemSaleStatus(ItemSaleStatus.ON_SALE)
                        .salesCount((int)(Math.random() * 10000))
                        .hit((int)(Math.random() * 10000))
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
                        .address(new Address())
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
                        .address(new Address())
                        .orderItems(List.of(orderItem))
                        .orderStatus(OrderStatus.ORDERED)
                        .build();
                orderItem.setOrder(order);

                em.persist(order);*/
            }
        }
    }
}
