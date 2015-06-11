package cn.bjzhou.kotlintest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.activity_main.*


public class MainActivity : AppCompatActivity() {

    var strs = arrayOf("aaa", "bbb", "ccc", "ddd", "eee", "aaa", "bbb", "ccc", "ddd", "eee", "aaa", "bbb", "ccc", "ddd", "eee", "aaa", "bbb", "ccc", "ddd", "eee", "aaa", "bbb", "ccc", "ddd", "eee")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var i = 0;
        listView.setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, strs))
        button.setOnClickListener { listView.setSelectionFromTop(0, --i) }
    }
}