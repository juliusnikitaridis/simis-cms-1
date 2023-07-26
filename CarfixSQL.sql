create table carfix.categories
(
    id varchar not null
        constraint categories_pk
            primary key,
    description varchar,
    category varchar
);

alter table carfix.categories owner to postgres;

create unique index categories_id_uindex
    on carfix.categories (id);

create table carfix.brands
(
    id varchar,
    brand_name varchar
);

alter table carfix.brands owner to postgres;

create unique index brands_id_uindex
    on carfix.brands (id);

create table carfix.vehicles
(
    id varchar,
    vin_number varchar,
    registration varchar,
    make varchar,
    model varchar,
    year varchar,
    fuel_type varchar,
    transmission varchar,
    odo_reading varchar,
    engine_code varchar,
    member_id varchar,
    service_history varchar,
    maintenance_plan varchar,
    make_id varchar,
    picture_data varchar
);

alter table carfix.vehicles owner to postgres;

create table carfix.service_request
(
    id varchar(255),
    created_date varchar(255),
    type varchar(255),
    vehicle_id varchar(255),
    member_id varchar(255),
    radius varchar(255),
    status varchar(255),
    current_odo_reading varchar(255),
    picture_data varchar,
    last_service_date varchar(255),
    additional_description varchar,
    accepted_quote_id varchar,
    confirmed_service_provider_id varchar,
    category_hash varchar,
    vehicle_brand_id varchar,
    preferred_date varchar,
    confirmed_date varchar,
    job_number varchar,
    customer_reference varchar,
    technician varchar,
    service_advisor integer,
    picture_data_clob bytea,
    latitude varchar,
    longitude varchar
);

alter table carfix.service_request owner to postgres;

create unique index service_request_id_uindex
    on carfix.service_request (id);

create index service_request_vehicle_id_uindex
    on carfix.service_request (vehicle_id);

create index service_request_vehicle_id_uindex2
    on carfix.service_request (vehicle_id);

create index service_request_member_id_uindex
    on carfix.service_request (member_id);

create index service_request_radius_uindex
    on carfix.service_request (radius);

create index service_request_status_uindex
    on carfix.service_request (status);

create index service_request_confirmed_service_provider_id_uindex
    on carfix.service_request (confirmed_service_provider_id);

create index service_request_vehicle_brand_id_uindex
    on carfix.service_request (vehicle_brand_id);

create table carfix.quote
(
    id varchar,
    request_for_service_id varchar,
    service_provider_id varchar,
    booking_date varchar,
    total varchar,
    service_provider_name varchar,
    status varchar,
    quantity varchar,
    created_date varchar,
    vat varchar,
    sub_total varchar,
    date_type varchar,
    distance_from_sp varchar
);

alter table carfix.quote owner to postgres;

create index brands_request_for_service_id_uindex
    on carfix.quote (request_for_service_id);

create index quote_request_for_service_id_uindex
    on carfix.quote (request_for_service_id);

create index quote_status_uindex
    on carfix.quote (status);

create table carfix.quotation_item
(
    id varchar,
    quote_id varchar,
    part_number varchar,
    part_description varchar,
    item_total_price varchar,
    quantity varchar,
    item_type varchar,
    item_status varchar,
    labour_total varchar,
    parts_reason varchar,
    replacement_reason varchar,
    parts_total varchar,
    parts_picture varchar
);

alter table carfix.quotation_item owner to postgres;

create unique index quotation_item_id_uindex
    on carfix.quotation_item (id);

create index quotation_item_quote_id
    on carfix.quotation_item (quote_id);

create index quotation_item_item_status_uindex
    on carfix.quotation_item (item_status);

create table carfix.service_provider
(
    id varchar,
    supported_brands varchar,
    name varchar,
    services varchar,
    certifications varchar,
    user_id varchar,
    address varchar,
    about_us varchar,
    logo_data varchar,
    supported_categories varchar,
    accreditations varchar,
    rating integer,
    count integer,
    drop_off varchar,
    rmi varchar,
    operating_year varchar,
    operating_hours varchar,
    account_firstname varchar,
    account_lastname varchar,
    account_no varchar,
    account_branch varchar,
    account_bank varchar
);

alter table carfix.service_provider owner to postgres;

create index service_provider_id_uindex
    on carfix.service_provider (id);

create index service_provider_user_id_uindex
    on carfix.service_provider (user_id);

create table carfix.service_request_items
(
    id varchar,
    service_request_id varchar,
    category_id varchar,
    item_description varchar
);

alter table carfix.service_request_items owner to postgres;

create unique index service_request_itemss_id_uindex
    on carfix.service_request_items (id);

create index service_request_items_service_request_id_uindex
    on carfix.service_request_items (service_request_id);

create table carfix.device_token
(
    unique_id varchar,
    token varchar
);

alter table carfix.device_token owner to postgres;

create table carfix.payment_history
(
    transaction_amount varchar,
    merchant_transaction_no varchar,
    member_id varchar,
    service_provider_id varchar,
    id varchar,
    status varchar,
    raw_request varchar,
    date varchar,
    sysdate varchar,
    invoice_amount varchar,
    commission_amount varchar,
    batch_payment_status varchar,
    vat_amount varchar,
    batch_payment_amount varchar
);

alter table carfix.payment_history owner to postgres;

