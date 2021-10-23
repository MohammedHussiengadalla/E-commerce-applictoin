package com.example.test.Adabter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.Model.ProductModel;
import com.example.test.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private ArrayList<ProductModel> data;
    private Context context;

    private double total_cost = 0;
    private ArrayList<Integer> list_id = new ArrayList<>();


    SharedPreferences sharedPreferences;

    public CartAdapter(Context context, ArrayList<ProductModel> data) {
        this.data = data;
        this.context = context;

        getProductsids();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.cart_item, parent, false);
        }

        // get current item to be displayed
        final ProductModel currentItem = (ProductModel) getItem(position);

        // get the TextView for item name and item description
        ImageView product_image = convertView.findViewById(R.id.image_of_cart_product);
        TextView product_name = convertView.findViewById(R.id.name_of_cart_product);
        TextView product_price = convertView.findViewById(R.id.price_of__cart_product);
        Button remove = convertView.findViewById(R.id.remove_from_cart);
        Button add_quan = convertView.findViewById(R.id.increase_quantity);
        Button dec_quan = convertView.findViewById(R.id.decrase_quantity);
        final TextView item_quan = convertView.findViewById(R.id.quantity_at_cart);


        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();

               list_id.remove(new Double(currentItem.getPro_id()));

                SharedPreferences.Editor editor;
                Gson gson = new Gson();
                String json = gson.toJson(list_id);
                editor = sharedPreferences.edit();
                editor.putString("lastorder", json);

                editor.apply();

            }
        });


        add_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quan = Integer.parseInt(item_quan.getText().toString());
                double res_before = quan * currentItem.getPrice();
                total_cost = total_cost - res_before;
                quan++;
                item_quan.setText(String.valueOf(quan));

                double res_after = quan * currentItem.getPrice();
                total_cost += res_after;


            }
        });

        dec_quan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quan = Integer.parseInt(item_quan.getText().toString());
                double res_before = quan * currentItem.getPrice();
                total_cost = total_cost - res_before;

                if (quan > 1)
                    quan--;

                item_quan.setText(String.valueOf(quan));

                double res_after = quan * currentItem.getPrice();
                total_cost += res_after;


            }
        });


        if (currentItem.getProImage() != null) {
            byte[] image_byte = data.get(position).getProImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(image_byte, 0, image_byte.length);
            product_image.setImageBitmap(bmp);
        }

        product_name.setText(currentItem.getProName());
        product_price.setText(currentItem.getPrice() + " $");


        // returns the view for the current row
        return convertView;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public double getTotal_cost() {
        return total_cost;
    }


    private void getProductsids() {
        sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        String ids = sharedPreferences.getString("lastorder", null);
        if (ids != null) {
            Gson gson = new Gson();
            list_id=gson.fromJson(ids,ArrayList.class);

        }
    }
}