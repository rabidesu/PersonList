package com.example.nem.recycle.app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;

/**
 * Фрагмент, отвечающий за выбор параметров сортировки. Сортировка может происходит по 1-3 параметрам.
 * Фрагмент содержит три spinner для выбора параметром. Изначально доступен выбор одного параметра, если он
 * указан - можно выбрать следующий параметр. Для каждого параметра установлены два RadioButton для выбора сортировки
 * по возрастанию или по убыванию.
 */
public class SortingFragment extends DialogFragment {

    private Spinner spinnerFirst;
    private Spinner spinnerSecond;
    private Spinner spinnerLast;
    private RadioButton firstSortDesc;
    private RadioButton secondSortDesc;
    private RadioButton lastSortDesc;
    private RadioButton firstSortAsc;
    private RadioButton secondSortAsc;
    private RadioButton lastSortAsc;

    //Параметры сортировки.
    private List<String> dataFirst = new ArrayList();
    {
        dataFirst.add("");
        dataFirst.add("Name");
        dataFirst.add("Active");
        dataFirst.add("Age");
        dataFirst.add("Registered");
        dataFirst.add("Gender");
        dataFirst.add("Id");
        dataFirst.add("E-mail");
        dataFirst.add("Address");
        dataFirst.add("Phone");
    }
    private List<String> dataSecond;
    private List<String> dataLast;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Sorting");

        View v = inflater.inflate(R.layout.fragment_sorting, container);
        spinnerFirst = (Spinner) v.findViewById(R.id.spinner_first);
        spinnerSecond = (Spinner) v.findViewById(R.id.spinner_second);
        spinnerSecond.setVisibility(View.INVISIBLE);
        spinnerLast = (Spinner) v.findViewById(R.id.spinner_last);
        spinnerLast.setVisibility(View.INVISIBLE);
        firstSortDesc = (RadioButton) v.findViewById(R.id.first_sort_desc);
        secondSortDesc = (RadioButton) v.findViewById(R.id.second_sort_desc);
        lastSortDesc = (RadioButton) v.findViewById(R.id.last_sort_desc);
        firstSortAsc = (RadioButton) v.findViewById(R.id.first_sort_asc);
        secondSortAsc = (RadioButton) v.findViewById(R.id.second_sort_asc);
        lastSortAsc = (RadioButton) v.findViewById(R.id.last_sort_asc);
        final RadioGroup firstGroup = (RadioGroup) v.findViewById(R.id.first_radio_group);
        final RadioGroup secondGroup = (RadioGroup) v.findViewById(R.id.second_radio_group);
        final RadioGroup lastGroup = (RadioGroup) v.findViewById(R.id.last_radio_group);

        /*
        * При нажатии на кнопку формируется LinkedHashMap параметров, где ключ - значение параметра,
        * а значение - флаг boolean, указывающий, что сортировка должна производиться по убыванию.
         */
        Button sortOk = (Button) v.findViewById(R.id.sort_ok);
        sortOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerFirst.getSelectedItemPosition() != 0) {
                    Map<String, Boolean> param = new LinkedHashMap<String, Boolean>();
                    param.put(spinnerFirst.getSelectedItem().toString(), firstSortDesc.isChecked());
                    if (spinnerSecond.getSelectedItemPosition() != 0){
                        param.put(spinnerSecond.getSelectedItem().toString(), secondSortDesc.isChecked());
                        if (spinnerLast.getSelectedItemPosition() != 0)
                            param.put(spinnerLast.getSelectedItem().toString(), lastSortDesc.isChecked());
                    }
                            ((MainActivity) getActivity()).fragment.sorting(param);
                    dismiss();
                }else {
                    Toast.makeText(getActivity(), "Select sorting parameters!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button sortCancel = (Button)v.findViewById(R.id.sort_cancel);
        sortCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /*
        * Адаптер формирует список для spinner. При выборе элемента - этот элемент удаляется из
        * списка остальных spinner, а сами эти spinner становятся доступными
         */
        spinnerFirst.setAdapter(new SpinAdapter(dataFirst));
        spinnerFirst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataSecond = new ArrayList<String>(dataFirst);
                if (!dataFirst.get(position).equals("")) {
                    setSortImage(dataFirst.get(position), firstSortAsc, firstSortDesc);
                    firstGroup.setVisibility(View.VISIBLE);
                    spinnerSecond.setVisibility(View.VISIBLE);
                    dataSecond.remove(dataFirst.get(position));
                    spinnerSecond.setAdapter(new SpinAdapter(dataSecond));

                } else {
                    firstGroup.setVisibility(View.INVISIBLE);
                    secondGroup.setVisibility(View.INVISIBLE);
                    lastGroup.setVisibility(View.INVISIBLE);
                    spinnerSecond.setVisibility(View.INVISIBLE);
                    spinnerLast.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSecond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataLast = new ArrayList<String>(dataSecond);
                if (!dataSecond.get(position).equals("")) {
                    setSortImage(dataSecond.get(position), secondSortAsc, secondSortDesc);
                    secondGroup.setVisibility(View.VISIBLE);
                    spinnerLast.setVisibility(View.VISIBLE);
                    dataLast.remove(dataSecond.get(position));
                    spinnerLast.setAdapter(new SpinAdapter(dataLast));
                } else {
                    secondGroup.setVisibility(View.INVISIBLE);
                    lastGroup.setVisibility(View.INVISIBLE);
                    spinnerLast.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerLast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!dataLast.get(position).equals("")){
                    setSortImage(dataLast.get(position), lastSortAsc, lastSortDesc);
                    lastGroup.setVisibility(View.VISIBLE);
                } else {
                    lastGroup.setVisibility(View.INVISIBLE);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    /*
    * Метод устанавливает картинки для RadioButton, отвечающих за сортировку по возрастанию или убыванию.
     */
    private void setSortImage(String item, RadioButton asc, RadioButton desc){
        Log.d("item", item);
        int ascDrawable;
        int descDrawable;
        switch (item){
            case "Gender": ascDrawable = R.drawable.female_img;
                descDrawable = R.drawable.male_img; break;
            case "Active": ascDrawable = R.drawable.online;
                descDrawable = R.drawable.offline; break;
            default: ascDrawable = R.drawable.sort_asc;
                descDrawable = R.drawable.sort_desc;
        }

        asc.setCompoundDrawablesWithIntrinsicBounds(ascDrawable, 0, 0, 0);
        desc.setCompoundDrawablesWithIntrinsicBounds(descDrawable, 0, 0, 0);
    }

    private class SpinAdapter extends ArrayAdapter<String> {

        public SpinAdapter(List<String> data) {

            super(getActivity(), android.R.layout.simple_spinner_item, data);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

    }
}
