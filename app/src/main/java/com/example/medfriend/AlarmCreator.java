package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AlarmCreator extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker, saveAlarmButton, cancelAlarmButton;
    TextView txtDate, txtTime;
    private int year, month, day, hour, minute;
    int repeatinterval = -10;

    private static final String[] MEDICATION_NAMES = new String[]{
            "Acetaminophen","Acyclovir","Adalimumab","Albuterol","Albuterol Sulfate",
            "Alendronate Sodium","Allopurinol","Alprazolam","Amiodarone Hydrochloride",
            "Amitriptyline","Amlodipine Besylate","Amoxicillin","Amphetamine",
            "Amphetamine Aspartate","Anastrozole","Apixaban","Aripiprazole","Aspirin",
            "Atenolol","Atomoxetine Hydrochloride","Atorvastatin","Atropine Sulfate",
            "Azathioprine","Azelastine Hydrochloride","Azithromycin","Bacitracin",
            "Baclofen","Beclomethasone","Benazepril Hydrochloride","Benzonatate",
            "Benztropine Mesylate","Bimatoprost","Bisoprolol Fumarate","Brimonidine Tartrate",
            "Budesonide","Bumetanide","Bupropion","Buspirone Hydrochloride","Butalbital",
            "Calcitriol","Calcium","Canagliflozin","Carbamazepine","Carbidopa","Carisoprodol",
            "Carvedilol","Cefdinir","Cefuroxime","Celecoxib","Cephalexin",
            "Cetirizine Hydrochloride","Chlorhexidine","Chlorthalidone","Cholecalciferol",
            "Cilostazol","Ciprofloxacin","Citalopram","Clavulanate Potassium","Clindamycin",
            "Clobetasol Propionate","Clonazepam","Clonidine","Clopidogrel Bisulfate",
            "Codeine Phosphate","Colchicine","Cyanocobalamin","Cyclobenzaprine","Cyclosporine",
            "Dabigatran Etexilate Mesylate","Desogestrel","Desvenlafaxine","Dexamethasone",
            "Dexlansoprazole","Dexmethylphenidate Hydrochloride","Dextroamphetamine",
            "Dextroamphetamine Saccharate","Diazepam","Diclofenac","Dicyclomine Hydrochloride",
            "Digoxin","Diltiazem Hydrochloride","Diphenhydramine Hydrochloride",
            "Diphenoxylate Hydrochloride","Divalproex Sodium","Docusate","Donepezil Hydrochloride",
            "Dorzolamide Hydrochloride","Doxazosin Mesylate","Doxepin Hydrochloride","Doxycycline",
            "Drospirenone","Duloxetine","Dutasteride","Empagliflozin","Enalapril Maleate",
            "Enoxaparin Sodium","Epinephrine","Ergocalciferol","Erythromycin","Escitalopram Oxalate",
            "Esomeprazole","Estradiol","Estrogen","Eszopiclone","Ethinyl Estradiol","Etonogestrel",
            "Exenatide","Ezetimibe","Famotidine","Fenofibrate","Fentanyl","Ferrous Sulfate","Finasteride",
            "Flecainide Acetate","Fluconazole","Fluoxetine Hydrochloride","Fluticasone",
            "Fluticasone Propionate","Folic Acid","Formoterol","Formoterol Fumarate","Furosemide",
            "Gabapentin","Gemfibrozil","Glimepiride","Glipizide","Glyburide","Guaifenesin","Guanfacine",
            "Haloperidol","Hydralazine Hydrochloride","Hydrochlorothiazide","Hydrocodone Bitartrate",
            "Hydrocortisone","Hydromorphone Hydrochloride","Hydroxychloroquine Sulfate","Hydroxyzine",
            "Ibuprofen","Insulin Aspart","Insulin Detemir","Insulin Glargine","Insulin Human","Insulin Lispro",
            "Ipratropium","Ipratropium Bromide","Irbesartan","Isosorbide Mononitrate","Ketoconazole",
            "Ketorolac Tromethamine","Labetalol","Lamotrigine","Lansoprazole","Latanoprost",
            "Levetiracetam","Levocetirizine Dihydrochloride","Levodopa","Levofloxacin",
            "Levonorgestrel","Levothyroxine","Lidocaine","Linagliptin","Liothyronine Sodium",
            "Liraglutide","Lisdexamfetamine Dimesylate","Lisinopril","Lithium","Loperamide Hydrochloride",
            "Loratadine","Lorazepam","Losartan Potassium","Lovastatin","Lurasidone Hydrochloride",
            "Magnesium","Meclizine Hydrochloride","Medroxyprogesterone Acetate","Meloxicam",
            "Memantine Hydrochloride","Mesalamine","Metformin Hydrochloride","Methimazole","Methocarbamol",
            "Methotrexate","Methylcellulose","Methylphenidate","Methylprednisolone",
            "Metoclopramide Hydrochloride","Metoprolol","Metronidazole","Minocycline Hydrochloride",
            "Mirabegron","Mirtazapine","Modafinil","Mometasone","Mometasone Furoate","Montelukast",
            "Morphine","Mupirocin","Nadolol","Naphazoline Hydrochloride","Naproxen",
            "Nebivolol Hydrochloride","Neomycin","Niacin","Nifedipine","Nitrofurantoin",
            "Nitroglycerin","Norethindrone","Norgestimate","Nortriptyline Hydrochloride","Nystatin",
            "Ofloxacin","Olanzapine","Olmesartan Medoxomil","Olopatadine","Omega-3-acid Ethyl Esters",
            "Omeprazole","Ondansetron","Oseltamivir Phosphate","Oxcarbazepine","Oxybutynin","Oxycodone",
            "Pancrelipase Amylase","Pancrelipase Lipase","Pancrelipase Protease","Pantoprazole Sodium",
            "Paroxetine","Penicillin V","Pheniramine Maleate","Phentermine","Phenytoin","Pioglitazone",
            "Polyethylene Glycol 3350","Polymyxin B","Potassium","Pramipexole Dihydrochloride",
            "Pravastatin Sodium","Prazosin Hydrochloride","Prednisolone","Prednisone","Pregabalin",
            "Primidone","Prochlorperazine","Progesterone","Promethazine Hydrochloride",
            "Propranolol Hydrochloride","Pseudoephedrine Hydrochloride","Quetiapine Fumarate",
            "Quinapril","Rabeprazole Sodium","Raloxifene Hydrochloride","Ramipril","Ranitidine",
            "Ranolazine","Risperidone","Rivaroxaban","Rizatriptan Benzoate",
            "Ropinirole Hydrochloride","Rosuvastatin Calcium","Salmeterol Xinafoate","Sennosides",
            "Sertraline Hydrochloride","Sildenafil","Simvastatin","Sitagliptin Phosphate","Sodium",
            "Sodium Fluoride","Solifenacin Succinate","Sotalol Hydrochloride","Spironolactone",
            "Sucralfate","Sulfamethoxazole","Sumatriptan","Tadalafil","Tamoxifen Citrate",
            "Tamsulosin Hydrochloride","Telmisartan","Temazepam","Terazosin","Terbinafine",
            "Testosterone","Thyroid","Timolol","Timolol Maleate","Tiotropium","Tizanidine",
            "Tolterodine Tartrate","Topiramate","Torsemide","Tramadol Hydrochloride","Travoprost",
            "Trazodone Hydrochloride","Tretinoin","Triamcinolone","Triamterene","Trimethoprim",
            "Valacyclovir","Valsartan","Venlafaxine Hydrochloride","Verapamil Hydrochloride",
            "Vilazodone Hydrochloride","Vortioxetine Hydrobromide","Warfarin","Zolpidem Tartrate"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_creator);

        btnDatePicker = findViewById(R.id.datebutton);
        btnTimePicker = findViewById(R.id.btn_time);
        saveAlarmButton = findViewById(R.id.saveAlarmButton);
        cancelAlarmButton = findViewById(R.id.cancelAlarmButton);
        Spinner repeatSpinner = findViewById(R.id.repeatSpinner);

        final AutoCompleteTextView nameText = findViewById(R.id.autoCompleteName);
        ArrayAdapter<String> autoCompleteadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MEDICATION_NAMES);
        nameText.setAdapter(autoCompleteadapter);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.options_array, android.R.layout.simple_spinner_item);
// Set the layout to use for each dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        repeatSpinner.setAdapter(adapter);
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);
        year = 0;
        month = 0;
        day = 0;
        hour = 0;
        minute = 0;

        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repeatinterval = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        saveAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if (year == 0 || hour == 0) {
                String name = nameText.getText().toString();

                if (year == 0) {
                    Toast.makeText(AlarmCreator.this, "Please select a date and time", Toast.LENGTH_LONG).show();
                } else {

                    //TODO: move code into the fragment/homepage so that we can cancel the alarms there.
                    //this is the code to set the alarm for now.
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    GregorianCalendar calendar = new GregorianCalendar(year, month, day, hour, minute);
                    long time = calendar.getTimeInMillis();
                    Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, in, 0);
                    am.setRepeating(AlarmManager.RTC_WAKEUP, time, 86400000 / repeatinterval, pendingIntent);



                    Intent intent = new Intent();
                    intent.putExtra("year", year);
                    intent.putExtra("month", month + 1);
                    intent.putExtra("day", day);
                    intent.putExtra("hour", hour);
                    intent.putExtra("minute", minute);
                    intent.putExtra("Interval", repeatinterval);
                    intent.putExtra("name", name);
                    setResult(1, intent);
                    finish();
                }
            }
        });

        cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("year", year);
                intent.putExtra("month", month + 1);
                intent.putExtra("day", day);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                setResult(1, intent);
                finish();
            }
        });

        //btnTimePicker.setOnClickListener(this);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setTime(int hr, int min) {
        String PMAM = "";
        txtTime = findViewById(R.id.in_time);
        int hourtxt = (hr  > 12) ? (hr - 12) : (hr);
        PMAM = (hr > 12) ? "PM" : "AM";
        if (min < 10) {
            txtTime.setText(String.valueOf(hourtxt) + ":0" + String.valueOf(min) + PMAM);
        } else {
            txtTime.setText(String.valueOf(hourtxt) + ":" + String.valueOf(min) + PMAM);
        }
        hour = hr;
        minute = min;
    }

    public void setDate(int yr, int mnth, int dy) {
        txtDate = findViewById(R.id.in_date);
        txtDate.setText(String.valueOf(mnth+1) + "/" + String.valueOf(dy) + "/" + String.valueOf(yr) + " ");
        year = yr;
        month = mnth;
        day = dy;
    }
}
