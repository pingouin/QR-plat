package cn.com.pingouin.qr_plat.server.enums;

public enum ImageFileFormatEnum {

	JPG("jpg"),
	PNG("png"),
	GIF("gif"),
	;
	
	private final String key;

    public String getKey() {
        return key;
    }

    private ImageFileFormatEnum(String key) {
        this.key = key;
    }

    public boolean equalsValue(String value) {
        ImageFileFormatEnum target = null;
        ImageFileFormatEnum[] values = values();
        for (ImageFileFormatEnum enumInstance : values) {
            if (enumInstance.getKey().equals(value)) {
                target = enumInstance;
            }
        }
        return this.equals(target);
    }

    public String getKeyDescription() {
        if (this != null) {
            if (this.equals(JPG)) {
                return "JPG";
            } else if (this.equals(PNG)) {
                return "PNG";
            } else if (this.equals(GIF)) {
                return "GIF";
            } else {
            }
        }
        return "";
    }

    public String getKeyDescriptionByValue(String status) {
        ImageFileFormatEnum enumInstance = ImageFileFormatEnum.valueOf(status);
        return enumInstance.getKeyDescription();
    }

    @Override
    public String toString() {
        return getKey();
    }

    public static final ImageFileFormatEnum valueOfByKey(String key) {
        ImageFileFormatEnum[] values = values();
        for (ImageFileFormatEnum enumInstance : values) {
            if (enumInstance.equalsValue(key)) {
                return enumInstance;
            }
        }
        //
        return null;
    }
    
}
