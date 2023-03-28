create table cart (
                      id bigint not null auto_increment,
                      created_date datetime(6),
                      modified_date datetime(6),
                      created_by varchar(255),
                      modified_by varchar(255),
                      member_id bigint,
                      primary key (id)
) engine=InnoDB;
alter table cart
    add constraint FKix170nytunweovf2v9137mx2o
        foreign key (member_id)
            references member (id);

create table cart_item (
                           id bigint not null auto_increment,
                           count integer not null,
                           cart_id bigint,
                           item_id bigint,
                           primary key (id)
) engine=InnoDB;
alter table cart_item
    add constraint FK1uobyhgl1wvgt1jpccia8xxs3
        foreign key (cart_id)
            references cart (id);
alter table cart_item
    add constraint FKdljf497fwm1f8eb1h8t6n50u9
        foreign key (item_id)
            references item (id);

create table item (
                      id bigint not null auto_increment,
                      created_date datetime(6),
                      modified_date datetime(6),
                      created_by varchar(255),
                      modified_by varchar(255),
                      category_id bigint,
                      description varchar(255),
                      hit integer not null,
                      item_name varchar(255),
                      item_sale_status varchar(255),
                      original_price integer not null,
                      rep_image_url varchar(255),
                      review_count integer not null,
                      sale_price integer not null,
                      sales_count integer not null,
                      stock_quantity integer not null,
                      primary key (id)
) engine=InnoDB;

create table item_category (
                               id bigint not null auto_increment,
                               category_name varchar(255),
                               parent_id bigint,
                               primary key (id)
) engine=InnoDB;

create table item_image (
                            id bigint not null auto_increment,
                            is_rep_image bit not null,
                            item_image_url varchar(255),
                            original_file_name varchar(255),
                            renamed_file_name varchar(255),
                            item_id bigint,
                            primary key (id)
) engine=InnoDB;
alter table item_image
    add constraint FKta6kqet3u8mv95y7jwtgwqpys
        foreign key (item_id)
            references item (id);

create table member (
                        id bigint not null auto_increment,
                        created_date datetime(6),
                        modified_date datetime(6),
                        created_by varchar(255),
                        modified_by varchar(255),
                        address varchar(255),
                        detail_address varchar(255),
                        zip_code integer not null,
                        email varchar(255),
                        grade varchar(255),
                        member_name varchar(255),
                        member_status varchar(255),
                        phone_number varchar(255),
                        point integer not null,
                        role varchar(255),
                        social_type varchar(255),
                        primary key (id)
) engine=InnoDB;

create table order_item (
                            id bigint not null auto_increment,
                            created_date datetime(6),
                            modified_date datetime(6),
                            created_by varchar(255),
                            modified_by varchar(255),
                            count integer not null,
                            order_price integer not null,
                            rep_image_url varchar(255),
                            saving_point integer not null,
                            item_id bigint,
                            order_id bigint,
                            primary key (id)
) engine=InnoDB;
alter table order_item
    add constraint FKija6hjjiit8dprnmvtvgdp6ru
        foreign key (item_id)
            references item (id);
alter table order_item
    add constraint FKt4dc2r9nbvbujrljv3e23iibt
        foreign key (order_id)
            references orders (id);

create table orders (
                        id bigint not null auto_increment,
                        created_date datetime(6),
                        modified_date datetime(6),
                        created_by varchar(255),
                        modified_by varchar(255),
                        address varchar(255),
                        detail_address varchar(255),
                        zip_code integer not null,
                        order_status varchar(255),
                        payment_type varchar(255),
                        phone_number varchar(255),
                        total_payment_amount integer not null,
                        total_saving_point integer not null,
                        using_point integer not null,
                        member_id bigint,
                        primary key (id)
) engine=InnoDB;
alter table orders
    add constraint FKpktxwhj3x9m4gth5ff6bkqgeb
        foreign key (member_id)
            references member (id);

create table point (
                       id bigint not null auto_increment,
                       created_date datetime(6),
                       modified_date datetime(6),
                       created_by varchar(255),
                       modified_by varchar(255),
                       event_type varchar(255),
                       expire_date datetime(6),
                       point integer not null,
                       member_id bigint,
                       order_item_id bigint,
                       primary key (id)
) engine=InnoDB;
alter table point
    add constraint FKbet7cyy000fgj8pm7pbuuur46
        foreign key (member_id)
            references member (id);
alter table point
    add constraint FKjggyroyb6viy7tm6412qghlhh
        foreign key (order_item_id)
            references order_item (id);

create table reaction (
                          id bigint not null auto_increment,
                          created_date datetime(6),
                          modified_date datetime(6),
                          likes integer not null,
                          member_id varchar(255),
                          review_id bigint,
                          primary key (id)
) engine=InnoDB;
alter table reaction
    add constraint FKm5jit2nyye865qwms7hflu3ma
        foreign key (review_id)
            references review (id);

create table review (
                        id bigint not null auto_increment,
                        created_date datetime(6),
                        modified_date datetime(6),
                        content varchar(255),
                        likes integer not null,
                        item_id bigint,
                        member_id bigint,
                        primary key (id)
) engine=InnoDB;
alter table review
    add constraint FK6hb6qqehnsm7mvfgv37m66hd7
        foreign key (item_id)
            references item (id);
alter table review
    add constraint FKk0ccx5i4ci2wd70vegug074w1
        foreign key (member_id)
            references member (id);

