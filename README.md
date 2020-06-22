### Type Body <br>

CREATE TYPE t_certificate AS ( <br>
name varchar(30), <br>
description varchar(999), <br>
date_of_creation date, <br>
date_of_modification date, <br>
duration_days integer, <br>
id_certificate integer, <br>
price double precision <br>
); <br>
<br>

### Function Body <br>
CREATE OR REPLACE FUNCTION return_t_certificate( text varchar) RETURNS SETOF t_certificate AS <br>
$$ <br>
DECLARE _result t_certificate; <br>
BEGIN <br>
FOR _result IN SELECT * FROM rest_api_basics.certificate WHERE rest_api_basics.certificate.name LIKE text <br>
LOOP <br>
RETURN NEXT _result; <br>
END LOOP; <br>
RETURN; <br>
END <br>
$$ LANGUAGE plpgsql; <br>
