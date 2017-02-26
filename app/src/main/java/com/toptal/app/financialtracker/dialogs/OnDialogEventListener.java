package com.toptal.app.financialtracker.dialogs;

/**
 * This class handles some dialog events.
 * @author Felipe Porge Xavier - http://www.felipeporge.com
 */
public interface OnDialogEventListener {

    /**
     * Called when the user pressed cancel.
     * @param requestCode - Request code.
     */
    void onCancel(int requestCode);
}
