package com.epam.esm.constant;

public class SQLRequests {

    public static final String ADD_TAG_TO_CERTIFICATE = "INSERT INTO rest_api_basics.many_certificates_to_many_tags\n" +
            "VALUES (:id_certificate, :id_tag)";

    public static final String DELETE_TAG_FROM_CERTIFICATE =
            "DELETE FROM rest_api_basics.many_certificates_to_many_tags WHERE id_certificate = :id_certificate " +
                    "AND id_tag = :id_tag";

    public static final String FIND_ALL_CERTIFICATES_WHERE_ID_MORE_THAN_PARAMETER =
            "SELECT * FROM rest_api_basics.certificate WHERE id_certificate > :id";

    public static final String FIND_CERTIFICATE_BY_TAG_NAME =
            "SELECT name, description, date_of_creation, " +
                    "date_of_modification,duration_days,certificate.id_certificate,price " +
                    "FROM rest_api_basics.many_certificates_to_many_tags\n" +
                    "INNER JOIN rest_api_basics.certificate " +
                    "ON many_certificates_to_many_tags.id_certificate = certificate.id_certificate\n" +
                    "INNER JOIN rest_api_basics.tag " +
                    "ON many_certificates_to_many_tags.id_tag = tag.id_tag\n" +
                    "AND tag_name LIKE :name " +
                    "GROUP BY certificate.id_certificate";

    public static final String FIND_CERTIFICATE_BY_ID =
            "select * from rest_api_basics.certificate where id_certificate = :id";

    public static final String FIND_ALL_CERTIFICATES = "select * from rest_api_basics.certificate";

    public static final String FIND_ALL_CERTIFICATES_BY_DATE =
            "select * from rest_api_basics.certificate order by (rest_api_basics.certificate.date_of_creation)";

    public static final String ADD_CERTIFICATE = "INSERT INTO rest_api_basics.certificate (name, " +
            "description, " +
            "date_of_creation, " +
            "date_of_modification, " +
            "duration_days, " +
            "price, " +
            "id_certificate) " +
            "VALUES (:name,:description,:date_of_creation,:date_of_modification,:duration_days,:price, DEFAULT )";

    public static final String UPDATE_CERTIFICATE = "UPDATE rest_api_basics.certificate SET " +
            "name = :name, " +
            "description = :description, " +
            "date_of_creation = :date_of_creation, " +
            "date_of_modification = :date_of_modification, " +
            "duration_days = :duration_days, " +
            "price = :price " +
            "WHERE id_certificate = :id";

    public static final String DELETE_CERTIFICATE =
            "DELETE FROM rest_api_basics.certificate WHERE id_certificate = :id";

    public static final String FIND_TAGS_BELOW_CONCRETE_CERTIFICATE = "SELECT tag.id_tag, tag.tag_name\n" +
            "FROM rest_api_basics.many_certificates_to_many_tags\n" +
            "RIGHT JOIN rest_api_basics.tag ON many_certificates_to_many_tags.id_tag = tag.id_tag\n" +
            "WHERE id_certificate = :id";

    public static final String FIND_ALL_TAGS = "select * from rest_api_basics.tag";

    public static final String FIND_TAG_BY_ID = "select * from rest_api_basics.tag where id_tag = :id";

    public static final String ADD_TAG =
            "INSERT INTO rest_api_basics.tag (tag_name, id_tag) VALUES (:tag_name, DEFAULT )";

    public static final String DELETE_TAG_BY_ID = "DELETE FROM rest_api_basics.tag where id_tag = :id";

    public static final String FIND_BY_PART_OF_NAME = "SELECT * FROM return_t_certificate(:text)";

    public static final String DELETE_ALL_CERTIFICATES = "DELETE FROM rest_api_basics.certificate";

    public static final String DELETE_ALL_TAGS = "DELETE FROM rest_api_basics.tag";

    public static final String DELETE_ALL_RELATIONSHIPS =
            "DELETE FROM rest_api_basics.many_certificates_to_many_tags;";

    public static final String ADD_NEW_TAG_TO_CERTIFICATE_TRANSACTION = "DO $$\n" +
            "DECLARE\n" +
            "tag_id_to_set int;\n" +
            "BEGIN\n" +
            "INSERT INTO rest_api_basics.tag (tag_name, id_tag) VALUES (:tag_name, DEFAULT) RETURNING id_tag INTO tag_id_to_set;\n" +
            "INSERT INTO rest_api_basics.many_certificates_to_many_tags VALUES (:id_certificate, tag_id_to_set);\n" +
            "COMMIT;\n" +
            "END $$";
}
