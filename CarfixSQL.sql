create table carfix.service_request(
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


create table carfix.service_request_items(
                                             id varchar,
                                             service_request_id varchar,
                                             service_request_option_id varchar
);



create table service_request_options(
                                        id varchar,
                                        category varchar,
                                        description varchar,
                                        part varchar
);

create table carfix.vehicles
(
    vehicle_id varchar,
    vin_number varchar,
    registration varchar,
    make varchar,
    model varchar,
    year varchar,
    fuel_type varchar,
    transmission varchar,
    odo_reading varchar,
    engine_code varchar
);

alter table carfix.vehicles owner to postgres;

