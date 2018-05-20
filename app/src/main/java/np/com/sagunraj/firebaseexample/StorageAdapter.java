package np.com.sagunraj.firebaseexample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

class StorageAdapter extends BaseAdapter {
    List<String> data;
    Context context;
    String website;
    public StorageAdapter(StorageActivity storageActivity, List<String> dataList) {
        data = dataList;
        context = storageActivity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.listview_storage_activity, null);
        Button downloadBtn = view.findViewById(R.id.downloadBtn);
        TextView link = view.findViewById(R.id.link);

        link.setText(data.get(i).toString());
        website = data.get(i).toString();

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                context.startActivity(i);
            }
        });
        return view;
    }
}
