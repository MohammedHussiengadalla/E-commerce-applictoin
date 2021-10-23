package com.example.test.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.test.Adabter.CartAdapter;
import com.example.test.Database.MyDatabase;
import com.example.test.Model.ProductModel;
import com.example.test.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static android.content.Context.MODE_PRIVATE;
/**
 * A simple {@link Fragment} subclass.
 */
public class Cart extends Fragment {

    private ListView cart_products;
    private CartAdapter adapter;
    private ArrayList<ProductModel> data = new ArrayList<>();
    EditText text;
    Button btn,confirm;
    private MyDatabase database;
    private SharedPreferences sharedPreferences;

    FusedLocationProviderClient fusedLocationProviderClient;

    TextView orignal_price,delivery_cost,total_cost;

    double cost=0;

    int PERMISSION_ID = 44;

    public Cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        btn = view.findViewById(R.id.apply_address);
        text = view.findViewById(R.id.add_address);
        cart_products = view.findViewById(R.id.cart_product);
        database = new MyDatabase(getContext());

        orignal_price=view.findViewById(R.id.order_price);
        delivery_cost=view.findViewById(R.id.delivery_cost);
        total_cost=view.findViewById(R.id.total_cost);
        confirm = view.findViewById(R.id.confirm_order);


        getProductsids();
        fusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(getContext().getApplicationContext());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext().getApplicationContext()
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(getActivity()
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }


            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sender_mail,sender_pass;
                SharedPreferences shared = view.getContext().getSharedPreferences("remember file",MODE_PRIVATE);
                 sender_mail = "abdulrhmanelsayed7@gmail.com";//(shared.getString("username", ""));
                sender_pass=(shared.getString("password", ""));

                Properties properties =new Properties();
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.starttls.enable","true");
                properties.put("mail.smtp.host","smtp.gmail.com");
                properties.put("mail.smtp.port","587");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender_mail,sender_pass);
                    }
                });

                try
                {
                    Message message=new MimeMessage(session);
                    message.setFrom(new InternetAddress(sender_mail));

                    message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(sender_mail));

                    message.setSubject("Order");

                    message.setText("Your Order Confirmed Successfully");

                    sendmail send=new sendmail();
                    send.execute(message);
                }
                catch (MessagingException e)
                {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }


    private void getProductsids() {
        sharedPreferences = this.getActivity().getSharedPreferences("cart", MODE_PRIVATE);
        String ids = sharedPreferences.getString("lastorder", null);
        if (ids != null) {
            Gson gson = new Gson();
            ArrayList id = gson.fromJson(ids, ArrayList.class);
            getCartProduct(id);


            adapter = new CartAdapter(getContext(), data);
            adapter.setTotal_cost(cost);
            cart_products.setAdapter(adapter);


            orignal_price.setText(String.valueOf(adapter.getTotal_cost()) + " $");
            delivery_cost.setText("20.0 $");
            total_cost.setText(cost + 20.0 + "$");
        }


    }

    private void getCartProduct(ArrayList<Integer> ids) {

        data.clear();
        for (int i = 0; i < ids.size(); i++) {
            Cursor cursor = database.getProductbyId(String.valueOf(ids.get(i)));
            if (cursor != null) {
                ProductModel productModel = new ProductModel(Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        cursor.getString(1), cursor.getBlob(2),
                        Double.parseDouble(cursor.getString(3)));
                productModel.setPro_id(Integer.parseInt(cursor.getString(0)));
                data.add(productModel);
                cost+=Double.parseDouble(cursor.getString(3));
            }
        }

    }






    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }

}
    @SuppressLint("MissingPermission")
    public void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if(location!=null)
                {
                    try {


                        Geocoder geocoder = new Geocoder(getContext().getApplicationContext(),
                                Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);
                        Toast.makeText(getContext().getApplicationContext(), addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                        text.setText(addresses.get(0).getAddressLine(0));

                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    private class sendmail extends AsyncTask<Message,String,String> {

       // ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  progressDialog=progressDialog.show(getContext().getApplicationContext()
            //        ,"Please wait","sending mail....",true,false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return  "seccess";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            progressDialog.dismiss();
            if(s.equals("success"))
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext().getApplicationContext());
                builder.setCancelable(false);
                builder.setTitle("success");
                builder.setMessage("mail send success.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        to_txt.setText("");
//                        title_txt.setText("");
//                        obj_txt.setText("");
                    }
                });
                builder.show();
            }
            else
                Toast.makeText(getContext().getApplicationContext(),"something went wrong ?",Toast.LENGTH_LONG).show();
        }
    }

}
