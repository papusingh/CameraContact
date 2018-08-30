package provider.androidbuffer.com.cameracontact;

/**
 * Created by incred-dev
 * on 29/8/18.
 */

class ContactItem {

    final String name;
    final String num;
    private boolean disabled;

    ContactItem(String name, String num) {
        this.name = name;
        this.num  = num;
        disabled  = false;
    }

    void setDisabled() {
        disabled = true;
    }

    boolean isDisabled() {
        return disabled;
    }
}
