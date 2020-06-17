package com.epam.esm.dao.constant;

public class SQLRequests {
    public static final String GET_CERTIFICATE =
            "select * from rest_api_basics.certificate where id_certificate = :id";
    public static final String ADD_TAG_TO_CERTIFICATE = "INSERT INTO rest_api_basics.many_certificates_to_many_tags\n" +
            "VALUES (:id_certificate, :id_tag)";
    public static final String DELETE_TAG_FROM_CERTIFICATE =
            "DELETE FROM rest_api_basics.many_certificates_to_many_tags WHERE id_certificate = :id_certificate " +
                    "AND id_tag = :id_tag";
    public static final String GET_ALL_CERTIFICATES_WHERE_ID_MORE_THAN_PARAMETER =
            "SELECT * FROM rest_api_basics.certificate WHERE id_certificate > :id";
    public static final String FIND_CERTIFICATE_BY_TAG =
            "SELECT name, description, date_of_creation, " +
                    "date_of_modification,duration_days,certificate.id_certificate,price " +
                    "FROM rest_api_basics.many_certificates_to_many_tags\n" +
                    "JOIN rest_api_basics.certificate " +
                    "ON many_certificates_to_many_tags.id_certificate = certificate.id_certificate\n" +
                    "JOIN rest_api_basics.tag " +
                    "ON many_certificates_to_many_tags.id_tag = tag.id_tag\n" +
                    "WHERE certificate.id_certificate = many_certificates_to_many_tags.id_certificate\n" +
                    "AND tag_name LIKE :name " +
                    "GROUP BY name, description, date_of_creation, " +
                    "date_of_modification,duration_days,certificate.id_certificate,price";

    public static final String GET_CERTIFICATE_BY_ID =
            "select * from rest_api_basics.certificate where id_certificate = :id";
    public static final String GET_ALL_CERTIFICATES = "select * from rest_api_basics.certificate";

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

    public static final String GET_TAGS_BELOW_CONCRETE_CERTIFICATE = "SELECT tag.id_tag, tag.tag_name\n" +
            "FROM rest_api_basics.many_certificates_to_many_tags\n" +
            "RIGHT JOIN rest_api_basics.tag ON many_certificates_to_many_tags.id_tag = tag.id_tag\n" +
            "WHERE id_certificate = :id";

    public static final String GET_ALL_TAGS = "select * from rest_api_basics.tag";
    public static final String GET_TAG_BY_ID = "select * from rest_api_basics.tag where id_tag = :id";
    public static final String ADD_TAG =
            "INSERT INTO rest_api_basics.tag (tag_name, id_tag) VALUES (:tag_name, DEFAULT )";
    public static final String DELETE_TAG_BY_ID = "DELETE FROM rest_api_basics.tag where id_tag = :id";

}
