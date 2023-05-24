create unique index brands_id_uindex
    on carfix.brands (id);

create unique index categories_id_uindex
    on carfix.categories (id);

create unique index quotation_item_id_uindex
    on carfix.quotation_item (id);

create  index quotation_item_quote_id
    on carfix.quotation_item (quote_id);

create index quotation_item_item_status_uindex
    on carfix.quotation_item (item_status);


create index brands_request_for_service_id_uindex
    on carfix.quote (request_for_service_id);

create index quote_request_for_service_id_uindex
    on carfix.quote (request_for_service_id);

create  index quote_status_uindex
    on carfix.quote (status);

create unique index service_provider_id_uindex
    on carfix.service_provider (id);

create index service_provider_user_id_uindex
    on carfix.service_provider (user_id);

create unique index service_request_id_uindex
    on carfix.service_request (id);

create index service_request_vehicle_id_uindex2
    on carfix.service_request (vehicle_id);

create index service_request_member_id_uindex
    on carfix.service_request (member_id);

create  index service_request_radius_uindex
    on carfix.service_request (radius);

create index service_request_status_uindex
    on carfix.service_request (status);

create index service_request_confirmed_service_provider_id_uindex
    on carfix.service_request (confirmed_service_provider_id);

create index service_request_vehicle_brand_id_uindex
    on carfix.service_request (vehicle_brand_id);

create unique index service_request_itemss_id_uindex
    on carfix.service_request_items (id);

create index service_request_items_service_request_id_uindex
    on carfix.service_request_items (service_request_id);
