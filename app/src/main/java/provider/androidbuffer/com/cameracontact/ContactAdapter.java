package provider.androidbuffer.com.cameracontact;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by incred-dev
 * on 29/8/18.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ContactItem> contactList;
    private OnCall onCall;
    private ArrayList<Integer> disabled;

    ContactAdapter(List<ContactItem> contactList, OnCall onCall) {

        Log.e("ContactAdapter", "" + contactList.size());
        this.contactList = contactList;
        this.onCall      = onCall;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View contactView = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ContactItem contactItem = contactList.get(position);
        ContactHolder contactHolder = (ContactHolder) holder;
        contactHolder.bindData(contactItem, onCall);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}

class ContactHolder extends RecyclerView.ViewHolder {

    private TextView    nameView;
    private TextView    numView;
    private Button      callButton;

    ContactHolder(View itemView) {
        super(itemView);

        nameView = itemView.findViewById(R.id.contact_name);
        numView  = itemView.findViewById(R.id.contact_num);
        callButton = itemView.findViewById(R.id.call_btn);
    }

    void bindData(final ContactItem contactItem, final OnCall onCall) {

        nameView.setText(contactItem.name);
        numView.setText(contactItem.num);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ContactHolder", "button: " + contactItem.num);
                if (contactItem.isDisabled()) {
                    Log.e("ContactHolder", "itemdisabled");

                } else {
                    onCall.onCallClicked(contactItem.num);
                    contactItem.setDisabled();
                }
            }
        });
    }
}