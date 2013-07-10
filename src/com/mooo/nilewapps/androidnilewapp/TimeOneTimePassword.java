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
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

/**
 * Provides time synchronised one time password functionality
 * @author nilewapp
 *
 */
public class TimeOneTimePassword {
    
    /**
     * Returns a six digit password based on the current unix time and a secret key
     * @param secret Base64 encoded secret key
     * @throws Exception
     */
    public static String code(byte[] secret) throws Exception {
        /* Init hash algorithm with secret */
        Mac mac = Mac.getInstance("HmacSHA1");
        byte[] key = Base64.decode(secret, Base64.DEFAULT);
        Log.d("TimeOneTimePassword", "Secret: " + new String(secret) + ", key: " + new String(key));
        
        SecretKeySpec keySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(keySpec);

        /* Get current unix timestamp truncated to the nearest 30 seconds */
        Long message = System.currentTimeMillis() / 30000L;
        Log.d("TimeOneTimePassword", "Message: " + message);
        
        /* Hash message */
        byte[] hash = mac.doFinal(message.toString().getBytes());
        Log.d("TimeOneTimePassword", "Hash: '" + new String(hash) + "'");
        
        /* Get the last four bits of the hash */
        int offset = 0xF & hash[hash.length - 1];
        Log.d("TimeOneTimePassword", "Offset: " + offset);
        
        /* Get four bytes from the hash starting from the offset */
        byte[] truncatedHash = Arrays.copyOfRange(hash, offset, offset + 4);
        Log.d("TimeOneTimePassword", "Truncated hash: '" + new String(truncatedHash) + "'");
        
        /* Clear the most significant bit */
        truncatedHash[0] &= 0x7F;
        Log.d("TimeOneTimePassword", "Cleared top bit: '" + new String(truncatedHash) + "'");
        
        ByteBuffer wrapper = ByteBuffer.wrap(truncatedHash);
        int code = wrapper.getInt() % 1000000;
        
        return String.format(Locale.US, "%06d", code);
    }
}
