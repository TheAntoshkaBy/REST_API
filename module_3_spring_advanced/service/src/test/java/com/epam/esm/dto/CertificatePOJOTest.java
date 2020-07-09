package com.epam.esm.dto;

import com.epam.esm.entity.Certificate;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

class CertificatePOJOTest {

    private Certificate certificate;

    @Before
    public void init(){
        Certificate certificate = new Certificate(43L, "Football", "for You",
                432.0, new Date(), new Date(), 432, null);
    }

    @Test
    public void pojoToEntity() {
    }

    @Test
    public void entityToPOJO() {
    }
}