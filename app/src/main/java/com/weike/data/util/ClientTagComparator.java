package com.weike.data.util;

import com.weike.data.model.resp.GetClientTagListResp;

import java.util.Comparator;

public class ClientTagComparator implements Comparator<GetClientTagListResp.TagSub> {
    @Override
    public int compare(GetClientTagListResp.TagSub o1, GetClientTagListResp.TagSub o2) {
        return o1.sort.compareTo(o2.sort);
    }
}
