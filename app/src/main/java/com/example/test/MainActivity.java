package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adaptor.CaetAdapter;
import Adaptor.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CaetAdapter.RefreshPriceInterface {

    private LinearLayout top_bar;
    private ListView listview;
    private TextView price;
    private TextView delete;
    private TextView tv_go_to_pay;
    private TextView shopcar_num;
    private List<User> goodsList;
    View layout=null;
    private List<HashMap<String,String>> listmap=new ArrayList<>();
    private CaetAdapter adapter;

    private double totalPrice = 0.00;
    private int totalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mianactivity);
        initView();
    }

    private void initView() {
        top_bar = (LinearLayout) findViewById(R.id.top_bar);
        listview = (ListView) findViewById(R.id.listview);
        price = (TextView) findViewById(R.id.tv_total_price);
        shopcar_num=findViewById(R.id.shopcar_num);
        tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);
        tv_go_to_pay.setOnClickListener(this);
        initDate();
        adapter = new CaetAdapter(MainActivity.this, listmap);
        listview.setAdapter(adapter);
        adapter.setRefreshPriceInterface(this);
        priceControl(adapter.getPitchOnMap());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.all_chekbox:
//                AllTheSelected();
//                break;
//            case R.id.tv_delete:
//                checkDelete(adapter.getPitchOnMap());
//                break;
            case R.id.tv_go_to_pay:
                if(totalCount<=0){
                    Toast.makeText(this,"请选择要付款的商品~",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"付款成功",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    /**
     * 数据
     */
    private void initDate() {
        for(int i=0;i<10;i++){
            HashMap<String,String> map=new HashMap<>();
            map.put("id",i+"");
            map.put("name",i+"name");
            map.put("type",i+"type");
            map.put("price",i+"");
            map.put("count",i+"");
            listmap.add(map);
        }
    }

    @Override
    public void refreshPrice(HashMap<String, Integer> pitchOnMap) {
        priceControl(pitchOnMap);
    }

    /**
     * 控制价格展示总价
     */
    private void priceControl(Map<String, Integer> pitchOnMap){
        totalCount = 0;
        totalPrice = 0.00;
        for(int i=0;i<listmap.size();i++){
                totalCount=totalCount+Integer.valueOf(listmap.get(i).get("count"));
                double goodsPrice=Integer.valueOf(listmap.get(i).get("count"))*Double.valueOf(listmap.get(i).get("price"));
                totalPrice=totalPrice+goodsPrice;
        }
        price.setText(" ¥ "+totalPrice);
        shopcar_num.setText("共"+pitchOnMap.size()+"件商品");
        tv_go_to_pay.setText("付款("+totalPrice+")");
    }

    /**
     * 删除 控制价格展示总价
     * @param map
     */
    private void checkDelete(Map<String,Integer> map){
        List<HashMap<String,String>> waitDeleteList=new ArrayList<>();
        Map<String,Integer> waitDeleteMap =new HashMap<>();
        for(int i=0;i<listmap.size();i++){
            if(map.get(listmap.get(i).get("id"))==1){
                waitDeleteList.add(listmap.get(i));
                waitDeleteMap.put(listmap.get(i).get("id"),map.get(listmap.get(i).get("id")));
            }
        }
        listmap.removeAll(waitDeleteList);
        map.remove(waitDeleteMap);
        priceControl(map);
        adapter.notifyDataSetChanged();
    }

    /**
     *
     * 手动输入条码
     * input_tiaoma
     * **/
    public  void  input_tiaoma(View view){
        // 创建一个Dialog
        final Dialog dialog = new Dialog(this,
                R.style.myNewsDialogStyle);

        // 自定义对话框布局
        layout = View.inflate(this, R.layout.dialog,
                null);
        dialog.setContentView(layout);
        //确定
        TextView title = (TextView) layout.findViewById(R.id.close1);
        //取消
        TextView title2 = (TextView) layout.findViewById(R.id.close2);

        dialog.show();
        // 设置确定按钮的事件
        title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 设置取消按钮的事件
        title2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

    }

    /**
     *
     * 数字键盘事件
     *
     * */
    public  void  numClick(View view){
        Button bt=(Button)view;
        String text= bt.getText().toString();
        EditText editText1=layout.findViewById(R.id.username);
        if (!TextUtils.isEmpty(editText1.getText())){
            if (editText1.getText().toString().length()<=15){
                text=editText1.getText().toString()+text;
                editText1.setText(text);
                editText1.setSelection(text.length());
            }
        }else {
            editText1.setText(text);
            editText1.setSelection(text.length());
        }
    }

    /**
     *
     * 删除按钮事件
     *
     * */
    public  void  deleteClick(View view){

        EditText editText=layout.findViewById(R.id.username);
        if (null!=editText.getText().toString()&&editText.getText().toString().length()>0){
            String old_text=editText.getText().toString();
            editText.setText(old_text.substring(0,old_text.length()-1));
            editText.setSelection(editText.getText().toString().length());
        }
    }

}
