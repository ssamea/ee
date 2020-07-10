package com.example.graduation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BarChartActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;
    BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        initDatabase();
        chart = findViewById(R.id.barchart);


        mDatabase=FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference("Distribution_DB");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BarEntry> people_Data = new ArrayList();

                ArrayList labels = new ArrayList();// 식당라벨
                labels.add("TIP");
                labels.add("종합관");
                labels.add("Olive");
                labels.add("산융");


                for (DataSnapshot myData : dataSnapshot.getChildren()) {

                    Integer cnt= dataSnapshot.child("Tip").child("people_number").getValue(Integer.class);
                    Integer cnt2= dataSnapshot.child("JongHap").child("people_number").getValue(Integer.class);
                    Integer cnt3= dataSnapshot.child("Olive").child("people_number").getValue(Integer.class);
                    Integer cnt4= dataSnapshot.child("Sanyung").child("people_number").getValue(Integer.class);

                    Float SensorValue = Float.valueOf(cnt).floatValue();
                    Float SensorValue2 = Float.valueOf(cnt2).floatValue();
                    Float SensorValue3 = Float.valueOf(cnt3).floatValue();
                    Float SensorValue4 = Float.valueOf(cnt4).floatValue();

                    people_Data.add(new BarEntry(0.5f,SensorValue));
                    people_Data.add(new BarEntry(1.5f,SensorValue2));
                    people_Data.add(new BarEntry(2.5f,SensorValue3));
                    people_Data.add(new BarEntry(3.5f,SensorValue4));

                }

                Collections.sort(people_Data, new EntryXComparator());
                BarDataSet bardataset = new BarDataSet(people_Data, "인원 수");
                BarData data = new BarData(bardataset); // MPAndroidChart v3.X 오류 발생
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

                //x축 string으로 변환
                XAxis xAxis = chart.getXAxis();
                xAxis.setValueFormatter(new MyXAxisValueFormatter(labels)); //MyXAxisValueFormatter 커
                xAxis.setGranularity(1f);
                xAxis.setTextSize(13f);
                xAxis.setCenterAxisLabels(true);
                //xAxis.setLabelRotationAngle(-90);


                data.setValueTextSize(15f);
                data.setBarWidth(0.5f);

                chart.animateY(5000);
                chart.setData(data);
                chart.notifyDataSetChanged();
                chart.invalidate();



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BarChartActivity.this, "Fail to load post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Distribution_DB");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

}
