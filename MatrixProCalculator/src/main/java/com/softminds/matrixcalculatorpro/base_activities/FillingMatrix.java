/*
 * Copyright (C) 2017 Ashar Khan <ashar786khan@gmail.com>
 *
 * This file is part of Matrix Calculator.
 *
 * Matrix Calculator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matrix Calculator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matrix Calculator.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.softminds.matrixcalculatorpro.base_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.softminds.matrixcalculatorpro.Type;
import com.softminds.matrixcalculatorpro.dialog_activity.CustomValueFiller;
import com.softminds.matrixcalculatorpro.R;
import com.softminds.matrixcalculatorpro.dialog_activity.DialogConfirmation;

import java.util.Random;

public class FillingMatrix extends AppCompatActivity {

    final String CustomValueKey = "com.softminds.matrixCalculator.CUSTOM_VALUE";
    final int RESULT=2;
    static int row,col;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDark=preferences.getBoolean("DARK_THEME_KEY",false);
        boolean SmartFit = preferences.getBoolean("SMART_FIT_KEY",false);

        if(isDark)
            setTheme(R.style.AppThemeDark_NoActionBar);
        else
            setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);

         Bundle bundle = getIntent().getExtras();
        col=bundle.getInt("COL");
        row=bundle.getInt("ROW");
        setContentView(R.layout.filler);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFill);
        setSupportActionBar(toolbar);


        CardView cardView = (CardView) findViewById(R.id.DynamicCard);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String string=sharedPreferences.getString("ELEVATE_AMOUNT","4");
        String string2=sharedPreferences.getString("CARD_CHANGE_KEY","#bdbdbd");

        cardView.setCardElevation(Integer.parseInt(string));
        cardView.setCardBackgroundColor(Color.parseColor(string2));

        CardView.LayoutParams params1= new CardView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        GridLayout gridLayout = new GridLayout(getApplicationContext());
        gridLayout.setRowCount(row);
        gridLayout.setColumnCount(col);
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                EditText editText = new EditText(getApplication());
                editText.setId(i*10+j);
                editText.setGravity(Gravity.CENTER);
                editText.setHint("A"+String.valueOf(i+1)+String.valueOf(j+1));
                editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL
                        |InputType.TYPE_NUMBER_FLAG_SIGNED);
                editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(getLenght())});
                if(SmartFit) {
                    editText.setWidth(ConvertTopx(CalculatedWidth(col)));
                    editText.setTextSize(SizeReturner(row, col,
                            PreferenceManager.getDefaultSharedPreferences
                                    (getApplicationContext()).
                                    getBoolean("EXTRA_SMALL_FONT", false)));
                }
                else{
                    editText.setWidth(ConvertTopx(62));
                    editText.setTextSize(SizeReturner(3,3,PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("EXTRA_SMALL_FONT", false)));
                }
                editText.setSingleLine();
                GridLayout.Spec Row = GridLayout.spec(i,1);
                GridLayout.Spec Col = GridLayout.spec(j,1);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(Row,Col);
                gridLayout.addView(editText,params);
            }
        }
        gridLayout.setLayoutParams(params1);
        cardView.addView(gridLayout);
        MakeType((Type)(getIntent().getExtras().getSerializable("TYPE")));
        if(GetMaximum()<GetMinimum()) {
            final Snackbar  snackbar;
            snackbar =Snackbar.make(findViewById(R.id.filling_matrix), R.string.Warning3, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
            snackbar.setAction(getString(R.string.action_settings), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                    Intent intent = new Intent(getApplicationContext(),SettingsTab.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }
    public int getLenght()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean v=preferences.getBoolean("EXTRA_SMALL_FONT",false);
        if(v)
        {
            if(!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("SMART_FIT_KEY",false))
                return 7;
            else
                return 8;
        }
        else
            return 6;
    }
    public float CalculatedWidth(int a) //should return size in dp
    {
        switch (a)
        {
            case 1 : return 72;
            case 2 : return 67;
            case 3 : return 62;
            case 4 : return 57;
            case 5 : return 52;
            case 6 : return 47;
            case 7 : return 44;
            case 8 : return 42;
            case 9 : return 40;

        }
        return 0;
    }
    public int SizeReturner(int r, int c,boolean b)
    {
            if (!b) {
                if (r > c) {
                    switch (r) {
                        case 1:
                            return 18;
                        case 2:
                            return 17;
                        case 3:
                            return 15;
                        case 4:
                            return 13;
                        case 5:
                            return 12;
                        case 6:
                            return 11;
                        case 7:
                            return 10;
                        case 8:
                            return 10;
                        case 9:
                            return 9;
                    }
                } else {
                    switch (c) {
                        case 1:
                            return 18;
                        case 2:
                            return 17;
                        case 3:
                            return 15;
                        case 4:
                            return 13;
                        case 5:
                            return 12;
                        case 6:
                            return 11;
                        case 7:
                            return 10;
                        case 8:
                            return 10;
                        case 9:
                            return 9;
                    }
                }
            } else {
                return SizeReturner(r, c, !b)-2; //If extra small is active then return two pixel less than original
        }
        return 0;
    }
    @Override
    public void onBackPressed(){
        if(!AllEmpty()){
            Bundle Info = new Bundle();
            int ResultCode=70;
            Info.putInt("MESSAGE",R.string.Warning4);
            Info.putInt("CONFIRM_TEXT",R.string.Yup);
            Info.putInt("RESULT_CODE",ResultCode);
            Info.putInt("CANCEL_TEXT",R.string.CancelCustomFill);
            Intent intent3 = new Intent(getApplicationContext(), DialogConfirmation.class);
            intent3.putExtras(Info);
            startActivityForResult(intent3,ResultCode);
    }
    else
    finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.custom_fill_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.ConfirmCreate :
                if(!MatrixError()){
                    float variable[][]=GetIntoFloat();
                    Bundle bundle=new Bundle();
                    bundle.putAll(getIntent().getExtras());
                    bundle.putSerializable("VALUES",variable);
                    Intent intnt=new Intent();
                    intnt.putExtras(bundle);
                    setResult(0,intnt);
                    finish();
                    return true;
                }
                else
                break;
            case R.id.Input1Empty :
                        EmptyInput(1);
                        return true;
            case R.id.ClearAll:
                ClearAllValues();
                return true;
            case R.id.FillAllZero :
                        EmptyInput(0);
                        return  true;
            case R.id.InputRandom :
                        InputRandomInt();
                        return true;
            case R.id.InputRandomFloat :
                        InputRandomFloat();
                        return true;
            case R.id.FillCustom :
                startActivityForResult(new Intent(getApplicationContext(),CustomValueFiller.class),RESULT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean MatrixError()
    {
        for(int i=0;i<row;i++)
            for (int j=0;j<col;j++)
            {
                EditText editText= (EditText) findViewById(i*10+j);
                if(editText.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplication(),R.string.Warning2,Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        return false;
    }
    public float[][] GetIntoFloat()
    {
        float var[][] = new float[row][col];
        for(int i=0;i<row;i++)
            for(int j=0;j<col;j++) {
                EditText editText = (EditText) findViewById(i*10+j);
                String p = editText.getText().toString();
                var[i][j]=Float.parseFloat(p);
            }
        return var;
    }
    public void EmptyInput(float p) {
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                EditText editText = (EditText) findViewById(i * 10 + j);
                if (editText.getText().toString().isEmpty()) {
                    editText.setText(String.valueOf(p));
                }
            }
    }
    @Override
    protected void onActivityResult(int requestcode,int resultCode,Intent data)
    {
        super.onActivityResult(requestcode,resultCode,data);
        if(resultCode==RESULT)
        {
            if(data.getFloatExtra(CustomValueKey,0)!=0)
            EmptyInput(data.getFloatExtra(CustomValueKey,0));
        }
        if(resultCode ==70) //When User Presses, back key
        {
            finish();
        }
    }
    public void MakeType(Type type)
    {
        switch (type)
        {
            case Null:
                for (int i = 0; i < row; i++)
                    for (int j = 0; j < col; j++) {
                        EditText editText = (EditText) findViewById(i * 10 + j);
                            editText.setText(String.valueOf(0.0));
                    }
                float variable[][]=GetIntoFloat();
                Bundle bundle=new Bundle();
                bundle.putAll(getIntent().getExtras());
                bundle.putSerializable("VALUES",variable);
                Intent intnt=new Intent();
                intnt.putExtras(bundle);
                setResult(0,intnt);
                finish();

                break;
            case Diagonal:
                for (int i = 0; i < row; i++)
                    for (int j = 0; j < col; j++) {
                        EditText editText = (EditText) findViewById(i * 10 + j);
                        if (i!=j) {
                            editText.setText(String.valueOf(0.0));
                        }
                    }
                break;
            case Identity:
                for (int i = 0; i < row; i++)
                    for (int j = 0; j < col; j++) {
                        EditText editText = (EditText) findViewById(i * 10 + j);
                        if(i==j)
                            editText.setText(String.valueOf(1.0));
                        else
                            editText.setText(String.valueOf(0.0));
                    }
                float vari[][]=GetIntoFloat();
                Bundle bundle2=new Bundle();
                bundle2.putAll(getIntent().getExtras());
                bundle2.putSerializable("VALUES",vari);
                Intent intn=new Intent();
                intn.putExtras(bundle2);
                setResult(0,intn);
                finish();
                break;
        }
    }
    public void InputRandomInt()
    {
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                EditText editText = (EditText) findViewById(i * 10 + j);
                    if(!NegativeAllowed())
                        editText.setText(String.valueOf(new Random().nextInt(GetMaximum()+1-GetMinimum())+GetMinimum()));
                    else {
                        int sign;
                        boolean rand = new Random().nextBoolean(); //Generates random boolean
                        if(rand)
                            sign = 1; //if true then set sign positive
                        else
                            sign = -1; //if flase set sign as negative
                        int random = new Random().nextInt(GetMaximum()+1-GetMinimum())+GetMinimum();
                        editText.setText(String.valueOf(random* sign));
                    }
            }
    }
    public void InputRandomFloat()
    {
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                EditText editText = (EditText) findViewById(i * 10 + j);
                if(!NegativeAllowed())
                editText.setText(String.valueOf(new Random().nextFloat()+new Random().nextInt(GetMaximum()-GetMinimum())+GetMinimum()));
                else {
                    int sign;
                    boolean rand = new Random().nextBoolean(); //Generates random boolean
                    if(rand)
                        sign = 1; //if true then set sign positive
                    else
                        sign = -1; //if flase set sign as negative
                    int random = new Random().nextInt(GetMaximum()+1-GetMinimum())+GetMinimum();
                    editText.setText(String.valueOf(random*sign));
                }
            }
    }
    public boolean NegativeAllowed()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return (sharedPreferences.getBoolean("SIGNED_RANDOM",false));
    }
    public int GetMaximum()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String  b=sharedPreferences.getString("MAX_INT","100");
        return Integer.parseInt(b);
    }
    public int GetMinimum()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String a=sharedPreferences.getString("MIN_INT_KEY","0");
        return Integer.parseInt(a);
    }
    public void ClearAllValues()
    {
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                EditText editText = (EditText) findViewById(i * 10 + j);
                editText.setText("");
            }
    }
    public boolean AllEmpty()
    {
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                EditText editText = (EditText) findViewById(i * 10 + j);
                if (!editText.getText().toString().isEmpty())
                    return false;

            }
        return true;
    }
    private int ConvertTopx(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return ((int)(dp * ((float)metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT));

    }

}
