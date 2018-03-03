package org.fungo.share.bean;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by chenliguan on 2017/8/25 0025.
 */
public enum SHARE_PLATFROM {
    GOOGLEPLUS,
    GENERIC,
    SMS,
    EMAIL,
    SINA,
    QZONE,
    QQ,
    RENREN,
    WEIXIN,
    WEIXIN_CIRCLE,
    WEIXIN_FAVORITE,
    TENCENT,
    DOUBAN,
    FACEBOOK,
    FACEBOOK_MESSAGER,
    TWITTER,
    LAIWANG,
    LAIWANG_DYNAMIC,
    YIXIN,
    YIXIN_CIRCLE,
    INSTAGRAM,
    PINTEREST,
    EVERNOTE,
    POCKET,
    LINKEDIN,
    FOURSQUARE,
    YNOTE,
    WHATSAPP,
    LINE,
    FLICKR,
    TUMBLR,
    ALIPAY,
    KAKAO,
    DROPBOX,
    VKONTAKTE,
    DINGTALK,
    MORE;

    public static SHARE_MEDIA toMedia(SHARE_PLATFROM platfrom) {
        if(platfrom.toString().equals("QQ")) {
            return SHARE_MEDIA.QQ;
        } else if(platfrom.toString().equals("SMS")) {
            return SHARE_MEDIA.SMS;
        } else if(platfrom.toString().equals("GOOGLEPLUS")) {
            return SHARE_MEDIA.GOOGLEPLUS;
        } else if(!platfrom.toString().equals("GENERIC")) {
            if(platfrom.toString().equals("EMAIL")) {
                return SHARE_MEDIA.EMAIL;
            } else if(platfrom.toString().equals("SINA")) {
                return SHARE_MEDIA.SINA;
            } else if(platfrom.toString().equals("QZONE")) {
                return SHARE_MEDIA.QZONE;
            } else if(platfrom.toString().equals("RENREN")) {
                return SHARE_MEDIA.RENREN;
            } else if(platfrom.toString().equals("WEIXIN")) {
                return SHARE_MEDIA.WEIXIN;
            } else if(platfrom.toString().equals("WEIXIN_CIRCLE")) {
                return SHARE_MEDIA.WEIXIN_CIRCLE;
            } else if(platfrom.toString().equals("WEIXIN_FAVORITE")) {
                return SHARE_MEDIA.WEIXIN_FAVORITE;
            } else if(platfrom.toString().equals("TENCENT")) {
                return SHARE_MEDIA.TENCENT;
            } else if(platfrom.toString().equals("FACEBOOK")) {
                return SHARE_MEDIA.FACEBOOK;
            } else if(platfrom.toString().equals("FACEBOOK_MESSAGER")) {
                return SHARE_MEDIA.FACEBOOK_MESSAGER;
            } else if(platfrom.toString().equals("YIXIN")) {
                return SHARE_MEDIA.YIXIN;
            } else if(platfrom.toString().equals("TWITTER")) {
                return SHARE_MEDIA.TWITTER;
            } else if(platfrom.toString().equals("LAIWANG")) {
                return SHARE_MEDIA.LAIWANG;
            } else if(platfrom.toString().equals("LAIWANG_DYNAMIC")) {
                return SHARE_MEDIA.LAIWANG_DYNAMIC;
            } else if(platfrom.toString().equals("INSTAGRAM")) {
                return SHARE_MEDIA.INSTAGRAM;
            } else if(platfrom.toString().equals("YIXIN_CIRCLE")) {
                return SHARE_MEDIA.YIXIN_CIRCLE;
            } else if(platfrom.toString().equals("PINTEREST")) {
                return SHARE_MEDIA.PINTEREST;
            } else if(platfrom.toString().equals("EVERNOTE")) {
                return SHARE_MEDIA.EVERNOTE;
            } else if(platfrom.toString().equals("POCKET")) {
                return SHARE_MEDIA.POCKET;
            } else if(platfrom.toString().equals("LINKEDIN")) {
                return SHARE_MEDIA.LINKEDIN;
            } else if(platfrom.toString().equals("FOURSQUARE")) {
                return SHARE_MEDIA.FOURSQUARE;
            } else if(platfrom.toString().equals("YNOTE")) {
                return SHARE_MEDIA.YNOTE;
            } else if(platfrom.toString().equals("WHATSAPP")) {
                return SHARE_MEDIA.WHATSAPP;
            } else if(platfrom.toString().equals("LINE")) {
                return SHARE_MEDIA.LINE;
            } else if(platfrom.toString().equals("FLICKR")) {
                return SHARE_MEDIA.FLICKR;
            } else if(platfrom.toString().equals("TUMBLR")) {
                return SHARE_MEDIA.TUMBLR;
            } else if(platfrom.toString().equals("KAKAO")) {
                return SHARE_MEDIA.KAKAO;
            } else if(platfrom.toString().equals("DOUBAN")) {
                return SHARE_MEDIA.DOUBAN;
            } else if(platfrom.toString().equals("ALIPAY")) {
                return SHARE_MEDIA.ALIPAY;
            } else if(platfrom.toString().equals("MORE")) {
                return SHARE_MEDIA.MORE;
            } else if(platfrom.toString().equals("DINGTALK")) {
                return SHARE_MEDIA.DINGTALK;
            } else if(platfrom.toString().equals("VKONTAKTE")) {
                return SHARE_MEDIA.VKONTAKTE;
            } else if(platfrom.toString().equals("DROPBOX")) {
                return SHARE_MEDIA.DROPBOX;
            }
        }
        return null;
    }

    public static SHARE_PLATFROM toPlatfrom(SHARE_MEDIA shareMedia) {
        if(shareMedia.toString().equals("QQ")) {
            return SHARE_PLATFROM.QQ;
        } else if(shareMedia.toString().equals("SMS")) {
            return SHARE_PLATFROM.SMS;
        } else if(shareMedia.toString().equals("GOOGLEPLUS")) {
            return SHARE_PLATFROM.GOOGLEPLUS;
        } else if(!shareMedia.toString().equals("GENERIC")) {
            if(shareMedia.toString().equals("EMAIL")) {
                return SHARE_PLATFROM.EMAIL;
            } else if(shareMedia.toString().equals("SINA")) {
                return SHARE_PLATFROM.SINA;
            } else if(shareMedia.toString().equals("QZONE")) {
                return SHARE_PLATFROM.QZONE;
            } else if(shareMedia.toString().equals("RENREN")) {
                return SHARE_PLATFROM.RENREN;
            } else if(shareMedia.toString().equals("WEIXIN")) {
                return SHARE_PLATFROM.WEIXIN;
            } else if(shareMedia.toString().equals("WEIXIN_CIRCLE")) {
                return SHARE_PLATFROM.WEIXIN_CIRCLE;
            } else if(shareMedia.toString().equals("WEIXIN_FAVORITE")) {
                return SHARE_PLATFROM.WEIXIN_FAVORITE;
            } else if(shareMedia.toString().equals("TENCENT")) {
                return SHARE_PLATFROM.TENCENT;
            } else if(shareMedia.toString().equals("FACEBOOK")) {
                return SHARE_PLATFROM.FACEBOOK;
            } else if(shareMedia.toString().equals("FACEBOOK_MESSAGER")) {
                return SHARE_PLATFROM.FACEBOOK_MESSAGER;
            } else if(shareMedia.toString().equals("YIXIN")) {
                return SHARE_PLATFROM.YIXIN;
            } else if(shareMedia.toString().equals("TWITTER")) {
                return SHARE_PLATFROM.TWITTER;
            } else if(shareMedia.toString().equals("LAIWANG")) {
                return SHARE_PLATFROM.LAIWANG;
            } else if(shareMedia.toString().equals("LAIWANG_DYNAMIC")) {
                return SHARE_PLATFROM.LAIWANG_DYNAMIC;
            } else if(shareMedia.toString().equals("INSTAGRAM")) {
                return SHARE_PLATFROM.INSTAGRAM;
            } else if(shareMedia.toString().equals("YIXIN_CIRCLE")) {
                return SHARE_PLATFROM.YIXIN_CIRCLE;
            } else if(shareMedia.toString().equals("PINTEREST")) {
                return SHARE_PLATFROM.PINTEREST;
            } else if(shareMedia.toString().equals("EVERNOTE")) {
                return SHARE_PLATFROM.EVERNOTE;
            } else if(shareMedia.toString().equals("POCKET")) {
                return SHARE_PLATFROM.POCKET;
            } else if(shareMedia.toString().equals("LINKEDIN")) {
                return SHARE_PLATFROM.LINKEDIN;
            } else if(shareMedia.toString().equals("FOURSQUARE")) {
                return SHARE_PLATFROM.FOURSQUARE;
            } else if(shareMedia.toString().equals("YNOTE")) {
                return SHARE_PLATFROM.YNOTE;
            } else if(shareMedia.toString().equals("WHATSAPP")) {
                return SHARE_PLATFROM.WHATSAPP;
            } else if(shareMedia.toString().equals("LINE")) {
                return SHARE_PLATFROM.LINE;
            } else if(shareMedia.toString().equals("FLICKR")) {
                return SHARE_PLATFROM.FLICKR;
            } else if(shareMedia.toString().equals("TUMBLR")) {
                return SHARE_PLATFROM.TUMBLR;
            } else if(shareMedia.toString().equals("KAKAO")) {
                return SHARE_PLATFROM.KAKAO;
            } else if(shareMedia.toString().equals("DOUBAN")) {
                return SHARE_PLATFROM.DOUBAN;
            } else if(shareMedia.toString().equals("ALIPAY")) {
                return SHARE_PLATFROM.ALIPAY;
            } else if(shareMedia.toString().equals("MORE")) {
                return SHARE_PLATFROM.MORE;
            } else if(shareMedia.toString().equals("DINGTALK")) {
                return SHARE_PLATFROM.DINGTALK;
            } else if(shareMedia.toString().equals("VKONTAKTE")) {
                return SHARE_PLATFROM.VKONTAKTE;
            } else if(shareMedia.toString().equals("DROPBOX")) {
                return SHARE_PLATFROM.DROPBOX;
            }
        }
        return null;
    }

}
