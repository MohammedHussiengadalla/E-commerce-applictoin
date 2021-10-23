package com.example.test.Adabter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test.Model.ProductModel;
import com.example.test.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ProductAdabter extends ArrayAdapter {

   private ArrayList<ProductModel>data;
   private ArrayList<ProductModel>data_copy;

  private LinkedHashSet<Integer> selected_items=new LinkedHashSet<>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ProductAdabter(@NonNull Context context, int resource, @NonNull ArrayList<ProductModel> objects) {
        super(context, resource, objects);
        data=objects;
        data_copy=objects;
        sharedPreferences=getContext().getSharedPreferences("cart",Context.MODE_PRIVATE);

        getSelectedProducts();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView==null)
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);


            ImageView product_image = convertView.findViewById(R.id.image_of_product);
            TextView product_name = convertView.findViewById(R.id.name_of_product);
            TextView product_price = convertView.findViewById(R.id.price_of_product);
            Button add_cart = convertView.findViewById(R.id.add_to_cart);


            if (data.get(position).getProImage() != null) {
                byte[] image_byte = data.get(position).getProImage();
                Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
                product_image.setImageBitmap(bmp);
            }

            product_name.setText(data.get(position).getProName());
            product_price.setText(data.get(position).getPrice() + " $");


            add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        selected_items.add(data.get(position).getPro_id());
                        Gson gson = new Gson();
                        String json = gson.toJson(selected_items);
                        editor = sharedPreferences.edit();
                        editor.putString("lastorder", json);
                        editor.apply();

                    Toast.makeText(getContext(), "product added", Toast.LENGTH_SHORT).show();
                }
            });


            return convertView;


        }

        public void filter(ArrayList<ProductModel>filterlist){

        data.clear();
        data.addAll(filterlist);
        notifyDataSetChanged();

        }





    private void getSelectedProducts() {

        String ids = sharedPreferences.getString("lastorder", null);
        selected_items.clear();
        if (ids != null) {
            Gson gson = new Gson();
            selected_items = gson.fromJson(ids, LinkedHashSet.class);
        }
    }

}
