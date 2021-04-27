package com.wiqer.efkv.model.type;
//节省带宽
public enum MsgType {
    STRING((byte) 1),
    LIST((byte) 2),
    MAP((byte) 3),
    SET((byte) 4), //命中率
    SET_COUNT((byte) 5),
    HOT_MAP_COUNT((byte) 6),
    EMPTY((byte) 7),
    MQ((byte) 8),
    PONG((byte) 9),
    PING((byte) 10);
    private byte type;

    MsgType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public static MsgType get(byte type) {
        for (MsgType value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        return null;
    }

}
