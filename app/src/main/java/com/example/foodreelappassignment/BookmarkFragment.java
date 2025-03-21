package com.example.foodreelappassignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodreelappassignment.adapter.MyAdapter;
import com.example.foodreelappassignment.model.FoodData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookmarkFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<FoodData> bookmarkedList = new ArrayList<>();


    public BookmarkFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBookmarks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter(getContext(), bookmarkedList);
        recyclerView.setAdapter(adapter);

        return view;
    }


}