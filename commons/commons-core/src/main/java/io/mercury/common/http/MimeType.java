package io.mercury.common.http;

public interface MimeType {

    //text
    String TEXT_PLAIN_UTF8 = "text/plain;charset=utf-8";
    String TEXT_HTML_UTF8 = "text/html;charset=utf-8";
    String TEXT_CSS_UTF8 = "text/css;charset=utf-8";
    String TEXT_JAVASCRIPT_UTF8 = "text/javascript;charset=utf-8";

    //application
    String APPLICATION_JSON_UTF8 = "application/json;charset=utf-8";
    String APPLICATION_XML_UTF8 = "application/xml;charset=utf-8";
    String APPLICATION_PDF = "application/pdf";
    String APPLICATION_OCTET_STREAM = "application/octet-stream";
    String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    //multipart
    String multipart_form_data = "multipart/form-data";

    //image
    String IMAGE_JPEG = "image/jpeg";
    String IMAGE_PNG = "image/png";
    String IMAGE_GIF = "image/gif";

    //audio
    String AUDIO_MPEG = "audio/mpeg";
    String AUDIO_WAV = "audio/wav";

    //video
    String VIDEO_MP4 = "video/mp4";
    String VIDEO_AVI = "video/avi";


}
