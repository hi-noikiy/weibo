package com.gapcoder.weico.Message.FG;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gapcoder.weico.General.SysMsg;
import com.gapcoder.weico.General.URLService;
import com.gapcoder.weico.Index.Model.TitleModel;
import com.gapcoder.weico.Message.Adapter.CommAdapter;
import com.gapcoder.weico.Message.Message;
import com.gapcoder.weico.Message.Model.CommModel;
import com.gapcoder.weico.R;
import com.gapcoder.weico.Utils.Pool;
import com.gapcoder.weico.Utils.Token;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by suxiaohui on 2018/3/7.
 */

public class CommFG extends BaseFG {

    LinkedList<CommModel.InnerBean> data = new LinkedList<>();
    LinkedList<CommModel.InnerBean> tmp = new LinkedList<>();

    CommAdapter adapter;
    int cache = 10;
    int id = 0;

    @BindView(R.id.timeline)
    RecyclerView tl;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout rf;

    boolean flag=false;
    @Override
    View init(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comm_fg, container, false);
    }

    @Override
    public void CreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState, View v) {

        adapter = new CommAdapter(data, getActivity());
        tl.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        tl.setAdapter(adapter);
        rf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Refresh(1);
            }
        });
        rf.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Refresh(0);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if(!flag)
        {
            flag=true;
            rf.autoRefresh();
        }
    }

    void Refresh(final int flag) {


        if (flag == 1) {
            if (data.size() != 0) {
                id = data.get(0).getId();
            }
        } else {
            id = data.get(data.size() - 1).getId();
        }

        //((Message)getActivity()).getMessage();
        Pool.run(new Runnable() {
            @Override
            public void run() {

                String url = "comm.php?token=" + Token.token + "&flag=" + flag + "&id=" + id;
                final SysMsg m = URLService.get(url, CommModel.class);

                if (!check(m, rf)) {
                    return;
                }
                tmp = ((CommModel) m).getInner();

                if (flag == 1) {
                    for (int i = 0; i < tmp.size(); i++)
                        data.addFirst(tmp.get(tmp.size() - i - 1));
                    int n = data.size() - cache;
                    for (int i = 0; i < n; i++) {
                        data.removeLast();
                    }
                } else if(tmp.size()>0){
                    data.addAll(tmp);
                    int n = data.size() - cache;
                    for (int i = 0; i < n; i++) {
                        data.removeFirst();
                    }
                }

                for (int i = 0; i < data.size(); i++)
                    Log.i("tag", data.get(i).toString());
                Log.i("tag", data.toString());

                UI(new Runnable() {
                    @Override
                    public void run() {
                        if(rf.isRefreshing())
                            rf.finishRefresh(true);
                        if(rf.isLoading())
                            rf.finishLoadmore(true);

                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}

