package com.example.xueyuanzhang.weibo.model;

import java.util.List;

/**
 * Created by xueyuanzhang on 16/6/24.
 */
public class SourceWeiboClass {
        private String id;
        private String text;
        private String reposts_count;
        private String comments_count;
        private String attitudes_count;
        private String created_at;
        private String bmiddle_pic;
        private String original_pic;
        private List<PicUrl> picUrl;
        private UserClass user;

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getText() {
                return text;
        }

        public void setText(String text) {
                this.text = text;
        }

        public String getReposts_count() {
                return reposts_count;
        }

        public void setReposts_count(String reposts_count) {
                this.reposts_count = reposts_count;
        }

        public String getComments_count() {
                return comments_count;
        }

        public void setComments_count(String comments_count) {
                this.comments_count = comments_count;
        }

        public String getAttitudes_count() {
                return attitudes_count;
        }

        public void setAttitudes_count(String attitudes_count) {
                this.attitudes_count = attitudes_count;
        }

        public String getCreated_at() {
                return created_at;
        }

        public void setCreated_at(String created_at) {
                this.created_at = created_at;
        }

        public String getBmiddle_pic() {
                return bmiddle_pic;
        }

        public void setBmiddle_pic(String bmiddle_pic) {
                this.bmiddle_pic = bmiddle_pic;
        }

        public String getOriginal_pic() {
                return original_pic;
        }

        public void setOriginal_pic(String original_pic) {
                this.original_pic = original_pic;
        }

        public List<PicUrl> getPicUrl() {
                return picUrl;
        }

        public void setPicUrl(List<PicUrl> picUrl) {
                this.picUrl = picUrl;
        }

        public UserClass getUser() {
                return user;
        }

        public void setUser(UserClass user) {
                this.user = user;
        }
}
