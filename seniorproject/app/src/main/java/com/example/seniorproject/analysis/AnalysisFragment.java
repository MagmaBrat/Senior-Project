package com.example.seniorproject.analysis;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seniorproject.R;
import com.example.seniorproject.SlidePageAdapter;
import com.example.seniorproject.trustdialog.TrustedDone;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalysisFragment extends Fragment {

    PieChart chart;
//    protected Typeface tfRegular;
//    protected Typeface tfLight;

    protected final String[] parties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.analysisfragment,container,false);
        chart = rootview.findViewById(R.id.analysispie);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);

//                        chart.setCenterTextTypeface(tfLight);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(R.color.backgroundo);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(0);

        chart.setHoleRadius(35f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        Legend l = chart.getLegend();
        l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);
        chart.setEntryLabelColor(Color.RED);
//                        chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f);
        setData(3,10f);
        ParseUser user=ParseUser.getCurrentUser();
        if (user!=null){
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("username",user.getUsername());
            ParseCloud.callFunctionInBackground("getanalysis", parameters, new FunctionCallback<Map<String, Object>>() {
                @Override
                public void done(Map<String, Object> mapObject, ParseException e) {
                    if (e == null) {
                        Log.i("monther",mapObject.toString());

                    }
                    else {
                        Log.i("monther",e.getMessage());
                    }
                }
            });
        }

        return rootview;
    }

    private void setData(int count, float range) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
                    parties[i % parties.length],
                    getResources().getDrawable(R.drawable.visa)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Average");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.argb(255,174,191,126));
        colors.add(Color.argb(255,178,187,165));
        colors.add(Color.argb(255,103,129,136));
        colors.add(Color.argb(255,201,179,140));
        //colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

}
