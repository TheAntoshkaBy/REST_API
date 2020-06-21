##Type Body
CREATE TYPE t_certificate AS (
                                 name varchar(30),
                                 description varchar(999),
                                 date_of_creation date,
                                 date_of_modification date,
                                 duration_days integer,
                                 id_certificate integer,
                                 price double precision
                             );

##Function Body
CREATE OR REPLACE FUNCTION return_t_certificate( text varchar) RETURNS SETOF t_certificate AS
$$
DECLARE _result t_certificate;
BEGIN
    FOR _result IN SELECT * FROM rest_api_basics.certificate WHERE rest_api_basics.certificate.name LIKE text
        LOOP
            RETURN NEXT _result;
        END LOOP;
    RETURN;
END
$$ LANGUAGE plpgsql;
