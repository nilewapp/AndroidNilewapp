/**
 *  Copyright 2013 Robert Welin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mooo.nilewapps.androidnilewapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableRequestEntity implements Parcelable {
    
    static class ParcelableNameValuePair implements Parcelable {

        private BasicNameValuePair content;

        public ParcelableNameValuePair(BasicNameValuePair content) {
            this.content = content;
        }
        
        public ParcelableNameValuePair(Parcel in) {
            String[] data = new String[2];
            in.readStringArray(data);
            content = new BasicNameValuePair(data[0], data[1]);
        }
        
        public BasicNameValuePair getContent() {
            return content;
        }

        public void setContent(BasicNameValuePair content) {
            this.content = content;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeStringArray(new String[]{content.getName(), content.getValue()});
        }
        
        public static final Parcelable.Creator<ParcelableNameValuePair> CREATOR
                = new Parcelable.Creator<ParcelableNameValuePair>() {
            public ParcelableNameValuePair createFromParcel(Parcel in) {
                return new ParcelableNameValuePair(in);
            }
        
            public ParcelableNameValuePair[] newArray(int size) {
                return new ParcelableNameValuePair[size];
            }
        };
        
    }

    private List<ParcelableNameValuePair> content;
    
    public List<BasicNameValuePair> getEntity() {
        List<BasicNameValuePair> entity = new ArrayList<BasicNameValuePair>(content.size());
        for (int i = 0; i < content.size(); i++) {
            entity.add(i, content.get(i).getContent());
        }
        return entity;
    }
    
    public ParcelableRequestEntity(List<BasicNameValuePair> entity) {
        content = new ArrayList<ParcelableNameValuePair>(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            content.add(i, new ParcelableNameValuePair(entity.get(i)));
        }
    }
    
    public ParcelableRequestEntity(Parcel in) {
        content = new ArrayList<ParcelableNameValuePair>();
        in.readList(content, null);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeList(content);
    }
    
    public static final Parcelable.Creator<ParcelableRequestEntity> CREATOR
            = new Parcelable.Creator<ParcelableRequestEntity>() {
        public ParcelableRequestEntity createFromParcel(Parcel in) {
            return new ParcelableRequestEntity(in);
        }
        
        public ParcelableRequestEntity[] newArray(int size) {
            return new ParcelableRequestEntity[size];
        }
    };

}
