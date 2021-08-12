package com.glodon.groupsix.seckillprocess.models;

import lombok.Data;

@Data
public class SendRedis {
    private String messageId;
    private String key;
    private String value;
    private String createTime;

    @Override
    public String toString() {
        return "SendRedis{" +
                "messageId='" + messageId + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
