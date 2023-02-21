create table carfix.categories
(
    id          varchar,
    description varchar,
    category    varchar
);

alter table carfix.categories
    owner to postgres;

create table carfix.brands
(
    id         varchar,
    brand_name varchar
);

alter table carfix.brands
    owner to postgres;

create table carfix.vehicles
(
    id               varchar,
    vin_number       varchar,
    registration     varchar,
    make             varchar,
    model            varchar,
    year             varchar,
    fuel_type        varchar,
    transmission     varchar,
    odo_reading      varchar,
    engine_code      varchar,
    member_id        varchar,
    service_history  varchar,
    maintenance_plan varchar
);

alter table carfix.vehicles
    owner to postgres;

create table carfix.service_request
(
    id                            varchar(255),
    created_date                  varchar(255),
    type                          varchar(255),
    vehicle_id                    varchar(255),
    member_id                     varchar(255),
    radius                        varchar(255),
    status                        varchar(255),
    current_odo_reading           varchar(255),
    picture_data                  varchar,
    last_service_date             varchar(255),
    additional_description        varchar,
    accepted_quote_id             varchar,
    confirmed_service_provider_id varchar,
    category_hash                 varchar,
    vehicle_brand_id              varchar,
    preferred_date                varchar,
    confirmed_date                varchar
);

alter table carfix.service_request
    owner to postgres;

create table carfix.quote
(
    id                     varchar,
    request_for_service_id varchar,
    service_provider_id    varchar,
    booking_date           varchar,
    total                  varchar,
    service_provider_name  varchar,
    status                 varchar,
    quantity               varchar,
    created_date           varchar
);

alter table carfix.quote
    owner to postgres;

create table carfix.quotation_item
(
    id               varchar,
    quote_id         varchar,
    part_number      varchar,
    part_description varchar,
    item_total_price varchar,
    quantity         varchar
);

alter table carfix.quotation_item
    owner to postgres;

create table carfix.service_provider
(
    id                   varchar,
    supported_brands     varchar,
    name                 varchar,
    services             varchar,
    certifications       varchar,
    user_id              varchar,

    address              varchar,
    about_us             varchar,
    logo_data            varchar,
    supported_categories varchar,
    accreditations       varchar,
    rating               integer,
    count                integer
);

alter table carfix.service_provider
    owner to postgres;

create table carfix.service_request_items
(
    id                 varchar,
    service_request_id varchar,
    category_id        varchar,
    item_description   varchar
);

alter table carfix.service_request_items
    owner to postgres;

