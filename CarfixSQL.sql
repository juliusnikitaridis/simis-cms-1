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
    member_id varchar
);

alter table carfix.vehicles owner to postgres;

create table carfix.service_request
(
    id varchar(255),
    date varchar(255),
    type varchar(255),
    vehicle_id varchar(255),
    member_id varchar(255),
    radius varchar(255),
    status varchar(255),
    current_odo_reading varchar(255),
    picture_data varchar,
    last_service_date varchar(255)
);

alter table carfix.service_request owner to postgres;

create table carfix.service_request_items
(
    id varchar,
    service_request_id varchar,
    service_request_option_id varchar
);

alter table carfix.service_request_items owner to postgres;

create table carfix.quote
(
    id varchar,
    request_for_service_id varchar,
    service_provider_id varchar,
    date varchar,
    total varchar
);

alter table carfix.quote owner to postgres;

create table carfix.quotation_item
(
    id varchar,
    quote_id varchar,
    part_number varchar,
    part_description varchar,
    item_total_price varchar
);

alter table carfix.quotation_item owner to postgres;

