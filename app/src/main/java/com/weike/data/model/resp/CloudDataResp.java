package com.weike.data.model.resp;

import com.weike.data.base.BaseReq;

import java.sql.Date;
import java.util.List;


public class CloudDataResp extends BaseReq {



    public List<SubCloudDataResp> subCloudDataRespList;

    public class SubCloudDataResp{


        /**
         * id
         */
        private Long id;
        /**
         * 客户id
         */
        private  Long userId;
        /**
         * 文件路径
         */
        private String url;
        /**
         * 文件名称
         */
        private String fileName;
        /**
         * 存储空间
         */
        private String bucket;
        /**
         * 文件类型
         */
        private String fileType;
        /**
         * 文件类型
         */
        private double fileSize;

        /***
         * 创建时间
         */
        private Date createDate;

        /**
         * begin
         * @return
         */



        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public double getFileSize() {
            return fileSize;
        }

        public void setFileSize(double fileSize) {
            this.fileSize = fileSize;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

    }



}
