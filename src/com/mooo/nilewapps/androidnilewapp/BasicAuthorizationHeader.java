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

import android.util.Base64;

/**
 * Specialisation of AuthorizationHeader using the Basic authentication
 * scheme, i.e. "Basic username:password".
 * @author nilewapp
 *
 */
public class BasicAuthorizationHeader extends AuthorizationHeader {

    public static final String SCHEME = "Basic";
    
    public BasicAuthorizationHeader(String user, String pass) {
        super(SCHEME, Base64.encodeToString(
                (user + ":" + pass).getBytes(),
                Base64.URL_SAFE|Base64.NO_WRAP));
    }
}
