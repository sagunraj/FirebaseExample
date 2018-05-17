package np.com.sagunraj.firebaseexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

class MyAdapter extends BaseAdapter {
    List<DataModule> data;
    Context context;
    public MyAdapter(DashboardActivity dashboardActivity, List<DataModule> dataList) {
        data = dataList;
        context = dashboardActivity;
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
        view = LayoutInflater.from(context).inflate(R.layout.listview_data_activity, null);
        TextView name, rollno;
        name = view.findViewById(R.id.name);
        rollno = view.findViewById(R.id.rollno);
        rollno.setText(""+data.get(i).getRollno());
        name.setText(""+data.get(i).getName());
        return view;
    }
}
