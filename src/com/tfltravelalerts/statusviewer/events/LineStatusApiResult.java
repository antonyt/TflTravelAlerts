package com.tfltravelalerts.statusviewer.events;

import com.tfltravelalerts.ApiResult;
import com.tfltravelalerts.model.LineStatusUpdateSet;

public class LineStatusApiResult extends ApiResult<LineStatusUpdateSet> {

    public LineStatusApiResult(int statusCode, LineStatusUpdateSet data) {
        super(statusCode, data);
    }

}
