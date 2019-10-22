package com.example.cameraview;

/**
 * Created by luyiling on 2019/4/6
 * <p>
 * TODO: remember to set the different req_code
 * 如果加入FetchFileImgView 和 FetchPhoneImgView 同時，
 * @request_code: 需要將將各自的設定 req_code
 *
 *
 * <IMPORTANT></IMPORTANT>
 */
public final class Constants {

    public static final String PACKAGE_NAME = "com.example.cameraview";

    public static final int IMAGE_FROM_GALLERY = 355;
    public static final int REQ_IMAGE_CAPTURE = 555;

    /**
     * @request_code: 需要將將各自的設定 req_code
     * 這個不一定要設，因為FetchFileImgView (data = null)和 FetchPhoneImgView(data != null)
     * 所以他們不會同時收到
     */
    public static final int FILE_FROM_GALLERY = 455;

    public static final int REQ_FILE_CAPTURE = 655;

}
