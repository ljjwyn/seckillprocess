package com.glodon.groupsix.seckillprocess.models.vo;

import lombok.Data;

import java.util.Set;

@Data
public class GetCountAndUser {
    String count;
    Set<String> userNumber;
}
