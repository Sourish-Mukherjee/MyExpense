package com.example.myexpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyCustomListAdapter extends ArrayAdapter<ExpenseDetails> {

    Context context;
    int resource;
    List<ExpenseDetails> list;
    public MyCustomListAdapter(Context context,int resource, List<ExpenseDetails> list)
    {
        super(context,resource,list);
        this.context= context;
        this.resource=resource;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_layout,null);
        TextView tv = view.findViewById(R.id.label);
        ImageView iv = view.findViewById(R.id.type_icon);

        ExpenseDetails expenseDetails = list.get(position);
        tv.setText(expenseDetails.getText());
        iv.setImageDrawable(context.getResources().getDrawable(expenseDetails.getImage()));
        return view;
    }
}
