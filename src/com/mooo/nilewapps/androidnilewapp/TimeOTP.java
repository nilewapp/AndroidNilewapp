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

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * Provides time synchronised one time password functionality.
 * @author nilewapp
 *
 */
public class TimeOTP {
    
    /**
     * Returns a six digit password based on the current unix time truncated to
     * the last 30 seconds and a secret key.
     * @param secret Base64 encoded secret key
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws Exception
     */
    public static String generate(byte[] secret)
            throws NoSuchAlgorithmException, InvalidKeyException {
        return generate(secret, 30);
    }

    /**
     * Returns a six digit password based on the current unix time and a secret key.
     * @param secret Base64 encoded secret key
     * @param seconds time interval between different passwords being generated
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static String generate(byte[] secret, long seconds)
            throws NoSuchAlgorithmException, InvalidKeyException {
        /* Init hash algorithm with secret */
        Mac mac = Mac.getInstance("HmacSHA1");
        byte[] key = Base64.decode(secret, Base64.DEFAULT);
        
        SecretKeySpec keySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(keySpec);

        /* Get current unix timestamp truncated to the nearest 30 seconds */
        Long message = System.currentTimeMillis() / (seconds * 1000L);
        
        /* Hash message */
        byte[] hash = mac.doFinal(message.toString().getBytes());
        
        /* Get the last four bits of the hash */
        int offset = 0xF & hash[hash.length - 1];
        
        /* Get four bytes from the hash starting from the offset */
        byte[] truncatedHash = Arrays.copyOfRange(hash, offset, offset + 4);
        
        ByteBuffer wrapper = ByteBuffer.wrap(truncatedHash);
        int code = wrapper.getInt() & 0x7FFFFFFF % 1000000;
        
        return String.format(Locale.US, "%06d", code);
    }
}
