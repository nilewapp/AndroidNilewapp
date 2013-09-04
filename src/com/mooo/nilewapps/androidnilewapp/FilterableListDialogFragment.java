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

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * A dialog that shows a filterable list of strings.
 * 
 * @author nilewapp
 *
 */
public class FilterableListDialogFragment extends DialogFragment {
    
    private EditText filterText = null;
    private ArrayAdapter<String> adapter = null;
    
    /**
     * Simple TextWatcher that performs filtering on key press
     */
    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
        }
        
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.getFilter().filter(s);
        }
    };

    FilterableListDialogListener listener;
    
    /**
     * Listens to list item click events.
     * @author nilewapp
     *
     */
    public interface FilterableListDialogListener {
        /**
         * Callback method that is called when an item is clicked
         * @param item the value of the item that was clicked
         */
        public void onDialogItemClick(String item);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater inflater = activity.getLayoutInflater();
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_filterable_list, null);

        adapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1);
        
        /* Set up the search field for filtering */
        filterText = (EditText) layout.findViewById(R.id.filter_text);
        filterText.addTextChangedListener(filterTextWatcher);
        
        /* Create the dialog */
        builder.setView(layout)
            .setSingleChoiceItems(adapter, STYLE_NORMAL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onDialogItemClick(adapter.getItem(which));
                }
            });
        
        final Dialog dialog = builder.create();
        
        /* Make the dialog resize when the soft keyboard is open */
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        
        return dialog;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /* Get target fragment and assert that it implements the listener */
        final Fragment frag = getTargetFragment();
        if (frag == null) {
            throw new RuntimeException(this.toString() + " target fragment not set.");
        }
        try {
            listener = (FilterableListDialogListener) frag;
        } catch (ClassCastException e) {
            throw new ClassCastException(frag.toString()
                    + " must implement " + FilterableListDialogListener.class.getName());
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        filterText.removeTextChangedListener(filterTextWatcher);
    }
    
    /**
     * Set the contents of the dialog.
     * @param items content list
     */
    public void setData(List<String> items) {
        adapter.clear();
        if (items != null) {
            for (String item : items) {
                adapter.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
