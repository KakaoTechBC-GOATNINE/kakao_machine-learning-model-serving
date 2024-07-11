package com.example.kakao_mlms.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AllDto<T> {
    private T dataList;
    private PageInfo pageInfo;

    @Builder
    public AllDto(T dataList, PageInfo pageInfo) {
        this.dataList = dataList;
        this.pageInfo = pageInfo;
    }
}
