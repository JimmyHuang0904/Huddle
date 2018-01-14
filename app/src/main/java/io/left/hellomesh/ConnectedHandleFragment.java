package io.left.hellomesh;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectedHandleFragment extends Fragment {


    public ConnectedHandleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connected_handle, container, false);

        String[] users = {"John", "Amy", "Mark"};

        ListView listView = (ListView) view.findViewById(R.id.userList);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                users
        );

        listView.setAdapter(listViewAdapter);
        // Inflate the layout for this fragment
        return view;
    }

}
