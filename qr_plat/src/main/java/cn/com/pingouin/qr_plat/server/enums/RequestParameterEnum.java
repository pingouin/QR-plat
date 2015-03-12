package cn.com.pingouin.qr_plat.server.enums;


public enum RequestParameterEnum {
	REQUEST_URL("requestUrl"), //
	ENCODE_WIDTH("encodeWidth"),
	ENCODE_HEIGHT("encodeHeight"),
	ENCODE_COLOR("encodeColor"),
	ENCODE_LOGO_URL("encodeLogoUrl"),
    ;

    private final String key;

    public String getKey() {
        return key;
    }

    private RequestParameterEnum(String key) {
        this.key = key;
    }

    public boolean equalsValue(String value) {
        RequestParameterEnum target = null;
        RequestParameterEnum[] values = values();
        for (RequestParameterEnum enumInstance : values) {
            if (enumInstance.getKey().equals(value)) {
                target = enumInstance;
            }
        }
        return this.equals(target);
    }

    public String getKeyDescription() {
        if (this != null) {
            if (this.equals(REQUEST_URL)) {
                return "请求地址";
            } else if (this.equals(ENCODE_WIDTH)) {
                return "编码宽度";
            } else if (this.equals(ENCODE_HEIGHT)) {
                return "编码高度";
            } else if (this.equals(ENCODE_COLOR)) {
                return "编码颜色";
            } else if (this.equals(ENCODE_LOGO_URL)) {
                return "编码logo的url";
            } else {
            }
        }
        return "";
    }

    public String getKeyDescriptionByValue(String status) {
        RequestParameterEnum enumInstance = RequestParameterEnum.valueOf(status);
        return enumInstance.getKeyDescription();
    }

    @Override
    public String toString() {
        return getKey();
    }

    public static final RequestParameterEnum valueOfByKey(String key) {
        RequestParameterEnum[] values = values();
        for (RequestParameterEnum enumInstance : values) {
            if (enumInstance.equalsValue(key)) {
                return enumInstance;
            }
        }
        //
        return null;
    }
}
