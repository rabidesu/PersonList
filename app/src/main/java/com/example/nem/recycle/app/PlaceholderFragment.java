package com.example.nem.recycle.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Фрагмент, отображающий список данных.
 */
public class PlaceholderFragment extends Fragment {
    RecycleAdapter adapter;
    RecyclerView recyclerView;
    private List<Person> persons = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setRetainInstance(true);

        //Восстанавливается состояние списка при изменении конфигурации, или, если данных нет, создается список.
        if (savedInstanceState != null){
            persons = (ArrayList<Person>)savedInstanceState.getSerializable("persons");
        } else {
            try {
                persons = new JsonParser().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        adapter = new RecycleAdapter(persons);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }


    public void sorting(Map<String, Boolean> param){
        Collections.sort(persons, new PersonComparator(param));
        adapter.notifyDataSetChanged();
    }

    /*
    * Компаратор для сортировки. В конструкторе передается map, где в качестве ключей по порядку
    * указаны параметры сортировки. В качестве значений используется boolean флаг, который
    * имеет значение true, если сортировка должа производиться по убыванию.
     */
    class PersonComparator implements Comparator<Person>{

        Map<String, Boolean> param;

        public PersonComparator(Map<String, Boolean> param){
            this.param = param;

        }
        @Override
        public int compare(Person lhs, Person rhs) {
            for (Map.Entry<String, Boolean> entry : param.entrySet()){
                switch (entry.getKey()){
                    case "Name": {
                        if (lhs.getName().equals(rhs.getName())) break;
                        if (entry.getValue()) return rhs.getName().compareTo(lhs.getName());
                        else return lhs.getName().compareTo(rhs.getName());
                    }
                    case "Age": {
                        if (lhs.getAge() == (rhs.getAge())) break;
                        if (entry.getValue()) return rhs.getAge()-lhs.getAge();
                        else return lhs.getAge()-rhs.getAge();
                    }
                    case "Active": {
                        if (lhs.isActive() == (rhs.isActive())) break;
                        if (entry.getValue()) return lhs.isActive() ? 1 : -1;
                        else return rhs.isActive() ? 1 : -1;
                    }
                    case "Id": {
                        if (lhs.getId().equals(rhs.getId())) break;
                        if (entry.getValue()) return rhs.getId().compareTo(lhs.getId());
                        else return lhs.getId().compareTo(rhs.getId());
                    }
                    case "E-mail": {
                        if (lhs.getEmail().equals(rhs.getEmail())) break;
                        if (entry.getValue()) return rhs.getEmail().compareTo(lhs.getEmail());
                        else return lhs.getEmail().compareTo(rhs.getEmail());
                    }
                    case "Registered": {
                        if (lhs.getRegistered().equals(rhs.getRegistered())) break;
                        if (entry.getValue()) return rhs.getRegistered().compareTo(lhs.getRegistered());
                        else return lhs.getRegistered().compareTo(rhs.getRegistered());
                    }
                    case "Phone": {
                        if (lhs.getPhone().equals(rhs.getPhone())) break;
                        if (entry.getValue()) return rhs.getPhone().compareTo(lhs.getPhone());
                        else return lhs.getPhone().compareTo(rhs.getPhone());
                    }
                    case "Address": {
                        if (lhs.getAddress().equals(rhs.getAddress())) break;
                        if (entry.getValue()) return rhs.getAddress().compareTo(lhs.getAddress());
                        else return lhs.getAddress().compareTo(rhs.getAddress());
                    }
                    case "Gender": {
                        if (lhs.getGender().equals(rhs.getGender())) break;
                        if (entry.getValue()) return rhs.getGender().compareTo(lhs.getGender());
                        else return lhs.getGender().compareTo(rhs.getGender());
                    }

                }
            }
            return 0;
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("persons", (ArrayList<Person>) persons);
    }
}

