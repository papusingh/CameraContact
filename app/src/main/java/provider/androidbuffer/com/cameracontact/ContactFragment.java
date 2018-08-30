package provider.androidbuffer.com.cameracontact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by incred-dev
 * on 29/8/18.
 */

public class ContactFragment extends Fragment implements OnCall {

    private static final String TAG                     = "ContactFragment";
    private static final int CONTACT_PERMISSION_CODE    = 1001;
    private static final int CALL_PERMISSION_CODE        = 1002;

    private RecyclerView contactListView;
    private String callNum;
    private ContactTask contactTask;

    static ContactFragment newInstance() {

        return new ContactFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        contactListView = rootView.findViewById(R.id.contact_list);
        checkAndShowContacts();
        return rootView;
    }

    @Override
    public void onDestroyView() {

        contactTask.cancel(true);
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(this.getActivity(), R.string.call_perm_denied,
                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchAndShowContacts();
            } else {
                Toast.makeText(this.getActivity(), R.string.con_perm_denied,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCallClicked(String num) {

        checkAndMakeCall(num);
    }

    private void checkAndShowContacts() {

        Log.e(TAG, "open camera");

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "permission not granted");

            requestPermissions( //Method of Fragment
                    new String[]{Manifest.permission.READ_CONTACTS},
                    CONTACT_PERMISSION_CODE
            );
        } else {
            Log.e(TAG, "permission granted");
            fetchAndShowContacts();
        }
    }

    private void checkAndMakeCall(String callNum) {

        this.callNum = callNum;
        Log.e(TAG, "open camera");

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "permission not granted");

            requestPermissions( //Method of Fragment
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION_CODE
            );
        } else {
            Log.e(TAG, "permission granted");
            makeCall();
        }
    }


    private void fetchAndShowContacts() {

        contactTask = new ContactTask();
        contactTask.execute();
    }

    @SuppressWarnings("ConstantConditions")
    private List<ContactItem> getContacts() {

        ContentResolver cr = this.getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        List<ContactItem> contactList = new ArrayList<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    if (pCur != null && pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactList.add(new ContactItem(name, phone));
                    } else {
                        contactList.add(new ContactItem(name, ""));
                    }

                    pCur.close();
                }
            }
        }

        cur.close();

        return contactList;
    }

    private void makeCall() {

        Log.e(TAG, "make call: " + callNum);

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + callNum));
        startActivity(intent);
    }

    private void showContacts(List<ContactItem> contactList) {

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        contactListView.setLayoutManager(linearLayoutManager);
        contactListView.setAdapter(new ContactAdapter(contactList, this));
        contactListView.addItemDecoration(itemDecoration);
    }

    @SuppressLint("StaticFieldLeak")
    private class ContactTask extends AsyncTask<Void, Void, List<ContactItem>> {


        @Override
        protected List<ContactItem> doInBackground(Void... voids) {

            return getContacts();
        }

        @Override
        protected void onPostExecute(List<ContactItem> pairs) {

            showContacts(pairs);
        }
    }
}
