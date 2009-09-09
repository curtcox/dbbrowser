package com.cve.web;

import com.cve.util.URIs;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class CompressedURIHandlerTest {

    static final String LONG_1 =
"http://localhost:8888/+/c8-qa/QA_C8_CORE/QA_C8_CORE.ACCOUNT/" +
"account_id+product_id+customer_id+account_status_id+account_name+username+password+date_entered+entered_by+" +
"setup+recurring+usage_fee+bill_period_id+parent_account_id+billing_start_date+renewal_date+" +
"original_product_id+original_account_id+one_time_billed+account_status_date+agency_id+" +
"lead_id+sales_rep_id+provisioner_id+note+radius_profile_id+cancel_status_id+status_reason_id+" +
"status_reason_description+domain_id+provider_id+brand_id+location_id+shipping_location_id+tax_id+" +
"promotion_code+promotion_code_original+free_billing_periods+free_billing_periods_orig+source_id+" +
"service_credits+tower_id+sector_id+renewal_promotion_code+future_cancel_date+dealer_location_id+" +
"offer_instance_id+sale_id+offer_product_id+currency_id/";

    static final String SMALL = "small";

    static final byte[] BYTES = CompressedURIHandler.unstring(SMALL);

    @Test
    public void small() {
        all("small");
    }

    @Test
    public void big1() {
        all(LONG_1);
    }

    @Test
    public void inflate() {
        byte[] reflated = CompressedURIHandler.inflate(CompressedURIHandler.deflate(BYTES));
        assertTrue(reflated.length > 0);
        equals(BYTES,reflated);
    }

    @Test
    public void deflate() {
        assertTrue(CompressedURIHandler.deflate(BYTES).length > 0);
    }

    @Test
    public void encode() {
        assertTrue(CompressedURIHandler.encode(BYTES).length > 0);
    }

    @Test
    public void decode() {
        byte[] recoded = CompressedURIHandler.decode(CompressedURIHandler.encode(BYTES));
        assertTrue(recoded.length > 0);
        equals(BYTES,recoded);
    }

    @Test
    public void longOf() {
        String longShort = CompressedURIHandler.longOf(CompressedURIHandler.shortOf(SMALL));
        assertTrue(longShort.length() > 0);
        equals(SMALL,longShort);
    }

    @Test
    public void shortOf() {
        assertTrue(CompressedURIHandler.shortOf(SMALL).length() > 0);
    }

    @Test
    public void shorUriEndingWithSlash() {
        String uri = CompressedURIHandler.shortURI(URIs.of("/foo/")).toString();
        assertTrue(uri.endsWith("/"));
        assertTrue(uri.startsWith("/z/"));
    }

    @Test
    public void shortUriNotEndingWithSlash() {
        String uri = CompressedURIHandler.shortURI(URIs.of("/foo/bar")).toString();
        assertFalse(uri.endsWith("/"));
        assertTrue(uri.startsWith("/z/"));
    }

    @Test
    public void longUris() {
        longUri("/foo");
        longUri("/foo/");
        longUri("/foo/bar");
        longUri("/foo/bar/");
        longUri(LONG_1);
    }

    void longUri(String uri) {
        String relonged = CompressedURIHandler.longURI(CompressedURIHandler.shortURI(URIs.of(uri))).toString();
        equals(uri,relonged);
    }

    void all(String s) {
        byte[] bytes = CompressedURIHandler.unstring(s);
        equals(bytes,CompressedURIHandler.decode(CompressedURIHandler.encode(bytes)));
        equals(bytes,CompressedURIHandler.inflate(CompressedURIHandler.deflate(bytes)));
        equals(s,CompressedURIHandler.longOf(CompressedURIHandler.shortOf(s)));
    }

    void equals(byte[] a, byte[] b) {
        assertEquals(a.length,b.length);
        for (int i=0; i<a.length; i++) {
            assertEquals(a[i],b[i]);
        }
    }

    void equals(String a, String b) {
        assertEquals(a,b);
    }
}
