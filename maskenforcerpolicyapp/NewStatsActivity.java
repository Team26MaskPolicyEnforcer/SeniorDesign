package com.example.maskenforcerpolicyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.View;
import android.widget.*;
import android.os.Bundle;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NewStatsActivity extends AppCompatActivity {
    TextView progressTextView;
    TextView titleMasks;
    TextView titleTotal;
    TextView titlePercentage;
    ListView myListView;
    Context thisContext;
    ItemAdapter itemAdapter;
    ArrayList<Integer> maskList = new ArrayList<Integer>();
    ArrayList<Integer> totalPeopleList = new ArrayList<Integer>();
    ArrayList<Float> percentageList = new ArrayList<Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_stats);
        progressTextView = (TextView) findViewById(R.id.progressTextView);
        titleMasks = (TextView) findViewById(R.id.maskTitleTextView);
        titleTotal = (TextView) findViewById(R.id.totalTitleTextView);
        titlePercentage = (TextView) findViewById(R.id.PercentageTitleTextView);
        Resources res = getResources();
        progressTextView.setText("");
        myListView = (ListView) findViewById(R.id.myListView);
        thisContext = this;
        Button btn = (Button) findViewById(R.id.getDataBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maskList.clear();
                totalPeopleList.clear();
                percentageList.clear();
                GetData retrieveData = new GetData();
                retrieveData.execute("");
            }
    });
    }
        private class GetData extends AsyncTask<String, String, String> {
            String msg = "";
            static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            static final String DB_URL = "jdbc:mysql://100.64.2.155:3306/data";
            @Override
            protected void onPreExecute(){
                progressTextView.setText("Connecting to database...");
            }
            @Override
            protected String doInBackground(String... strings) {

                Connection conn = null;
                Statement stmt = null;

                try{
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, DBStrings.USERNAME, DBStrings.PASSWORD);
                    stmt = conn.createStatement();
                    String sql = "SELECT * FROM Stats";
                    ResultSet rs = stmt.executeQuery(sql);
                    while(rs.next()){
                        int masks = rs.getInt("Masks");
                        int total = rs.getInt("Total");
                        float percentage = (float) ((float) (masks)/(float)(total));
                        if (percentage > 1 || percentage < 0 || masks < 0 || total < 0 || (masks > total)){
                            msg = "Error found in inputs.";
                            System.exit(1);
                        }
                        maskList.add(masks);
                        totalPeopleList.add(total);
                        percentageList.add(percentage);

                    }
                    msg = "Process complete.";
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch(SQLException connError){
                    msg = "An exception was thrown for JDBC.";
                    connError.printStackTrace();
                } catch (ClassNotFoundException e) {
                    msg = "A class not found exception was thrown";
                    e.printStackTrace();
                }finally {
                    try{
                        if (stmt != null){
                            stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try{
                        if (conn != null){
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
            @Override
            protected void onPostExecute(String msg){
                progressTextView.setText((this.msg));
                if (maskList.size()>0 && totalPeopleList.size()>0 && percentageList.size() > 0){
                    itemAdapter = new ItemAdapter(thisContext, maskList, totalPeopleList, percentageList);
                    itemAdapter.notifyDataSetChanged();
                    myListView.setAdapter(itemAdapter);
                    myListView.invalidateViews();
                }
            }
        }
    }