package com.knoxpo.customedittext;

/**
 * Created by knoxpo on 9/9/17.
 */

public interface BaseSpan {

      /*  *//**
         * Set the selected state of the chip.
         *//*
        void setSelected(boolean selected);

        *//**
         * Return true if the chip is selected.
         *//*
        boolean isSelected();
*/
        /**
         * Get the text displayed in the chip.
         */
      //  CharSequence getDisplay();

       /* *//**
         * Get the text value this chip represents.
         *//*
        CharSequence getValue();*/

       /* *//**
         * Get the id of the contact associated with this chip.
         *//*
        long getContactId();

        *//**
         * Get the directory id of the contact associated with this chip.
         *//*
        Long getDirectoryId();

        *//**
         * Get the directory lookup key associated with this chip, or <code>null</code>.
         *//*
        String getLookupKey();

        *//**
         * Get the id of the data associated with this chip.
         *//*
        long getDataId();*/



        /**
         * Set the text in the edittextview originally associated with this chip
         * before any reverse lookups.
         */
        void setOriginalText(String text);

        /**
         * Get the text in the edittextview originally associated with this chip
         * before any reverse lookups.
         */
        CharSequence getOriginalText();


}
