package com.weike.data.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

public class ExpandableAdapter<T,R> extends BaseExpandableListAdapter {
    private List<T> groupArray;

    private List<List<R>> childArray;

    private Context context;

    private int groupBR;

    private int childBr;

    private int groupLayoutItem;

    private int childLayoutItem;

    public ExpandableAdapter(List<T> groupArray , List<List<R>> childArray , int groupLayoutItem , int childLayoutItem,int groupBR , int childBR){
        this.childArray = childArray;
        this.groupArray = groupArray;
        this.groupLayoutItem = groupLayoutItem;
        this.childLayoutItem = childLayoutItem;
        this.groupBR = groupBR;
        this.childBr = childBR;
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return childArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewDataBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), groupLayoutItem, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(groupBR, groupArray.get(groupPosition));
        return binding.getRoot();
    }




    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewDataBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), childLayoutItem, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(childBr, childArray.get(groupPosition).get(childPosition));
        return binding.getRoot();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
