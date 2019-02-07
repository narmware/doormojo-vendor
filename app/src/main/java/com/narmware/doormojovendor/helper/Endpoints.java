package com.narmware.doormojovendor.helper;

/**
 * Created by rohitsavant on 08/08/18.
 */

public class Endpoints {
    public static final String BASE_URL = "http://doormojo.com/api/";
    public static final String LOGIN_URL = BASE_URL + "vendor-login.php";


    //Variables
    public static final String LOGIN_USERNAME = "username";
    public static final String LOGIN_PASSWORD = "password";
    public static final String VENDOR_ID = "vendor_id";
    public static final String ORDER_ID = "order_id";
    public static final String VM_ID = "vm_id";
    public static final String STATUS = "status";
    public static final String COMMENT = "comment";
    public static final String GET_LIVE_ORDERS = BASE_URL+"live-order.php";
    public static final String GET_STATUS_WISE_ORDERS = BASE_URL+"vendor_book_service_status.php";
    public static final String SEND_COMPLETE_ORDER = BASE_URL+"vendor-change-status.php";
    public static final String GET_ALL_ORDERS = BASE_URL+"vendor_all_booking.php";
    public static final String START_ORDER = BASE_URL+"vendor_booking_start.php";
    public static final String STOP_ORDER = BASE_URL+"vendor_booking_end.php";
    public static final String GET_VENDOR_PROFILE = BASE_URL+"vendor_profile.php";

}
