package com.zjmxdz.domain.dto;

import com.zjmxdz.domain.TbaseUserinfo;
import lombok.Data;

@Data
public class TbaseUserinfoDto extends TbaseUserinfo {
    private String EQUALS_NAME;
    private String EQUALS_PASSWORD;
}
