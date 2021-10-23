package com.example.test.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.test.Adabter.ProductAdabter;
import com.example.test.Database.MyDatabase;
import com.example.test.Model.ProductModel;
import com.example.test.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private ListView listView;
    private ArrayList<ProductModel> products = new ArrayList<>();
    private ProductAdabter adabter;
    private MyDatabase database;


    ArrayAdapter adapter_cate;


    public Home() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        listView = view.findViewById(R.id.list_products);



        database = new MyDatabase(getActivity());

        getAllProduct();

        if (adabter==null) {
            adabter = new ProductAdabter(getContext(), 0, products);
            listView.setAdapter(adabter);
        }







        return view;
    }

    private void getAllProduct() {
        Cursor cursor = database.getProducts();

        products.clear();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                ProductModel productModel=new ProductModel(Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        cursor.getString(1), cursor.getBlob(2),
                        Double.parseDouble(cursor.getString(3)));
                productModel.setPro_id(Integer.parseInt(cursor.getString(0)));
                products.add(productModel);
                cursor.moveToNext();
            }
        }
    }




}
