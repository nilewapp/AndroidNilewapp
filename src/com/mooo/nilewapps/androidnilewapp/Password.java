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

import java.math.BigInteger;
import java.security.SecureRandom;

import android.util.Base64;

/**
 * A password generation utility
 * @author nilewapp
 *
 */
public class Password {
    
    /**
     * Generates a Base64 encoded randomly generated password
     * @param bits number of bits in password
     * @return
     */
    public static String generate(int bits) {
        SecureRandom sr = new SecureRandom();
        
        /* Generate password */
        byte[] password = new BigInteger(bits, sr).toString(64).getBytes();
        
        /* Encode password in Base64 and return */
        return new String(Base64.encode(password, Base64.DEFAULT));
    }
    
    /**
     * Generates a Base64 encoded randomly generated 256-bit password
     * @return
     */
    public static String generate() {
        return generate(256);
    }
}
